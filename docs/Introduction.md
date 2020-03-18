This contains some basic examples of the Observable-Observer relationship. 

It also covers an intro to managing the main() thread of an application in order to allow
intervallic generation of events. This is an important concept that relates to an infinite/long-running
emission of events, as well as thread management. 

### Key Concept
Reactive Streams (Observables) are emitted/pushed towards their destination
Non-Reactive Streams are pulled/polled from a source.

This is an important aspect of reactive programming. There is an aggregate overhead associated with pulling/polling information while we wait for that information to exist, fetch it, and then process it. Emitting events allows us to minimize the computational and temporal overhead with the aforementioned model. 


