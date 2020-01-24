# vertxReactive

---
## Introduction 
This contains some basic examples of the Observable-Observer relationship. It also
covers an intro to managing the main() thread of an application in order to allow
intervallic generation of events. 

### Key Concept (Observables vs. Streams)
- Observables are PUSHed where as Streams are PULLed. 

---
## ObservableObserver
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

