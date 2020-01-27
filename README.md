# vertxReactive

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
    