# Basic Building Blocks


## Observables
An Observable is the "basic" unit of Reactive Streams. 

It is a data structure that consists of zero or more events represented by another data structure. 
If we remember the reactive contract from Home, we'll recall that Observables don't pass events concurrently. 

### "The Callback Trinity"

#### onNext()
This is called SEQUENTIALLY (Reactive Contract!) for each event within an Observable. onNext acts like a router, only interested in forwarding the event to its next step. A single Observable is not aware of whether or not the next "step" is the final destination or another link in the chain

#### onError()
This communicates an exception that has been encountered during the processing of a particular event
Generally, if the condition for the error can be handled, it is often done so with a retry() or a logic within the Observable. If the error condition can't be handled conventionally, it will be captured via onError() and 
pushed to the final destination (Observers: we'll get to these) allowing the condition to be handled there.

#### onComplete()
When a finite data set has drained to zero, onComplete is called to signal "End of Line"
If an Observable has no events, then onComplete() is immediately called.

### Terminal Callbacks.
onError and onComplete are terminal callbacks. No further processing is performed once one of these two callbacks
are called.

### defer vs. fromCallable
These are two factories that have significance to RxJava because they provide a way to lazily load Observables. By default, Observables are eagerly loaded.

defer() returns an Observable, that uses an ObservableSource factory to generate a new Observable for each new Observer.
* The factory function itself determines the ObservableSource that will be "observed" by the Observer. 

FromCallable generifies defer(), by calling a function you provide, and then emits that value onto the 
chain. 

Looking at the two method signatures, you'll note that fromCallable supports a non-RxJava callback as an argument.
* public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> supplier)
* public static <T> Observable<T> fromCallable(Callable<? extends T> supplier)

***

## Observers
An Observer is the "final destination" of an Observable. If you break this down conceptually, it makes sense. 

An Observable is a tree falling in the woods.  
The Observer is someone/something to record that it happened.


    EXAMPLE
    
        public interface Observer<T> {
            void onSubscribe(Disposable d);
            void onNext(T value);
            void onError(Throwable e);
            void onComplete();
        }

### Callbacks
the callbacks of an observer determine what type of Observer is used. They should look familiar.
- onNext()
- onError()
- onComplete()

# Disposables

## Prelude to Disposable: revisiting subscribe()
In order to understand how a Disposable works, we need to understand how subscribe() consumes events being pushed up the chain.

1. when subscribe() is called, it starts "listening" to the end of the event chain it has been called from.
2. subscribe() consumes the events and performs actions based on the logic in the callbacks and the type of the observable
* (i.e. onError captures events that blow up, onComplete captures the end of line, etc.) 
3a. For finite data sets, once the emissions stop, onComplete is called, and any resources associated with the reactive stream that was created are destroyed so that they can be properly garbage collected
3b. Infinite data sets, by definition, will never reach onComplete() while a system is up. 
* in theory, most use cases for infinite data sets "close" when the application is stopped. As a result, everything is perfectly cleaned up and memory leaks never happen right? 
* Exactly. Disposables have many uses for controlling long running/infinite data sets, but arguably their most valuable use case is simply good resource management. When the application/service that owns the chain is going to be nuked, make sure you are disposing the chain before it goes down. 

> Everything you leave to chance is a gamble. 

### the Disposable
Think of this as a connection or glue between the Observable and the Observer. 
* before an Observable is "consumed" it's just an Observable. This means it CAN BE observed, but HAS NOT YET.
* once you call subscribe(), the Observable is now "Observed". Once it is "Observed" it becomes "Disposable"

An important concept to note is that a Disposable is the return value regardless of what callback is executed within subscribe(). 
* Theoretically speaking, terminal events (onError(), onComplete()), will also return a Disposable, even though
the actual state of isDisposed() is true. It's kind of like getting a dud firecracker.

A Disposable has two useful methods
1. isDisposed(), which can intuitively be used to check the disposal state of a chain.
2. dispose(), which can intuitively be used to stop emissions from occurring on a chain.

    Interface:
    
    public interface Disposable {
        void dispose();
        boolean isDisposed();
    }

#### Disposing mid-chain
It is possible to override the methods of the Observer interface in order to gain access to the Disposables for 
the provided callbacks. 
* This allows us to pass a Disposable downstream through the event chain. (Disposables are pushed downstream via the onSubscribe() method of an Observable(or the like) interface)
* each of the following operators now has access to the Disposable and therefore can perform computation against it.

### ResourceObservers
This is a special type of Observer that has a Disposable return type. (Standard Observer is void). The purpose is to allow for event disposal at the end of the chain. 

#### Caveats
Since subscribe() doesn't support an Observer as a parameter, we have to use subscribeWith() to take advantage of 
a ResourceObserver. It's a package deal. Resource Observers + subscribeWith()


***

## Putting it Together: Observable Chains
First, we have a "source" observable that pushes events onto the reactive stream. 

Observables are connected or "chained" together with operators. (We'll get to these in a bit.) 

Once we are in the stream/chain, we have in-chain "observables" that are "routers". They have absolutely no
context prior to their input (the observable they've received events from) or beyond their output (the Observer
they are pushing events to). 

## Observable LifeCycle

This is a critical concept of reactive programming. 
The lifecycle of each observable dictates perspective and sequencing. 

1. Every Observable begins its life as an Observer. 
* the "source" observable is an Observer of the factory used to generate events. (i.e. create(), just(), etc.) 
* in-chain observables are observers of the data set being emitted from the previous operator (Not everything makes the cut... see "Suppressors")

2. Something happens
* typically an operator does something with the data (filters it down, changes it, et al.) 

3. A data set is left behind
* the "result set" becomes the Observable for the next link in the chain. 
* this could be empty or even an exception.

The data from step 3 is consumed by the next operator, whether that is another link in the chain or the final 
destination. At that point, the next stage begins its life as an Observer. 
