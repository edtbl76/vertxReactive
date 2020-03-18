# Introduction to "Combining" 
This section covers Rx operators and factories that combine or merge multiple observables and their kin, consolidating them into a single observable type.

This is very useful for merging reactive streams together to perform operations or working with disparate sets of data. 

One of the most important notes about these operators is that they all support thread safety of Observables on different threads.

***

# Basic Merging
Takes 2+ Observables and merges them into a single Observable. 
- The merged observable is a subscriber of ALL of its merged sources. 
- This works on infinite/long running data sets in addition to more data-driven/finite workflows. 

merge()
* Observable factory that takes 2 - 4 Observables as arguments, producing a new Observable that acts as the Observer of the Observables passed in. It also takes an Iterable of a Collection that contains Observables, allowing you to circumvent the 4 arg limitation.
  - Observable.merge(observable1, observable)
  - Observable.merge(Collection-of-many-observables).

mergeWith()
* Operator to an Observable that takes up to 4 Observables as an argument. 
* the notable difference to using the operator vs the factory, is that the factory requires the creation of a new
observable, where as the operator just "connects" an existing observable to another existing observable.

mergeArray()
* This allows you to merge more than 4 Observables
* see inline notes on why I prefer not to use this. 

### Notes on Ordering.
Both merge() and mergeWith() subscribe to sources CONCURRENTLY, so it isn't recommended to rely on ordering across
Observables on separate threads.
* emissions are USUALLY fired in order if 
  - they are cold
  - they are fired from the same thread
* ordering WITHIN each Observable is preserved

If you need to preserve ordering, check out concatenation (concat())
* It's worth considering that preserving order comes at a greater performance cost than letting the chips fall. 

## flatMap()
flatMap falls into the category of basic merge, but it is without a doubt the most commonly used operator in RxJava. 

* This is essentially a dynamic version of Observable.merge() that takes each pushed event and stuffing it to an Observable. 
  - It then merges all of the pushed events from the Observables it created into a single reactive stream.
  - The dynamic aspect is that it can keep accepting new Observables to be merged into a reactive stream, indefinitely. 

This is an extremely powerful "junction" for reactive streams processing.

If you know how many you want, and it will never change... consider using merge() or the like. 
However, if you need dynamic reactivity, flatMap provides the means to do so.

*** 
I consider it criminal and slightly vulgar how little I've included on flatMap(). I could write an entire volume on flatMap() alone. It has a vast array of overloads and specific implementations for Single, Maybe etc. 

However, I'd like to get the Rx pieces done (soup to nuts) first. 
If there is sufficient demand for flatMap() deep dives, I'll be happy to go back and flesh this out a little bit more. 

***


# Concatenation
In my opinion, this is still a form of merging, but just about every book I've read creates a separate section for it, so I'm going to jump off the bridge with the real experts :)

Concatenation IS merging. However, it is ORDERED merging. 

When we merge Observables, Singles, Maybes, we are just throwing all of their concurrent events at the final reactive stream without caring about how each element is placed in the stream with respect to events from other streams. 

When we concatenate these chains together, we are telling RxJava that we want the elements and chains pushed in a specific order. 
* i.e. chain A + chain B, means that chain B will NOT fire until chain A has called onComplete(). 
* Yes, this is a BLOCKING scenario. 
* Yes, this is a bad idea for infinite chains

NOTE: I'm going to emphasize that concatenation should only be used when ordering is absolutely required. There are a number of reasons (that I've already discussed) about preferring merge() over concat() related to both performance and concurrency. 


* concat()
  - concatenation factory  (i.e. Observable.concat(observable1, observable2);)
* concatWith()
  - concatenation operator (i.e. observable1.concatWith(observable2);)

## concatMap()
concatMap() is to concat() as flatMap() is to merge(). 

Another way of saying this is that concatMap() merges each mapped Observable sequentially, and then pushes them
to the next operator/subscriber one at a time.
* This is a blocking operation, as the downstream Observables are only fired when their upstream neighbors call onComplete()
* If downstream Observables are fired faster than upstream Observables, they'll have to be cached until the upstream Observables are completed. 

As stated before, if order isn't important, don't use this. flatMap() provides a more flexible option (and it doesn't block!) 

***

# Ambiguous Factory (amb()) 
Observable.amb() accepts an argument of Iterable<Observable<T>>
* emits the first Observable that fires, and disposes of the rest. 

This is great when you have redundant processes trying to fire the same events, and you only want one of them to be consumed. (otherwise you'll end up with duplicate data, and that is a very bad thing for some industries.) 


Observable.amb()
* this is the factory version that takes an Iterable<Observable<T>>. 

observable1.amb(observable2)
* This is the operator version

you can also use ambArray(), but I'm not going to demonstrate it because I don't hate myself enough to demonstrate code that I know requires a @SuppressWarnings("unchecked") annotation.

***

# zip 
Zip-a-dee-doo-dah, Zip-a-dee-ay!

Zip takes AN (singular) event/emission from multiple given Observable sources and combine it into a single emission. 
* The emissions do NOT have to be the same type. 

zip()
* zip is a factory that works like most of the other factories. It takes (up to 9) sources that it will
zip up together
* in order to provide more than the number of supported ObservableSource<T> values, you can use zipArray or provide an Iterable to zip, which bypasses the overhead associated with creating separate Observable objects. 

NOTE: Normally I prefer the latter, but in this one case, you'll find that using the Iterable<Observable<T>> is
a bit more painful than it is in the case of other operators.

zipWith()
* this is the standard operator that works like most other blahWith() operators. 


## Performance and Latency
For a moment, let's think back to some of our infinite examples, where emissions are coming in and different intervals. 
* in order to do its job, zip() has to queue up the fastest emissions, waiting on slower emissions to catch up in order to pair them together. 
  - yes, this causes latency and theoretically blocks until all of the pairs are matched up. 
  - (see combineLatest() as a solution to this problem if you are only interested in the most current data). 

## zipIterable()
* provides additional configuration options to defer "in-chain" errors to be raised until after all chains terminate. 
  - this allows all events that CAN be emitted, to be emitted, rather than allowing exceptions to supercede events. 
  - different strokes for different folks... keep in mind that both of these situations might be appropriate depending on the given use case.
* also allows prefetch of events to remediate impact caused by caching (and potentially blocking) of the fastest emissions. 

***

# Combine Latest 
When you don't care about catching up cached data, and you only need the latest emissions, combineLatest() is the
operator that tends to work best (as an alternative to zip())

This is commonly used in real-time applications and UI code where user inputs are being constantly updated. 

## zip() vs. combineLatest()
One of the major differences between these two is that zip is "symmetrical" and combineLatest() is not. 

* zip combines multiple observables, but it pairs them based on their position. The first combine operation is always going to include the first element of ever observable provided to zip
  - this means that if one observable is being pushed faster than the others, it's events must be queued and stick around until everyone else catches up.

* combineLatest just uses the latest emissions. This means that every time the "fastest" observable fires, you are going to get that event paired with a null or empty event or whatever the default is for all of the other Observables, until they start firing. There is no waiting or caching. You get what is flowing in real time.

# withLatestFrom()
This is similar to using combineLatest(),but only on Tuesdays. Just kidding. 
* It is an operator (not a factory) that matches each emission from THIS Observable with the latest emissions from the parameter (THAT/THOSE) Observables.
* it combines the emissions, but it will only take ONE emission from each of the other observables.

## combineLatest() vs. withLatestFrom()
The difference that I want to point out here is when emissions happen "simultaneously"
* with combineLatest() if I emit every 1 second from one observable and every 10 seconds from another observable, then every 10 seconds these two observables will have a concurrent set of emissions. combineLatest() consumes BOTH, resulting in some duplicate/errant data. 
* withLatestFrom() eliminates this duplication by "keying" off of the observable from which the operator is called. i.e. the context of consumption is from THIS Observable, and THAT/THOSE Observables are just along for the ride!

## argument limit
Like most of the observable operators, these both can only accept up to 4 ObservableSource arguments. In order to 
take more than that it accepts Iterable<Observable<T>>

***

# Groups
Grouping in Rx is performed by using a special "key" that splits out emissions into separate Observables. This is very useful for doing the opposite of combining.... splitting things apart so that they can be handled differently based on specific characteristics. 

## How it Works
* groupBy() operator takes a callback that maps events to a key. 
* returns an Observable<GroupedObservable<K,T>>

## GroupedObservable
This is an Observable that behaves like a K/V, where the K = the key property that you can use for accessing the grouped data. It pushes out T events that were grouped by the given K.

Normally, you are going to have to use a combining technique for the GroupedObservable (we just learned a crapload of them, right??).
* flatMap() or flatMapSingle() are the most common choices, the latter of which often results when collecting or reducing within a flatMap() to a Single common-key event. 

### Room Temperature: Where Hot and Cold Observables meet. 
An interesting observation about GroupedObservables is that they exhibit characteristics of both hot or cold Observables. 
* they don't replay missed emissions to a second Observer (therefore they aren't completely cold)
* they DO cache events and push them to the first Observer, guaranteeing that all of the events are consumed.