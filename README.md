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
        
    

