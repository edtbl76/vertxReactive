# Introduction to Operators 
Operators are the "business logic" workers of Reactive Programming. These are essentially chain-able convenience functions that allow us to create a pipeline for "reactive streams"/event chains. 

I haven't decided which term I like better, so I'll just flip back and forth :)

## Keeping them simple
Simple Operators are the types of operators you should be able to use fairly quickly. They are straightforward

One final note that I feel is probably overkill, but I have seen many developers overlook. 
Operators are written as a chain of events. This means that the INPUT to any given operator is the output of the
stage before it. 

If I have one billion events from the initial Observable, and every event is suppressed by the first operator, then the input is effectively Observable.empty() for any other step in the chain (unless there is a default/switch action that results in the insertion of alternative events/values.) The point to take away from this is that complicated chains of events may behave differently than expected based on the order in which it was written. 

## THE EASIEST WAY TO TROUBLESHOOT COMPLEX CHAINS:
Break them into smaller chains. If some result isn't what you expect, then reduce the problem to the inputs/outputs occurring at certain points of the chain, and replace the code with each half of your chain to ensure that Observables are pushing the events you expect, and Observers are consuming the events that you expect. 

End Rant :)

***

## Operators 1: Suppressors
These are operators that work to suppress events based on one or more attributes that determine whether or not the event should be pushed to the next "link" in the chain.

filter()
* suppresses events that evaluate to false based on a given Predicate<>

take()
* emits events based on given number, suppresses following events
* suppresses events after which a duration of time based on a given number, followed by a given TimeUnit expires.

skip()
* opposite of take()
* suppresses events based on given number, emits following events
* suppresses events for a duration of time based on a given number, followed by a given TimeUnit.

takeLast(), skipLast()
* similar to take()/skip(), but it evaluates the last number of events from the chain.
* NOTE: this results in slower performance and "synchronous-like" behavior, because it must wait for the entire chain of events to be pushed before it can determine which events to be suppressed.

takeWhile(), skipWhile()
* I prefer to think of these as "smart" versions of take() and skip() that accept a Predicate as an argument. suppression is carried out based on how events are evaluated against the Predicate. 

distinct()
* accepts various types of arguments that suppress events based on the identification of duplicate events (or some attribute subsequent to the event)

distinctUntilChanged()
* this is a variant of distinct() that is used for suppressing consecutive repetition of the same event. 
* I often think of this in terms of log suppression.

elementAt()
* This is more or less like index-level access of an event within a given chain. 
* there are many variations (see code examples for more details)
* returns a Maybe, where 1 means the element exists or 0 means it doesn't. 

***

## Operators 2: Transforming Operators
These are operators that perform a change of some kind. The change could be to the entire chain, each event within a chain, specific events that meet certain criteria or even the way that the events are emitted to the next link

map()
* map is like a Man-in-the-middle. It acts as an Observer to the upstream Observable, such that it consumers the unaltered events. 
* Afterwards, map() mutates the data ONE AT A TIME (Reactive Contract!) in some fashion
* finally it acts as the Observable to the downstream chain of operators until reaching the final Observer/Consumer. 
    
startWith() & startWithArray()
* this is a way to insert data before events are pushed. (There are various uses for this such as providing a title header, or a pointer that signals the start-of-messages etc.)
    
defaultIfEmpty() & switchIfEmpty()
* provide default values/observables when a particular observable results in empty (for whatever reason... i.e. as the result of suppression from previous operators in a chain, or if it was empty initially)
    
sorted()
* Do I really have to explain this one? The functionality is hopefully obvious, however there are some caveats relative to performance. 
* first off, this is a natural order sort by default (Comparators are supported as args)
* in order to sort the items, all of the items must be present. This is SLOOOOOOW against large data sets. Don't do it. That's a 6-O Slow. This is impossible against infinite observables... it WILL result in an OutOfMemory error.
            
delay()
* Like sorted, this is fairly self explanatory. It inserts a delay where-ever in the chain you stick it. This works similar to interval() such that the main thread needs to be "activated" in order to ensure that the computation scheduler is executed. 
* it is actually a fairly powerful operator. While my example is fairly basic, you can also pass in a separate
Observer that will act as a trigger, such that the "delayed" Observer will emit when events are pushed through the "argument" Observer. 
* I doubt many people will get the analogy, but it is similar to providing a side-chain to a compressor. 
    
repeat() & repeatUntil()
* This is mostly self explanatory. I'm going to "do something over and over". The basic param is just "the number of times to do it again". You can also supply a BooleanSupplier (repeatUntil()) as a way to provide a stop condition.
    
* NOTE: no parameter results in an infinite sequence of Single event Observables. This is not, I say, this is NOT, an infinite Observable. 
 * an infinite Observable is a sequence of infinite events that never reaches onComplete. 
 * an infinite sequence of single event Observables is a constant sequence of alternating onNext, onComplete?

scan()
* This was the official winner for the poorest naming convention of any Rx operator. It was given a short vacation to Tierra Del Fuego. 
* relevant texts, documents and scrolls discovered in seaside caves call this a "rolling aggregator". Each emission uses a biconsumer with an accumulator and the result of the summation. This is useful for counters and.. accumulators etc. 
    
cast()
* there is an operator called cast used as a Brute-force method for casting one object to another due to potential inheritance/polymorph collisions. 
* DO NOT USE THIS UNLESS YOU ABSOLUTELY MUST. 
 * It is my preference to properly use wildcards, generics etc. in these cases. 
 * I am always very concerned when I read "brute-force" in official documentation. 
 * I'm also concerned that proper alternatives aren't presented, so I'm going to provide them here. 
  * If you can not otherwise solve type issues WITHOUT BREAKING THE FUNDAMENTAL RULES OF REACTIVE PROGRAMMING then, and only then, may you consider using the cast() operator after slaying Medusa and bringing back the Golden Fleece.
  
***

# Reducers
This is the same type of action you expect it is. The purpose of a reducer is to take an Observable (or Flowable) and consolidate it into a single emission (This isn't always a Single)

NOTE: scan() vs. reduce() are different, but easy to get confused. 
* scan emits "rolling aggregation" (i.e. many emissions)
* reduce yields a SINGLE EMISSION that represents the FINAL ACCUMULATION once onComplete() is called. 
* This should go without saying, but I will anyway. It isn't plausible to consolidate infinite data sets. 

count()
* provides the number of events presented to it as a Single

reduce()
* uses the same exact "business logic" as scan(), BUT, it returns a Single/Maybe (Depending on how you set it up) that emits the final aggregation of all events presented to the operator. 
       
all()
* This is essentially a form of ACP (Atomic Commit Protocol) that takes a Predicate, such that if one sub-action of an action fails, then the entire action fails. 
* It could also be thought of an AND operation, which emits a Single<Boolean>

any() 
* this is more like an OR operation. If any of the results fit the provided Predicate, then it is true.
* emits a Single<Boolean>
    
contains()
* accepts a parameter of the same type being pushed by the Observable. 
* returns a Single<Boolean> indicating whether the value represents an event in the Observable. 

***

# Collectors
These are sort of like reducers, but instead of consolidating a bunch of events into a single event all of the events are stuffed into a Collection, and the Single Collection is emitted as the result. 

The commonality is that both reducers and collectors reduce an observable/flowable into a single emission.

Collectors should be reserved for "the logical grouping of events". In other words, the choice to collect() should
be deliberate and well thought out. Don't do it just because Collections are easier to work with or you are more 
familiar with them. (When all else fails, review the best practices!)

I'm not defining these. They should be fairly self explanatory. 

toList()
toSortedList()
toMap() & to MultiMap()

collect()
* I lied, I'll explain this one. This is the generic collector when the pre-baked operators aren't good enough. 


## collect() vs. reduce()
We mentioned before that reduce() is not recommended for stuffing events into mutable objects. That being said, 
you may have the need for doing so. 

***

# Exception Operators

## onError 
Let me start by saying this is NOT about onError().

* onError() is a callback that captures an exception and converts it to an event that is
pushed down the chain of operators to the final Observer so that the Observer has the 
responsibility of handling the error. 

    - what if the error condition is recoverable? If we wait for the Observer to handle it, then
    it is likely that we'll have to handle the recovery at the Observer and push it into another Observable to 
    be consumed again. 

    - It makes more sense to implement recovery closer to the point where the exception occurred. 
    
## Not onError
onErrorReturn() & on onErrorReturnItem()
* these are methods that provide alternate values to the exception message. 
* This allows you to stop the chain AT THAT POINT, but continue to process that final event instead of blowing up. 
  - The chain of operators still ends at this point. 
    
onErrorResumeNext()
* This is slightly flexible in that it allows you to accept another Observable to be executed when an exception is encountered. 
 
retry(), retryWhen() and retryUntil()
* retries are pretty self explanatory. See the code for more examples.

***

# Lights! Camera! Action Operators
These are better known as the doOns and doAfters. These are sneaky little stinkers that act like an Observer stuffed into the middle of a chain of Observables. 

The doOn is usually a Consumer that performs an action before the event is pushed downstream. (You can also think of it as being performed "Right Now", like the Van Halen song.)

* doOnNext(), doAfterNext()
* doOnComplete()
* doOnError()

* doOnEach()
  - fires for onNext + onComplete OR onNext + onError.
* doOnTerminate()
  - fires for terminators (i.e. onError or onComplete)

* doOnSubscribe, doOnDispose, doFinally
  - doOnSubscribe is used to create a disposable. 
  - doOnDispose is used to create an action when dispose/onDispose is activated.
  - This is pretty rare unless we are talking about infinite/long running observables. If onComplete() is reached, then the state of the Disposable.isDisposed() is true... so doOnDispose() doesn't fire. 
  - doFinally is like doTerminate and doOnDispose had a baby. It fires for onComplete, onError and onDispose 

Please review each example for details. 

It is HIGHLY recommended that you investigate the use of these convenience functions. Debugging reactive streams
is much easier if you understand how these work. It is also a very short "walk" from using these as debugging tools to using them as hooks for instrumentation. 

Concurrency and Reactive Programming can be challenging paradigms, especially when reviewing strange unintuitive failure modes. Action Operators can shed some "light" on these issues, and provide a "camera" to record the events being investigated. (I'm silly)

* A NOTE ON DEBUGGING
doOn/Action operators are one of the single most important debugging tools you have available to you when working with RxJava. 

When output is weird, or the result isn't what you expect, you can insert doOnNext() operators with print statements that will allow you to mark your output. 

