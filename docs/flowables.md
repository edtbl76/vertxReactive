# Flowables! I've heard so much about you!
Yes. I've mentioned them. 

## Quick Review of the Producer/Consumer Problem associated with Concurrency
When we started talking about concurrency, we were discussing ways to scale up. However, as is the case in software (and many other idioms), solutions are often more like a game of kick the can down the road than they are an absolute solution to all problems. 

Concurrency allows us to accomplish more work in less time by doing things over the same period of time. However, in most cases, we need to bring those concurrent tasks back together to a single point for consumption. In highly concurrent applications/systems we are effectively creating a tsunami of events crashing down on the consumer. 

In the previous section we discussed ways to group events in "bite size" chunks (i.e. window(), buffer()), as well as ways to prune/optimize our usage of concurrency by discarding events we believe are unnecessary (throttling, and switchMap()).

## The Other Way (Flowables).
The previous examples have their uses, but if we are generating more events than we can consume, it makes sense for us to control the flow/pace of event delivery from the perspective of the consumer's capacity to consume them. 

***

# Backpressure

## Reviewing the chain
When we consider an observable chain, we use verbs like "emit" or "push". We think of the direction/flow of events
as moving from a source observable, maybe through additional operators, eventually being consumed by the subscriber/consumer. 

This happens synchronously, one event at a time. As you recall, this is slow, because it means that while one event is working its way from source to consumption, all of the other events are essentially queued. 

This is slow, but it is almost impossible to produce events faster than the consumer can handle them using this pattern. 

## Concurrency and Asynchronous operation.
Certain operators or reactive features allow the introduction of concurrency by allocating tasks to be performed on separate threads. Once we offload tasks of the chain onto other threads, our "main" thread (i.e. the one handling the creation or accepting of events onto a chain) doesn't need to wait anymore. The operation of the chain becomes asynchronous. 

This means that events can reach the consumer much faster than they could using a simpler serialized chain. 

## Some important comparisons
Asynchronous systems are inherently far more complicated than synchronous systems. For a moment, let's say we have a task that is simple, like removing the petals of a flower. If we do that one at a time vs. all at once, the results are quite different. Doing it one at a time is precise, while ripping them all off at once is often messy. If we had to audit the order the petals were removed, we wouldn't be able to do that. In fact, it might even be hard to determine how many petals were removed at all.

Now, let's think of something more challenging like going grocery shopping. Think of absolutely every single step you have to perform from the moment you decide "It's time to go grocery shopping" to the moment you have finished putting away all of the groceries you have purchased and brought home. 

If you are the only person shopping, it is easy peasy. There will be no traffic, no one to block you in the aisles, no one will have purchased anything you need (so it won't be out of stock), and there won't be a line at the cash register when you check out. This isn't reality. Shopping (especially on the weekend) can be challenging. In fact, if you think about it, I'd be willing to bet that there are all sorts of failures, remediation steps, plan Bs, etc. that you have to act upon while going through your shopping experience. 

The point is that it is fairly unstable, very complicated, and requires a considerable amount of effort to design, implement and care for. 

## Baby got Backpressure. 
Sorry. Had to. 
So, Flowables operate on the principles of backpressure. I've probably already defined this, but occasional repetition is a good thing. In push-based event/message systems, backpressure is the ability for the consuming entity to know what its limits are, and to communicate those limits to the producing entity in order to pause the flow of events so that it (the consumer) can catch up. 

There is an actual default pattern to how events are paused. 
The Flowable's factory creates N events, and then pushes X of them up to the consumer. This means that we are queueing N - X events. 

Why? This pattern is a "think ahead". If we generated the same number of events in the Flowable as were being processed by the consumer, there would likely be some idle period between each request from the consumer to send the next batch of events. 

The queued N - X events ensure that the consumer always has work to do. 

For several years, N and X have been constant numbers. However, I have read in several texts that these may be adjusted by the developers of RxJava for optimization purposes. 

### Backpressure Strategy (as an argument) 
Luckily this isn't going to be a long-winded set of paragraphs on how to build your own backpressure strategy. Rx
provides 5 implementations for you that you can provide as arguments when trying to set up your own Flowables, or as an argument to the toFlowable() operator when converting from Observables to Flowables.

MISSING
* this effectively disables backpressure. The only time I have ever used this is in time-driven chains so that i can implement backpressure via operator later. (We'll get to that in a bit).

ERROR
* as soon as the Subscriber fails to keep up with the Flowable, a MissingBackpressureException is thrown

BUFFER
* events are queued up for consumer. 
* this is an unbounded queue, so it is possible to run out of memory if the Subscriber can't keep up. 

DROP
* this works similar to throttling. If the Subscriber becomes too busy to keep up with the Flowable, then it will
start dropping events/messages

LATEST
* This is similar to drop, but rather than dropping everything, it will keep the last event emitted while the Subscriber is busy.

### Backpressure Strategy (as an operator)
Backpressure strategy can also be accomplished via the onBackpressure operators. The most common use case is when you are using time-driven factories (i.e. interval()) to generate events. 

When we are using interval(), we are assuming that the rate of time passes consistently (notwithstanding any arguments from Hermann Minkowski). Since we treat time as a constant, we can't use backpressure to slow it down. 
Backpressure strategies still fail because they approach backpressure contextually from the source when events are generated. 

onBackpressure is treated like any other operator() in RxJava. It is a go-between. This means that we can have a time-driven source, with an operator that "inserts" a backpressure strategy after the source events have been generated. (In many cases, a Flowable will be implemented with the MISSING strategy in order to prevent any unwanted MissingBackpressureException errors before being proxied by one of the onBackpressure variants).

onBackpressureBuffer()
* This works like BackpressureStrategy.BUFFER (more or less the default impl of Backpressure)
* a capacity can be provided to avoid using an unbounded queue. This protects against out of memory errors. You must select the behavior when the capacity is reached. 

onBackPressureLatest()
* works just like Backpressure.LATEST

onBackPressureDrop()
* works just like BackpressureStrategy.DROP, however it also provides an overloaded implementation that supports an onDrop() method to handle what to do with dropped events. 

***

# Use Cases For Observables
* less than 1k events for life of observable subscription
* infrequent/intermittent events
* serialized/synchronized workflows (non-concurrent)
* UI Thread/proxy thread

## handling overwhelming producers
Use the flow control techniques introduced in the previous section (13) instead of Flowables

# Use Cases for Flowables.
* many events (as in many thousands of events)
  * asynchronous!
* (the most common use case) working with IO sources. 
  * most IO sources block while you are waiting for results. 
  * DBs, Network, Files are easy to stop/start because it often involves iteration. 
  
***

# Implementing Flowables
The great thing about Flowables is that we already know how to use them. Most(Note that I said MOST!) of the operators and factories supported for Observables are also supported on Flowables. 

I just think of them as Observables that have an underlying implementation of backpressure. 

Oh, and they don't use Observers...

## The Subscriber
Flowables use Subscribers in place of Observers. 
* Subscribers return a Subscription instead of Disposable
* Subscribers are disposed of by calling cancel(), instead of being cancelled by calling dispose(). :)
* Subscriptions use a request() method to tell the Flowable how many events it wants to receive. (this is overriding the default behavior discussed before)

## Backpressure From Source. 
There are ways to pass in arguments to set up backpressure, as well as operators that can "proxy" a source that doesn't support backpressure (or has it disabled).

Our first choice should be to try to provide a flowable that can introduce backpressure support upon event generation. 

### The easy way...
In general, one of the easiest factories for events (Observable or Flowable) is to provide an Iterable<T> to the fromIterable() factory. It so happens that this supports backpressure. For simple use cases that don't require getting into the weeds, this makes a lot of sense. 

### generate() 
generate() is more precise/flexible, but at the cost of a slightly more involved implementation. 
* it supports stateful and stateless emissions
* supports the ability to dispose of any state (clean up when the chain terminates.) 