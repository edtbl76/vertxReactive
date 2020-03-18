# Handling Multiple Observers at Scale

## Hot vs. Cold
Hot vs. Cold Observables are terminologies derived from handling multiple Observers. The default way to handle multiple observers, is to create a stream for each one. 

## Multicasting
ConnectableObservables provide a way to convert cold(or hot) Observables into hot Observables and then to push events to all active subscribers. The basic implementation might not produce the best performance in large-scale systems. 

This section is going to go into ways to leverage multicasting in order to adjust for problems of scale. 

***

# Multicasting
Multicasting is a form of "stream consolidation" where several streams are actually consolidated down to a single "proxy" Observer. (This happens on publish())

Connect() is a way to collect all of the subscribers and bind them to the consolidated stream. It's more or less a superimposition of the pub/sub concept we use via Observable factory/operator to subscribe(). 

(The naming convention kind of sucks though. we're using subscribe() at one level and publish() at another which muddles common usage of the terms a bit. Someone coming from other programming paradigms could easily be confused.) 

## ConnectableObservable
The ConnectableObservable<T> provides multicasting by publish()-ing events to a single reactive stream. Multiple Observers are able to connect() to this stream, allowing us to avoid the nuances of creating many separate streams. 

## Basic Multicasting vs. Cold Observables
Basic multicasting provides a basic improvement over the default case of handling Observables (which is to make them cold). 

If I have 15 clients, and I emit 5 events, each that takes 1 second to emit, my timeline of events will take a total of 75 seconds to emit. More importantly, the 15th client won't receive any data until 70 seconds (over a minute) has passed. That is a huge delay, and a very poor customer experience. 

By using ConnectableObservable and its flagship methods publish() and connect(), we provide two important improvements. 
* First of all, since each event is submitted concurrently, the total time equals (time_to_send_event * no_of_events), which is 5 seconds in this case. (The benefit is that this task is completed 70 seconds earlier, making the system available to perform tasks sooner. The end result is the perception of speed). 
* Secondly, all clients get data at the same time. Clients 1 through 15 each get the first event during the first second, etc. 
* Lastly, the characteristics/user experience becomes predictable/more uniform. 

## Multicasting and Operators
* The first major downside is that placing operators after the publish() method might split out the events into separate streams, bringing us back to square one. This also introduces an interesting data consistency issue that I go through in more detail in the code (examples 3 - 5)

The big takeaway for me is that publish() should be the last thing that happens. Yes, there are distributed use cases where you want to send different elements of data to multiple consumers (i.e. to provide parallel processing to long serialized chains/pipelines), but multicasting doesn't make much sense in those circumstances. You would be collapsing down to a single stream only to expand back out to a larger number of streams, without any control whatsoever of that fan in/fan out process. RxJava provides us a number of alternatives that are far more lightweight in terms of performance, and more robust in terms of allowing us control over the who, what, why, where and when in terms of filtering streams down to X, and back up to Y. 

## Scale
I'm sure by now, you've realized that the design/business goal of multicasting is related to scale. This technique helps consolidate common operations. If I have some data that needs to be consumed by N Observers, we can save a boatload of CPU, memory, and overhead associated with communications (disk, network sockets, whatevs), et al. just by doing the common work once instead of once per Observer. The end result is the same, but we've accomplished the same task with 1/Nth the resources. 

## The Gotcha
Not all workflows support hotness, which is a requirement for multicasting. If you don't particularly like your customers, then you can always risk firing connect() before any emissions are sent. This would emulate a cold process, but time introduces a fair amount of uncertainty. (All joking aside, this isn't THAT complicated to do, but the corner cases are harder to plan for. 

## Best Practices for Multicasting - "Fridge to Microwave"
Generically speaking, the pattern above (putting publish() at the end of a workflow) foreshadows the overall best
practices of managing Hot and Cold Observables. 

Keep everything in the refrigerator until you want to eat it. Then pop it in the Microwave. In other words, keep 
cold Observables COLD until you need to multicast... then call publish()

If you don't like that analogy then try it the old fashioned way. 
Sit down and figure out what operations you need to perform. Look for commonality between each pipeline. 

Here we can create a horizon or breakpoint. Before the breakpoint, we'll have operations that are the same for each pipeline, and most importantly -- yield the same data. After the breakpoint, we'll have the operations that vary for each pipeline. 

This is very similar to the process and mindset you'll use when working with data serialization techniques. We often like to put compensatable transactions before a "point of no return"/horizon/breakpoint and "non-failable transactions afterwards". 

While the context is different, the skill set is the same. 
(HINT: if you decompose the thought process even further, this is a fundamental aspect of software development...where we take big problems and break them down into smaller problems that are easier to solve, and often with a greater number of paths to take towards a solution.) 


## Don't Over Do It. 
Multicasting has a cost associated with it. Naturally, this isn't magic. Under the hood, Rx creates a proxy Observer to provide the perception of stream consolidation. This is more expensive than the natural process of a single Observer. 

Know your outputs. Are they one or many? 

If you only have one, then multicasting is unnecessary. If you have many, then the best way to implement multicasting is to find the point at which common operators split apart into different operations. The publish() call should be placed at the end of a chain that eliminates the redundancies, just before the horizon at which events are distributed out to consumers to differing operators to take advantage of the wonders of distributed computing.

***

# AutoConnect
Everything has to be automated today right? 9 characters (i.e. 'connect()') is asking too much? I'm just being facetious. The concept of automatically connecting can be useful, but dangerous. Very much like trying to manage a data-driven workflow with ConnectableObservables, using auto connect introduces the ever-so challenging aspect of time. 

The effect of time in these workflows is simple, but the impact can be massive. 
If a fire an emission before connect() is called, then all of the subscribers miss it. If that first emission is
some form of synchronizer, or it correlates/coordinates the data after it, it's likely that we are going to have a pretty bad day. This can result in data duplication, data loss, consistency problems, veracity and/or validation issues, and so on.

This is a convenience feature to cover use cases that are simple, suffer few corner cases, or don't have substantial data consistency requirements. If precision is a requirement, you are most likely better off handling
calls to connect() manually. 

## How it works. 
Using manual connect looks something like this:
* Define our ConnectableObservable, and use a factory to generate events
* establish subscribers
* call connect to attach/bind subscribers to our proxy observer. 

`   Example`

    ConnectableObservable<T> co = Observable.factory(get-the-thing).publish();
    co.subscribe(onNext());
    co.subscribe(onNext());
    co.connect();`

Using autoconnect works a bit differently:
* Define ConnectableObservable, and use a factory to generate events
* append an autoconnect(int num); call to the end of our ConnectableObservable declaration, where "num" is the number of subscribers to connect to. 
  - note that it defaults to 1 subscriber if you don't provide an argument.
* autoconnect() is going  to connect to the next 'num' subscribers we create.

`   Example`

    ConnectableObservable<T> co = Observable.factory(get-the-thing).publish().autoconnect(3);
    co.subscribe(onNext());
    co.subscribe(onNext());
    co.subscribe(onNext());

## How it doesn't work (i.e. downsides)
1. The first observation (pun intended) should be that you can't use this unless you know precisely how many observables you are going to have (or you can take them in reusable chunks, which invites more complexity than it 
is worth).

2. autoconnect() has a persistent connection to its source. 
Yes, even if we have a terminable event (onError, onComplete) 
Yes, even if we cancel/dispose of the events. 

These scenarios can, will and do result in missed emissions. 

# refCount() & share()
Remember case #2 above? This is solved using refCount(). 
This is similar to calling autoconnect(1), but it doesn't persist subscription(s) to the source if there are no
more Observers.
* The key concept is that if it doesn't persist, then it can reconnect to the observer if a subscription shows up.
* note, this is like hitting the reset button. Since the previous Observables were already disposed/terminated, a new subscription starts events from square 1 (or 0, if that's where you started from!) 

The use case for refCount provides flexibility if you want to create a grace period for connections to remain active during "quiet" periods. It's essentially the same concept in movie theaters and plays when you see "the doors close at X time". We'll allow you to be a little late to the party, but after X time, we can gracefully dispose of the resources. 

## share()
share is just an alias for publish().refCount()

***

# Shared Data
Looking back at multicasting, we can summarize that journey by suggesting that we are persisting data across multiple Observers. (I'm sure y'all can think of plenty of event/message stores that act as hybrid comms/persistence beasts of burden that behave similar to this.) 

## replay()
The easiest way to look at this is to consider a Hot observer that we want to give "cold" characteristics under
certain circumstances. 
* First, 1 or more events within a given scope are stored/persisted. 
* Second, when a new observer joins the fun, the persisted events are pushed to the newcomer. 
* Third, now that we're caught up, the newcomer receives the same events as the rest of club. 

For those of you who are familiar with consensus algorithms, you'll note how similar this is to some of Raft's configuration change mechanisms. I mention this to correlate the concepts and use cases. Replaying transaction logs is a common way to catch up services/resources responsible for managing writes. The details vary from solution to solution (and this particular example doesn't cover large amounts of "debt" to be replayed). 

### Downsides
There are two important points that we have to consider for replay()
1. The number and/or size of events that have to be cached for replay can result in memory pressure/starvation
2. The number and/or size of events that have to be cached for replay can result in latency for subscribers that have to replay events before they are caught up. 

These are mitigated by providing bufferSize args to the method, but this demonstrates a need to understand the use cases well enough to achieve the desired effect. 

It's also worth mentioning, that you probably aren't going to solve it on the first or even tenth try. This is one of those tunable circumstances that you'll probably start out with "ok" performance. You'll quickly get to "good" or "satisfactory" performance with some experience at runtime (hopefully before you get to production, but not always). It takes time, care, good instrumentation, and at least one person with a strong case of OCD to get pipelines like this to scream. 

### To Dispose or not To Dispose.
refCount() and autoConnect() both create different side effects when used with replay. Which one is best is determined by your specific use case. 

As you might recall, autoConnect() doesn't reset its connections to source observables. As a result, subscribers who connect after the chain has stopped emitting are going to miss all of the events. (This is also wasteful, 
because the connections remain active.) 
* This is a good choice for architectures that have no downtime or have static connections/listeners. 

refCount() on the other hand does reset it's connections once all of the observers are done chewing on the chain. Late-comers will restart the chain from the first event, similar to a cold observer. 
* refCount is an excellent way to leverage the benefits of multicasting in data-driven workflows. 

### Timers and Counters
replay() supports both time and count-based bufferSize arguments. The real difference here comes down to your use case. 

A count-based argument is always going to get the EXACT number of events you want, even if those last 5 events represent a time-span of 1 year of uptime. (hopefully not, because you just paid for a lot of wasted space.) 

You can also combine timers and counters to buffer only the last N number of events accumulated in the last X unit of time. This is a more specific use case, but it can and will happen. This is useful in scenarios where the delta between low and high usage is fairly large. 

For instance, if I decide to cap my cache based on the last 60 seconds of pushed events, this might be useful for 23 hours a day, but for whatever reason my service is extremely popular from 12-1, at which point the consumption is much higher. The number of events pushed in 60 seconds in that period of time is feasibly high enough to cause resource starvation. This isn't usually a case you design for intentionally. It's usually a case you end up at while tuning a system. 

Another way to say this is, I'm going to collect the last X units of time of data, provided it doesn't exceed N number of events. 

I like examples like this, because real-world development problems don't usually fit into boxes neatly. There are always going to be trade offs, and sacrifices that have to be made in order to deliver results based on an ever-changing hierarchy of priorities. Hopefully it goes without saying that those priorities favor the customer experience.

## Cache
One of the traits that is immediately apparent about replay is that it appears designed to be used with multicast observables that require some "seeding" or "overlap" of data based on given circumstances. (In other words, we're trying to remediate the loss of some/all data in a given workflow)

replay() allows tuning efforts to overcome resource management issues that could arise from large data sets. These tuning efforts likely require additional overhead to bake in to the logic. 

What if my use cases are never time-driven/infinite in nature? There are plenty of cases where data-driven workflows might want to use multicasting to take advantage of concurrency patterns, yet we have small data sets that aren't likely to cause resource management problems. If we use replay() in these cases, we're not using the best "under the hood" solutions to accomplish the task at hand. 

Without any further gilding the lily, this is what cache() was intended for. This provides a simpler, lightweight way to just "cache everything indefinitely" for use cases that don't require additional precision (and cost) associated with resource management. 

### Author's Note: 
"Best" is a relative term. There is absolutely nothing wrong with replay(). The overhead is more of an academic talking point. I personally have found no issues using replay() at scale. Unless ALL of your use cases fall into the use cases intended for cache(), I suggest using replay(). 

### Capacity
Cache does suffer from resizing latency under the hood, which can be alleviated by calling cacheWithInitialCapacity(). This will prebake a size into the process, front-loading the creation of storage


Keep in mind that cache() should ONLY be used if you can sustain the entire data set in memory. Check your corner cases carefully, and if all else fails, just use replay(). Realistically, the performance hit is negligible, especially when compared to the alternative case where you crash a system by using up all of its resources. 

***

# Subjects
These are described as "mutable variables of reactive programming" by its creator Erik Meijer. I feel like this would normally be a great spot for a Monty Python joke. 

You might remember that I referred to multicasting as creating a "proxy observer". Well, that's what a subject is. A subject is an ambiguous type of structure that is both an Observable and Observer simultaneously. 

Some authors describe them as an "event bus". Every author/expert on the subject I've studied has suggested that these come with a "handle with care" label. 

The common sentiment is that they tend to act almost like a "paradigm adapter" that allows imperative programming to interface with reactive programming. Use cases could be to allow the integration of older systems with newer ones during migration efforts, monolith to microservices, et al. 

## Simple Notes
Subject implements both the Observable and Observer interfaces (I wasn't lying, it is both at the same time!). The implication is that when you call onNext(), onError(), onComplete(), it pumps those events towards its own Observers.
* This is that "proxy" behavior, where it multicasts the results of all of its Observer-esque calls. 
* As soon as an Observable calls onComplete(), the game is over. All other Observables stop pushing.

## When They Are Useful
The most common use case is to "eagerly" subscribe to X number of Observable input sources and push out a single Observable output stream through consolidation, reduction, collection etc.
* HINT: infinite, event-driven (user-actions!) 

### Good Design!
In many cases, Observables and Observers are going to live in disparate classes due to Microservices/distributed systems best practices. 
* This is a major use case. Subjects also provide a way to bridge the gap between cohesive,but decoupled, components of a complex system.

### merge() replacement
merge() is a very useful operator when the Observable and Observer exist in the same module. However, as we established in the previous note, most modern systems are distributed. As a result, the Observable and Observer might live in different sections of code that require manipulation to be coordinated. The merge() operator isn't very well-suited to these tasks, because it requires that the Observables be established. 

More often than not, IoC is going to be used to inject dependencies based on client calls/user-defined operations. This reduces boilerplate by an order of N, where N is equal to all of the possible use cases your customer might instantiate Observables. 

As seen in the example, we can generify an observable in this regard, such that the factories and operators are passed or defined at runtime and they subscribe to a subject that could likewise be user-defined. 

We end up spend more time defining logic to be passed forward, and less time duplicating the tedious pieces. This enforces one of the principles of good development; more time solving problems, less time developing RSI due to excessive typing of the same junk over and over.  Make sure to "DRY" your code off ;)

(I could spend a long time discussing this part, because I think it is something that many junior and even senior engineers ignore when structuring code. The reactive programming paradigm, when understood, helps enforce generic software development best practices.) 

## When They Are Not
Ordering can be a challenge. It's very important to ensure that the Observer exists before we start firing events at it. This leads to my favorite quote about Reactive Programming from one of its experts:
> Reactive programming only maintains integrity when source Observables are derived from a well-defined and predictable source. (Thomas Nield)

Cancellation is non-existent. Subjects can not be disposed. 

Thread-safety is also an issue. 
* The methods in the Observer interface aren't thread-safe.
* This means "Danger, Will Robinson!". Any thread collisions due to the lack of safety violates the "reactive contract" (and probably results in a pretty bad day for customers). The solution to the problem is to cast it to a Serializable structure.


## Best Practices
1. Finite data sets (Data-driven workflows) should stay in the fridge. Multicasting is a much safer way to achieve hotness. 
* This is the simplest solution. You can probably avoid Subjects altogether.
2. If you are using Subjects as a way to interface with imperative code, cast it down to Observable or abstract it via an API. 
3. Abstraction you say? Wrap it up! This is probably the most viable solution. Do NOT expose Subjects. These work best as internal constructs. Events can be passed to a class or API that is eventually handles by the Subjects. 
* This doesn't have to be fancy or elaborate. Separating the Subject into an Adapter of is an over-obvious use of the design pattern and it can be implemented for option 2 or 3.

# PublishSubject
This is the "generic" or basic type of Subject. Events Observable, Events Observed. 

The pattern this creates is pretty simple. 
* Observer is created. 
* Events Happen. 
* New Observers don't get anything. 

# BehaviorSubject
If Publish is generic, then that means this one adds some color. 
* it replays the last pushed event to each new Observer. 
* Effectively this is like PublishSubject + replay(1).autoConnect() all smooshed together. 
* the caveat is that the once onComplete() is called, any subsequent Observers miss the emissions and behave as 
if it is empty(), which is consistent with a persistent connection between Observable and Observer. However, as we recall, replay() and cache() don't persist the connection, so the analogy to replay is effective, not literal.

This pattern is slightly more interesting. 
* Observer(s) is/are created, which become(s) something "like" a transaction log. ALL Events are consumed. 
* Events are consumed. 
* Any new observer gets the latest event(data) emitted. 

In the end, the first observer(s) is/are tracking everything that happened from source to final destination, but new observers are only interested in the end state. 

I'm not suggesting that this is a complete transaction manager, but it does share a lot of the same characteristics. If combined with other operators to enforce atomicity, it is quite possible to create one entirely using Rx mechanisms. 

# ReplaySubject
This is like a PublishSubject + cache()
* Yep. cache() is still kind of a bad idea if you aren't working with finite data sets. 
* Even if you are working with finite data sets, this forces careful consideration. You will only be able to scale services using this technique based on its access to enough resources to store the entire data set in memory. 

While I give it a hard time, it does have its uses...WITH FINITE DATA SETS.  

Another interesting use is that it replays events regardless of whether or not onComplete() is called. 
* If you recall, this means that the connection between the Observable and Observer is NOT persisted, resulting in
subsequent subscriptions to start from square one.

# AsyncSubject
AsyncSubjects are similar to a ReplaySubject, but rather than replaying the entire list of events, it replays the last event only. 
* they only push the last event they receive, followed by onComplete()
* nothing is consumed until onComplete() is called (therefore it is only useful in finite data sets). 

Another way to get here is Observable.takeLast(1).replay(1)
* This is undoubtedly a more reactive approach than using Subjects. 

# UnicastSubject
A unicast subject starts out like a silent collector, storing events until an Observer subscribes to it. Once the subscription happens the stored events are replayed and removed from the cache. At this point, the UnicastSubject effectively begins to behave like a PublishSubject (playing events one at a time in sequence per the Reactive contract). 

The one caveat to consider is that a UnicastSubject supports one and ONLY ONE Observer connection. This isn't some hidden rule in the code. This is intuitive when you consider the pattern. 
* The contents are released from cache when the first Observer subscribes, therefore subsequent Observers have nothing to release from cache. 
* There are loopholes in the code to work around this (i.e. using publish() to proxy the release of the cache out to multiple Observers. Some books/tutorials demonstrate this, but I'm not going to because it falls into the "don't be clever!" category of reactive programming.

This is the only Subject that allows you a way to clear the internal cache. However, if an Observer never subscribes to the cache, it suffers from the same limitations as the other Subjects do with respect to large (or infinite) data sets. 

That being said, if and only if the events don't represent critical data, I don't consider it being overly clever (in a bad way) to have a timer that will auto-observe to clear the cache, followed by a restart of the service to ensure it begins "counting" again. This pattern has many use cases. (Although, there are simpler, more reactive ways to go about it.) 




