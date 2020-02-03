# vertxReactive

Being Reactive means
- lower memory usage
- robust concurrency
- disposability (i.e. cleaning up after yourself!!!!)

---
## Introduction 
This contains some basic examples of the Observable-Observer relationship. It also
covers an intro to managing the main() thread of an application in order to allow
intervallic generation of events. 

### Key Concept (Observables vs. Streams)
- Observables are PUSHed where as Streams are PULLed. 

---
## Observable
### Key Concepts
- onNext()
    - passes each item sequentially from Source (Observable) to destination 
    (Observer)
- onCompleted()
    - commmunicates a "completion event" which tells the observer that
    the observable doesn't have anything else to listen to
- onError()
    - communicates an error, where the OBSERVER (usually) determines what to 
    do with it
    - Observable can use retry() operator to retry the event before sending
    an error. 
    - normally, if an error occurs, the emissions stop and the game is over.
    
### Covers Observable Factories
- create()
- just()
- fromIterable()
- range() or rangeLong() 
- interval()
- future()
- empty(), never(), error()
- defer()
- fromCallable()

    
### ReactiveContract
- events must be passed SEQUENTIALLY and ONE-AT-A-TIME
    - This means that an Observable can NOT pass events concurrently. 
    
---
## Observers

### KeyConcepts (Should Seem Familiar)
- onNext(), onComplete(), onError()
    - these methods define the TYPE of Observer that is used. 
    
    
    EXAMPLE
    
        public interface Observer<T> {
            void onSubscribe(Disposable d);
            void onNext(T value);
            void onError(Throwable e);
            void onComplete();
        }
        
### Observable Chains
- the first Observable in a chain emits events. 
- in-chain observables don't know where events originate from, or if
the next event is "the end"
- each observable returned by a chain operator
    - starts its life as an Observer (consuming the event from the
    upstream Observable)
    - processes/transforms the event data
    - becomes a "relay" Observable to move the event further downstream
    
The key concept here is that technically every Observable but the "source"
observable starts its life as an Observer. 

---
# Cold And Hot
These are descriptions concerning how Observables handle the data emitted to 
multiple subscribers. 

## Cold Observables
Every time an observer subscribes to a COLD observable, it replays 
ALL of the data from the observable. 
- Most "data-driven" Observables are COLD
    - .just() and fromIterable()
    - Databases, TextFiles, JSON, etc. 
- usually represents "FINITE DATA SETS"
    
NOTE: 
- if the data changes between two subscriptions, the second Observer will get
different emissions than the first. 

## HotObservables
- Subscribers only get "up to data" information, i.e. what is expressly
being produced at the time the subscriber observes the observable. 
    - this means that old data isn't "replayed"
- as opposed to Cold Observables, hot observables usually represent "EVENTS"
    - event MIGHT carry data with it, but it is "time-sensitive"
    
### Connectable
- special type of Hot Observable
- it takes ANY Observable (even cold ones!) and make it HOT so that all
emissions are played to all Observers at once. 

PROS:
- supports multicasting of events to all observers
- prevents reply of data to each Observer. 
    - i.e. replaying is expensive, or data flow should be real-time.

CAVEATS
1. the conversion is performed by calling publish() on any Observable
2. subscribe() doesn't fire the emissions on the observer, you must use
connect() instead.

#### Multicasting:
One of the major benefits of ConnectableObservers is that it allows you to 
fire events to ALL OBSERVERS SIMULTANEOUSLY

---
# Single
- This is a special version of Observable that only emits a single item. 
- it works the same, but it has a smaller repertoire of operators (i.e.
only the ones that can be sensibly used for a single item.)

- single there is only one item, onSuccess() is more or less a 
combination of onNext() and onComplete().

- you can convert a Single back to an Observable 
    - using toObservable()

    
    Single Interface
    
        interface SingleObserver<T> {
            void onSubscribe(Disposable D);
            void onSuccess(T value);
            void onError(Throwable throwable);
        
        }
        
# Maybe
- like a single, but it allows an empty() emission.
- onNext() is only called when an emission is present
- onComplete() is called either way.

    
    Maybe Interface
    
        interface MaybeObservable<T> {
            void onSubscribe(Disposable d);
            void onSuccess(T value);        <-- Replaces onNext()
            void onError(Throwable throwable);
            void onComplete();
        } 

---
# Completable
- Rx operator that only cares about whether or not the event took place. 
- does NOT receive ANY emissions.
    - no onNext()
    
    
    Completable Interface
    
    interface CompletableObserver<T> {
        void onSubscribe(Disposable d);
        void onComplete();
        void onError(Throwable throwable);
    }

    
---
# Disposables

## How subscribe() works
when subscribe() is called, it begins "listening" to an Observable. 
- stream is created to process events through Observable -> Observer chain.

When we are done w/ the data/events, we want the resources associated w/
the streams to be disposed so that they can be properly GC'd. 

- finite observables (the ones that call onComplete()) SHOULD clean up
- infinite or long running observables require our intervention. 
    - truthfully, RxJava recommends explicit disposal of resources
    due to "lack of trust" in GC, to prevent memory leaks. 
    
## Disposable
- connection between Observable and an ACTIVE observer. 
- useful methods
    - isDisposed() indicates if resources have been canned
    - dispose(), stops emissions and disposes of ALL resources for
    that Observer. 
    
    
    Interface:
    
    public interface Disposable {
        void dispose();
        boolean isDisposed();
    }
    
- When onError, onNext and onComplete are provided to subscribe(), 
it returns a Disposable, which dispose() maybe called against to 
manage the stopping of emissions and trashing of resources.
    - if we override the methods of an Observer we gain access to 
    their disposables, allowing any of these three events to call
    dispose()
   - the disposable returned at this point is sent through the
   "Observable" chain. 
    - this means that ever link in the chain can access, and therefore
    USE the Disposable. 

- Disposable is passed into an Observer via onSubscribe()
    - the entire purpose of this is to give developers the option to
    kill the subscription at a point in time of their choosing. 
    
### ResourceObserver
- This is an Observer that uses default Disposable handling. 
    - This exists because you can NOT pass an Observer to subscribe(). 
    - A standard Observer is of type VOID, because it is designed to
    handle Disposables. 
    - ResourceObserver is a special version of Observer that is passed to
    subscribeWith()
        - this returns a Disposable, so you can manage event disposal.
        
---
# Operators
These are the many many nuggets of functionality that enable the execution of business logic within the Rx framework. 
- an operator is an OBSERVER to the Observable it is executed against. 
- the operator "DOES THE THING" and then it becomes the PRODUCER for downstream Observers. 


       BEST PRACTICES:
       - Observable STARTS THE THING
       - Operators DO THINGS
       - a Final Observer CONSUMES THE THING
       
            That's it. You don't get points for being clever. 
       
       
       BAD PRACTICES:
       - DON'T CHEAT
       - don't extract values out of the middle of the chain. 
       - don't use blocking processes
       - don't use imperative programming. 
       
       
One final note that I feel is probably overkill, but I have seen many developers overlook. 
Operators are written as a chain of events. This means that the INPUT to any given operator is the output of the
stage before it. 

If I have one billion events from the initial Observable, and every event is suppressed by the first operator, then the
input is effectively Observable.empty() for any other step in the chain (unless there is a default/switch action
that results in the insertion of alternative events/values.) The point to take away from this is that complicated chains
of events may behave differently than expected based on the order in which it was written. 

THE EASIEST WAY TO TROUBLESHOOT COMPLEX CHAINS:
- break them into smaller chains. If some result isn't what you expect, then reduce the problem to the inputs/outputs
occurring at certain points of the chain, and replace the code with each half of your chain to ensure that 
Observables are pushing the events you expect, and Observers are consuming the events that you expect. 

End Rant :)

## Suppressors
Operators that SUPPRESS some or all events based on input parameters that determine what is to be suppressed, and how
that suppression will be carried out.

- filter()
    - accepts a Predicate, emitting the events that evaluate to true to the next Observer. 
    
- take()
    - accepts a number that acts as a hard upper limit of the number of 
    of events that will be emitted before onComplete() is called. 
    - accepts a number, followed by a TimeUnit that represents a timebox in which events will be pushed.
- skip()
    - This is essentially the OPPOSITE of take. 
    - accepts a number as the number of events to skip before the Observable begins emitting.
    - also accepts a number, followed by a TimeUnit that represents a duration of time in which events are 
    skipped before they begin being pushed. 
    
- takeLast()
    - similar to take(), but instead of taking the first N events, it takes the last N events
    - NOTE: this results in a phenomenon dubbed "queue and delay", because the entire chain of events
    must be emitted before the operator can determine what the last N events are. 
- skipLast()
    - similar to skip(), but instead of skipping the first N events, it skips the last N events. 
    - this is likewise subject to "queue and delay"
    
- takeWhile() & skipWhile()
    - these are smarter versions of take and skip, that accept a Predicate as an argument. 
    - these operators suppress emissions based on the the conditions of the Predicate.
    
- distinct()
    - accepts various types of arguments that allow suppression of events based on identifying the duplication of
    the event (or some attribute thereof)
    
- distinctUntilChanged()
    - specialized implementation of distinct() used for suppressing consecutive repetition of the same events. 
    
- elementAt()
    - there are many variations of this, but it returns a Maybe, because it can return a HIT (i.e. the element exists)
    or a MISS (if the index is out of bounds or empty)
    
## Transformers
Robots in Disguise. 
These are operators that perform some form of mutation on the events. (events are data. an event is a datum. the end
result is a collection of events that are "hopefully" meaningful INFORMATION to the user).

- map()
    - map is like a Man-in-the-middle. It acts as an Observer to the upstream Observable, such that it consumers 
    the unaltered events. 
    - Afterwards, map() mutates the data ONE AT A TIME (Yes... this is important) in some fashion
    - finally it acts as the Observable to the downstream chain of operators until reaching the final Observer/Consumer. 
    
- startWith() & startWithArray()
    - this is a way to insert data before events are pushed. (There are various uses for this such as providing a 
    title header, or a pointer that signals the start-of-messages etc.)
    
- defaultIfEmpty() & switchIfEmpty()
    - These are convenience functions that provide default values/observables when a particular observable results in
    empty (for whatever reason... i.e. as the result of suppression from previous operators in a chain, or if
    it was empty initially)
    
- sorted()
    - Do I really have to explain this one? 
    - The functionality is hopefully obvious, however there are some caveats relative to performance. 
        - first off, this is a natural order sort by default (Comparators are supported as args)
        - in order to sort the items, all of the items must be present. 
            - this is SLOOOOOOW against large data sets. Don't do it. 
            - this is impossible against infinite observables... it WILL result in an OutOfMemory error.
            
- delay()
    - Like sorted, this is fairly self explanatory. It inserts a delay where-ever in the chain you stick it. 
    - This works similar to interval() such that the main thread needs to be "activated" in order to ensure
    that the computation scheduler is executed. 
    
    - this is actually a fairly powerful operator. While my example is fairly basic, you can also pass in a separate
    Observer that will act as a trigger, such that the "delayed" Observer will emit when events are pushed through the
    "argument" Observer. 
    
- repeat() & repeatUntil()
    - This is mostly self explanatory. I'm going to "do something over and over"
    - The basic param is just "the number of times to do it again"
    - You can also supply a BooleanSupplier (repeatUntil()) as a way to 
    
    - NOTE: no parameter results in an infinite sequence of Single event Observables. 
        - this is not, I say, this is NOT, an infinite Observable. 
        - Ed, what's the difference? 
            - well, since you asked, an infinite Observable is a sequence of infinite events that never
            reaches onComplete. 
            - an infinite sequence of single event Observables is a constant sequence of alternating onNext, onComplete?
        - This is an oversight in my opinion, but I digress. 
        
- scan()
    - This was the official winner for the poorest naming convention of any Rx operator. It was given a short vacation
    to Tierra Del Fuego. 
    - scan() is what the docs call a "rolling aggregator". Each emission uses a biconsumer with an accumulator and the
    result of the summation. 
        - this is useful for counters and.. accumulators etc. 
    
- cast()
    - there is an operator called cast used as a Brute-force method for casting one object to another due to potential
    inheritance/polymorph collisions. 
    - DO NOT USE THIS UNLESS YOU ABSOLUTELY MUST. 
        - It is my preference to properly use wildcards, generics etc. in these cases. 
        - I am always very concerned when I read "brute-force" in official documentation. 
        - I'm also concerned that proper alternatives aren't presented, so I'm going to provide them here. 
        - If you can not otherwise solve type issues WITHOUT BREAKING THE FUNDAMENTAL RULES OF REACTIVE PROGRAMMING 
        (also mentioned above at the beginning of the Operators Section)
        Then you may consider using the cast() operator. 
        
## Reducers
This is the same type of action you expect it is. The purpose of a reducer is to take an Observable (or Flowable) and
consolidate it into a single emission (This isn't always a Single)

NOTE: scan() vs. reduce() are different, but easy to get confused. 
- scan emits "rolling aggregation" (i.e. many emissions)
- reduce yields a SINGLE EMISSION that represents the FINAL ACCUMULATION once onComplete() is called. 

NOTE: This should go without saying, but I will anyway. It isn't plausible to consolidate infinite data sets. 

- count()
    - provides the number of events presented to it as a Single

- reduce()
    - uses the same exact "business logic" as scan(), BUT, it returns a Single/Maybe (Depending on how you set it up)
    that emits the final aggregation of all events presented to the operator. 
       
- all()
    - This is essentially a form of ACP (Atomic Commit Protocol) that takes a Predicate, such that if one sub-action of 
    an action fails, then the entire action fails. 
    - It could also be thought of an AND operation
    - emits a Single<Boolean>

- any() 
    - this is more like an OR operation. If any of the results fit the provided Predicate, then it is true.
    - emits a Single<Boolean>
    
- contains()
    - accepts a parameter of the same type being pushed by the Observable. 
    - returns a Single<Boolean> indicating whether the value represents an event in the Observable. 
    
## Collectors
These are sort of like reducers, but instead of consolidating a bunch of events into a single event all of the events
are stuffed into a Collection, and the Single Collection is emitted as the result.
- (The commonality being that both reducers and collectors reduce an observable/flowable into a single emission)

NOTE:
- Collectors should be reserved for "the logical grouping of events". In other words, the choice to collect() should
be deliberate and well thought out. Don't do it just because Collections are easier to work with or you are more 
familiar with them. (When all else fails, go to the beginning of the Operators section and review the best practices!)

NOTE: 
- I'm not defining these. They should be fairly self explanatory. 

- toList()
- toSortedList()
- toMap() & to MultiMap()

- collect()
    - this is the generic collector when the pre-baked operators aren't good enough. 


### collect() vs. reduce()
We mentioned before that reduce() is not recommended for stuffing events into mutable objects. That being said, 
you may have the need for doing so. 

## Error Management Operators

### onError 
Let me start by saying this is NOT about onError().
- onError() is a callback that captures an exception and converts it to an event that is
pushed down the chain of operators to the final Observer so that the Observer has the 
responsibility of handling the error. 

- There is one problem with this model
    - what if the error condition is recoverable? If we wait for the Observer to handle it, then
    it is likely that we'll have to handle the recovery at the Observer and push it into another Observable to 
    be consumed again. 
    - It makes more sense to implement recovery closer to the point where the exception occurred. 
    
- onErrorReturn() & on onErrorReturnItem()
    - these are methods that provide alternate values to the exception message. 
    - This allows you to stop the chain AT THAT POINT, but continue to process that final event instead of blowing up. 
    - NOTE: The chain of operators still ends at this point. 
    
- onErrorResumeNext()
    - This is slightly flexible in that it allows you to accept another Observable to be executed when an exception is
    encountered. 
 
 - retry(), retryWhen() and retryUntil()
    - retries are pretty self explanatory. 
