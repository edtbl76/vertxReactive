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
       
       WHY?

