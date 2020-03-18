# Concurrency
Before we start thinking about how concurrency works in RxJava, let's talk about concurrency in a more general context. 

## Concurrency Defined
I like to start conversations about concurrency with an analogy. Let's say that we are standing at the base of a staircase. We have a simple task to reach the top of the staircase using each of our legs as a resource. 

Our first case is to jump up each step with the either leg, but only that leg. Regardless which leg we choose, we are only using a single resource. We are only using a single resource to perform the task. While our body is "capable" of concurrency, this particular pattern isn't concurrent, because only a single resource is being used to perform the task. 

Our second case, is to climb the staircase "normally", by alternating each foot per step. For this examples sake, let's say the staircase has a total of 8 steps, numbered 1 through 8. Let's say that we take a step once per second (we're slow). The total time to climb the stairs is 8 seconds. If we start with our left leg, it will take odd steps, from 00:01 to 00:07. Our right leg will take the even steps from 00:02 to 00:08. These two time periods are overlapping, even though no work was actually done at the same time. This is the definition of concurrency. Multiple tasks being performed over overlapping time periods (resulting in the perception of them being completed at the same time.) In our case, the tasks are decomposed steps (literally) of the greater piece of work (to climb the stairs.) Each leg represents a resource capable of performing some work (this might be a thread, but it's a bit short-sighted to think of it just in this context, as concurrency can extend to physical resources, or even entire distributed systems depending on the scope concurrency is referring to.) Concurrency is a general term that has context at multiple levels of focus in software systems. 

A third case, is to jump up each step with both legs at the exact same time. Using the same metrics in our previous example, it will still take 8 seconds to get up the stairs, but both legs will be working at the same time. This is ALSO concurrency. However, we typically refer to this as a special type of concurrency called parallelism. Work is still being done in an overlapping time period, but it just so happens that we have distributed resources in a manner that the work can be done at the EXACT same instance in time. 

There are two notes about this third case:
1. concurrency and parallelism are often confused. I hope this provides a way to easily differentiate them. 
2. This is actually not quite a "real world" example. It's usually more expensive to achieve true parallelism, but the reward for that added expense is being able to perform more work over the same amount of time, or the same work in a shorter amount of time. (In some cases you might even get more work in a shorter amount of time.) 

Another note on concurrency and parallelism:
You are going to encounter a lot of texts on these subjects that use both terms ambiguously. Parallelism is more commonly defined as multiple events/work/foo that is happening at the same precise instant in time. Every implementation of reactive programming I've encountered (heavily weighted towards RxJava) supports this definition. 

However, the word itself does a fairly poor job of explaining the spirit of the pattern. If I have two continuous parallel lines, and I remove segments of each line so that no segment of one parallel line is parallel to any segment of the other parallel line, we still describe the two lines as parallel non-continuous lines. This is a loophole in the definition, as well as the mathematical principles of time (allowing it to be infinitely subdivided). I leave it to you to decide what terms fit your scenarios best. I paraphrase Eric Evans when I say that "ubiquitous language" in design is essential for collaboration and communication of design. The most profound systems aren't just performant, they are capable of growth and maturation that are able to keep pace, maybe parallel if you will, with the entropy of innovation and technical revelation.


## Concurrency Justified - Scale
Doing work one task at time in series is typically the first step in the evolution of many things. If you're one of the crazy fools like me who alphabetized their CDs and DVDs, you probably remember pulling out CDs one at a time and moving them to a new sleeve in a CaseLogic book or on a shelf. 

Eventually, your collection probably got large enough that it became quite a time crunch to accomplish this. Optimizations were always going to be limited by the fact that only a single "executor" was capable of performing work. 

One particularly interesting optimization is getting multiple booklets, and spacing out disks so that we limit the "adjacent" runs of disks in the booklets or on the shelf. The reason this is interesting is that it is a throwback to basic data structure concepts. A single booklet with each disk stuffed next to each other makes insertions like an array, where we have to perform work on all elements that are displaced. Multiple booklets with disks separated by space supports insertion more like a linked list, where we can just shove a piece of data into the structure without having to do much work on the rest of the elements in the structure. 

I mention this for a reason. Concurrency is a powerful tool, however it doesn't completely supercede other optimizations. Good distributed systems require balance. There are going to be circumstances where scaling out is going to cost more than scaling up. Mature design approaches absolutely require thoughtfulness in every direction. 

However, back to our regularly scheduled discussion on concurrency, I had eventually migrated to several booklets with lots of empty space. This made it easier for multiple people to perform work. 

This is another important point about concurrency that is often overlooked. If I only have a single booklet, it doesn't matter if I have 30 people to perform work. Only 1 person (core, executor, thread) is capable of doing work, so the other N resources (people, cores, executors, threads) are going to wait around doing nothing, because we haven't enabled the work to be done by multiple parties. Decomposition of work into smaller units is a prerequisite for concurrency. Another approach to this, specifically for legacy codebases that might not be cost-effective to decompose due to older (less flexible) coding practices, is to fake it. Rather than decomposing existing units of work into smaller pieces, you can insert a superimposed structure/container that contains your old units of work, allowing them to be processed concurrently. This is hackish, typically slow, and best used as a last resort or stop gap for code that is destined for the great big garbage collector in the sky (or seven levels of hell if the code is particularly annoying). At a high level, this is what it means for an application to be coded to support concurrency. 

Once we have enabled work to be done by a separate pair of hands, all we need to implement concurrency is a separate pair of hands.

### Multithreading vs. Multiprocessing.
This is a rabbit hole, so I created another page entirely for it. Read at your own risk.

### Process vs. Threads - Vertical and Horizontal scaling.
A process is an instance of a program and its resources, subdivided by a set of threads which perform instructions defined by the program on those resources. Good design modern design processes suggest the "you have one job" approach for an instance of a program (or microservice). This allows us to classify a process as a higher level definition of work to be performed, of which the specific details are carried out by a thread. 

This means that if we want to increase the efficiency at which a job is performed, we need more threads (and/or more resources). This is scaling up/down, or "vertical" scaling. More often than not, this is about decreasing the amount of time it takes to perform the "job" performed by a process. 

If we want to increase the number of similar types of work to be performed, we need more instances of that process. If I have a single user performing work that performs relatively quickly, we might not be as concerned about this. However, let's say that we have one million users. If we only have a single instance, and our super-tuned job takes 1 microsecond, we can only support one job per second for that many users. If our user base grows to 1 billion, without making a change to our system, we'll only be able to perform jobs in batches of 1 million users at a time. This means at peak capacity, the users in the last spot of our queue will be waiting over 15 minutes for their job to complete.

It is very likely that the overall process of performing a transaction is very similar on a per-transaction basis. The high level elements are the same. Horizontal scaling (scaling out/in) is about taking units of work that are more or less the same and creating many instances of them in order to support a larger user base. Working from the previous example, if we adjusted our system by scaling out from 1 to 10 instances, we would improve the longest wait time from 15 minutes to under 2 minutes. This increases the work performed per unit of time by 8x. 

A real world example to compare against is Visa. Visa has over 1 billion cards worldwide with a reported capacity of 65,000 transactions per second. For the sake of simplicity and argument, let's assume that a transaction is performed by one all powerful "job". Mathematically, this means that if each transaction takes 1 nanosecond to perform (highly unlikely), it would take 65,000 instances of our magic job to hit that benchmark. 

While vertical scaling is important to a balanced system, horizontal scale improves performance by orders of magnitude. 

## Implementation - You thought it was easy?
Superficially, the implementation of concurrency appears easy. 

### Thread Safety
Up until this line, we've defined concurrency as a bunch of things happening at the same time without interacting with each other. This is helpful for understanding the concepts, but it isn't realistic in real world systems. Objects are going to bounce around systems from service to service, process to process and thread to thread. 

Organizing optical disks make a great example. Short of smashing them together and destroying them or scratching up the surface, there isn't a way to alter the data on the disks. They are immutable. No matter what order they are received in, the data can never be changed. This means that the data is "thread-safe". Regardless of what order the threads access the data, the result is the same. 

There are four basic ways to ensure thread safety 
* isolate the data - You can't break what you don't share. 
* immutability - we discussed this already. You can't break what you can't change.
* farm it out - use thread-safe data structures that manage thread safety for you
* coordinate - this basically means that you manage thread safety yourself by restricting access to data based on time. (synchronization, locking).

RxJava is door #3, but we'll get to that in a bit. 

### Concurrency and Coordination
As we mentioned, there needs to be balance in distributed systems. Coordination of threads can be seen as one point of intersection where we maintain that balance. 

One of the most important concepts about threads is that they are very costly to manage. The number of threads an individual computer can support is tied to CPU architecture. Given the constraints of cost and availability, most systems will eagerly allocate a pool of threads to be ready to support the work that needs to be done.

The taxi lane/station outside the departures terminal at an airport represents a thread pool waiting to do work. If an airplane has just landed, it is more likely that more people will require cabs than there are cabs in line available. As a result, there is going to be a line of people waiting at the taxi station. This is more or less a queue of work waiting to be assigned a thread. As cabs deliver people to their destinations, they come back to the taxi line and pick up another person. This means that threads can be reused. (Because it is very expensive to create a taxicab from scratch every single time a person comes out of the airport). 

## Schedulers
A scheduler is a computational machine responsible for the administration of tasks or jobs (a.k.a. "units of work"). In the world of Java, we refer to these units of work "Runnables". 

A scheduler can manage these runnables in a number of ways:
* it can assign a Runnable directly to a Thread, to be executed directly. While this is possible, it assumes that you are manually handling the concurrency with your own code. This is a lot of work, and you will need to maintain your own Thread management code.
* it can assign a Runnable to an Executor, which selects threads from a thread pool to handle the Runnable. The thread is still actually doing the work, but the executor acts as an abstraction layer that handles most of the Thread management for you. This streamlines implementation of concurrency by managing the thread pool and Runnable assignments for you. This has almost entirely superceded the use of concrete Thread assignments.
* it can assign a Runnable to an Event Loop. Event Loops are single threaded (and therefore thread-safe). I visualize this as a conveyor belt in a manufacturing plant or fulfillment center, where a product is assembled or packaged as it moves along the conveyor belt. No two pair of hands ever touch the same object at the same time. Event loops can be very effective if the events/messages being dispatched are asynchronous. This allows the systems that have some specific sequential requirements to leverage serialization or pipelining to process the events in an ordered fashion before allowing them to be assigned to threads for concurrent operations that may occur after the order-constrained requirements. 

***

# Multithreading vs. Multiprocessing. 
I'm going to do my best to explain this briefly without ignoring salient points. I know this might seem out of the way from a discussion on reactive programming, but I feel it is important to discuss the nature of multithreading and multiprocessing to ensure completeness of the subject matter. There are complexities and details pertaining to concurrency that can't be evaluated or discussed in totality without at least a cursory foundation of these concepts. 

## The basics - Multitasking.
In the beginning there was a single process. Threads weren't even a thing yet. Computational work was done one at a time by a single process. Then in the mid-19th century a dude named Luigi thought up multiprocessing. More accurately, he generalized the concept of multitasking which was for a "computer" to do more than one thing "at a time". 

We've later nudged that definition from "at a time" to "over a period of time", allowing for the generalizations we provide for concurrency. 

This is the only "multi" term I am fond of using, because it is the only one that was intended to be ambiguous. Multithreading, multiprocessing, et al. have become too overloaded to remain useful communications tools. I'm certain I'll be accused of being nuanced or pedantic, and I'm ok with that. I've been called worse!

## The Process
A process refers to single instance of a program that is being executed. I.e. we've loaded the code up and the CPU is being told to do something with it. 

In its simplest sense, a single-processing system performs the instructions one at a time. 

More accurately, the process is more than just an instance of a program. It is an instance of a program in addition to all of the resources requested by that program. 

## Multiprocessing - Parallel Processing 
This is where things begin to fall over. The definitions are ambiguously overloaded. A majority of the time, this is considered to be a system that provides for multiple processors so that a process can be executed on each. This is parallel processing. 

However, sometimes multiprocessing is defined as being synonymous with multitasking, suggesting that we are time-sharing between two processes on the same processor core. This is concurrency, specifically based on time. 

## Thread.
The concept of "threads of control" or "threads of execution" is quite broad. For the purposes of this discussion, I'm going to define it by contrasting it to a process. 
* a thread is "usually" a subdivision of a process. 
* a thread "usually" inherits the resources associated with its "parent" process

A process is typically measured in terms of "resources", where as the thread is measured in terms of "execution". When we first load up a program, it is probably fairly bare, so some of the first tasks executed by threads might be to eagerly load resources that the program will have for the duration of its life (in fact, some of this is baked into the bootstrapping process). Over time, the management of resources will be performed by threads as part of the operation of the process. Sometimes, those resources might very well be another process, defined as inter-process communication.

These instructions are forms of overhead that help keep the process healthy and performant when executing more important tasks - solving the business problems. 

## Multithreading 
Another "multi" term I'm not terribly fond of. This term tends to follow the opposite of its big brother (multiprocessing). Multithreading is usually considered synonymous with concurrency. However, many texts concerned with hardware or embedded systems development overload this term with TLP/ILP (Task Level Parallelism/Instruction Level Parallelism), which are more specific forms of parallel computing.

Hopefully, by now I've belabored these concepts enough that you can intuitively deduce that multithreading (concurrency!!!) is the ability for multiple threads, within a process to perform work during overlapping intervals of time. 

***

# RxJava Concurrency

## Schedulers
Rx defines several types of Schedulers for different patterns of work. 

computation()
* intended for non-blocking computational work 
* well suited for event-loops, callbacks/closures, math, logic tasks
  * these tasks are "CPU intensive", meaning that when they are running, they tend to saturate the resources or use them in a manner where there isn't much free time to delegate to other work.
* backed by a pool of N threads, where N = Runtime.availableProcessors()
  * This is a FIXED number of threads.
* GOTCHA: dispose() is important here, but not critical. 

This is the default operator for many operators/factories in RxJava. If your use case is NOT well suited for
the specifics of the computation() implementation, make sure to double-check the operator/factory for an overloaded version that takes a Scheduler as a parameter. 

Per Rx, if you are unsure which scheduler to use, this is the one to start with. 

from()
* wraps a Java Executor in an Ex Scheduler and delegates schedule() calls. 
* this is useful for integrating Rx code w/ non-Rx code

io()
* intended for IO specific work (generally, this is blocking work).
  * read/write to file systems, databases, network sockets. 
* will attempt to reuse threads
  * unlike computation tasks, which tend to hog up your resources, io spends a lot of time waiting for work to be done. As a result, it makes sense for you to OVERSUBSCRIBE the CPU with more threads than there are cores. 
  * threads are fanned in/out based on the number of tasks. 
  * tuning can become more challenging, because you have to understand how much time is spent idle, and how much of that idle time can be multitasked. 
* GOTCHA: This is very susceptible to leaky thread syndrome. Please dispose() of your workers. 


newThread()
* intended for blocking work in scenarios where resources (especially memory) might be incredibly scarce so you 
can't afford to hold a thread pool. 
* generates a concrete thread for each unit of work. (i.e. there is NO thread pool)
  * make sure this is what you are looking for, because this will create a new thread for each Observer and then
tear it down after the job is done. This is a lot of overhead happening within the critical code path.
* GOTCHA: This is very susceptible to leaky thread syndrome. Please dispose() of your workers. 

single()
* similar to newThread(), but this provides Scheduler backed by a single-thread
* typically reserved for serialization type tasks (i.e. single-threaded sequential work)
* intended for non-blocking work
* specific use cases:
  * event loop
  * delayed scheduling w/ Schedulers.from()
  * inter-thread pipelining.

trampoline()
* mostly used internally to RxJava
* worker threads place units of work into a queue, where one thread is responsible for executing them (one-by-one)
* This is very useful if you want to perform background tasks that need to be executed one at a time.
* GOTCHA: this work will not return execution to the main thread. (NOTE: RxJava does not provide a blocking-queuing scheduler)

Trampoline is mostly used to prevent an antipattern of recursive scheduling on the same thread (which can cause stack overflow errors). The scheduler is intelligent enough to know that scheduling a task while on the same thread is "bad", so it waits for the running task to complete before scheduling.

### Scheduler Management
If you follow Rx best practices, you shouldn't have to manage the schedulers. Most of the issues that requires these types of actions are legacy java technologies that are no longer implemented in modern systems. However, if you are using schedulers that are subject to leaks, or you are interfacing with non-rx code in challenging ways, you may need to hedge your bets and leverage the life cycle management methods that have been exposed. 


shutdown()
* Every scheduler type supports a shutdown() method that exposes the underlying executor. This can be used to clean up resources (or ensure that it is done if you don't trust the framework). 
* You can also hit all Schedulers running within a JVM by calling the class method Schedulers.shutdown(). This is basically a cruise missile that kills all threads, prevents task allocation, and destroyes any and all pools. 

start()
* Every scheduler has its own start() method that allows task allocation/thread management to start after having been shutdown.
* There is a class method Schedulers.start() that will start ALL schedulers in the same manner that shutdown() stops them. 

## Operators and Concurrency
One of the advantages of RxJava is that the OFFICIAL operators (and factories) all support thread safety across different Observables. This means that we can safely use merge, reduce, zip (and so on...) to combine events being forwarded by different threads. 

This is extremely useful, especially if you are used to attempting this with callbacks. Since Observables don't care about the thread in which they are assigned, it makes it much easier to develop concurrent systems. 

***

# Keeping a Main Thread Alive
By now you've probably realized that using Thread.sleep() might not be the best way to achieve a continuously listening thread. 

# Option 1 - Make a Daemon
You could use "the oldest trick in the book" (which I think is literally true in this case), which is to have the main thread create a daemon thread, then have "that" daemon thread be the source thread for all concurrent threads. This is somewhat convoluted, and you still have to manage the relationship between the main thread and your daemon threads. This is actually how many older applications solved the problem. 

(NOTE: The main thread can't become a daemon thread. Threads must be marked as daemon before they are created. The main thread starts before there is an opportunity (or a context) from which to declare it as a daemon thread.

# Option 2 - Pass The Buck/Farm It Out (use a framework that manages non-daemon threads itself). 
Android developers, or Window-based UI developers are most likely going to be working with frameworks that exhibit this functionality. 

In all seriousness, this is a great way to solve the problem. If you trust the framework to handle this for you, then by all means go that route. The less code you have to write to solve business problems, the better. However, any time you farm something out, you have to consider the risks. 

# Option 3 - Use Thread.sleep() anyway, but make it really really long. 
Thread.sleep() takes a long as an argument, which means it supports a max value of 2^63 + 1. That is just shy of 300 million years. If that timer expires naturally, congratulations. 

This is an interesting case, because it might be a completely valid solution. Let's say you have a service that is absolutely ever only going to run for N interval of time. If that is the case, then the argument provided to sleep() can be N + X where X is some duration of time that allows work expected to be performed in N time and to exit gracefully. X must be greater than 0, but it doesn't necessary have to be the same unit of time. 

That being said, that particular use case is incredibly rare. (I've never encountered it, and I've only seen it in textbooks.) Most of the time, a service only needs to be up when it is performing work, so a sleep() call that keeps it up indefinitely isn't ideal. 

# Option 4 - CountDownLatch()
This is a "newer" method of managing concurrent threads. This is more intelligent than its predecessors because it waits just long enough for the worker threads to finish their work. 

More specifically, CountDownLatch() causes the calling thread to block() until a counter that represents the number of threads it is managing reaches 0. At this point, it considers the job "done", and the calling thread ends.

* If you compare this to the daemon thread, this essentially solves the problem of needing two threads to manage concurrency, as main thread can be the calling thread when using CountDownLatch(). 
* This also provides a better solution than Thread.sleep(), because more often than not, the time a service needs to be up is ambiguous. 

If I wasn't using Rx, this would be the best option. 

# Option 5 - But we ARE Using Rx!!!!
Rx provides blocking operators that have the ability to stop the calling thread and wait for events to be fired off. Please use these carefully. blockingOperators were initially intended for test cases. This is one of the few exceptions where it is considered "safe" to use them in production. 


***

# Advanced Concurrency - subscribeOn()
This isn't really "advanced" concurrency, but the other examples were designed to give you just a taste of what we can do, what schedulers are packaged inside the framework, and ways to sustain/manage the life cycle of the threads/schedulers. 

This section is a deeper dive into two operators that do most of the heavy lifting with RxJava concurrency.

## subscribeOn()
I introduced this briefly in the previous section's code examples. 

This method will asynchronously subscribe Observers to the specified Scheduler. I also think of this as allowing the user to provide a scheduler implementation to manage concurrency according to the business case of the system. (see the use cases of scheduler implementations above).

A thread is typically allocated from the thread pool instantiated by the provided Scheduler. (Rx uses java.util.concurrent.ScheduledExecutorService under the covers). Each thread is usually assigned to a single Observer, and then released back to the scheduler (for reuse) when onComplete() is called. 

This pattern allows multiple Observers to be executed in overlapping time periods (see what I did there?). 
The main thread of the program becomes a machine responsible for executing Observables. Each one is immediately fired off onto a thread allocated from the pool provided by the given Scheduler. Since the main thread is assigning these threads to units of work asynchronously, it doesn't wait for them to finish. 

### How it works...
The order in which events flow when subscribeOn() is called isn't as intuitive as you would think. 
* When subscribeOn() shows up in an Observable chain, the first thing it does is tell the Observable which Scheduler to use. 
* That Scheduler (and the threads created/managed by it) determines how events are processed throughout the pipeline.. EVEN FOR OPERATORS THAT OCCUR BEFORE HAND
  * while both will work (in most cases), Rx recommends placing subscribeOn() as close to the source as possible. 
  * If for some reason you accidentally have more than one subscribeOn() in a single observable chain, only one of them is going to work. (I'll give you two guesses which one, but I don't think you'll need the second guess....)

NOTE 1: Pay very very close attention to that last bullet point. At first thought, most of us are going to immediately vow never to create a single chain with two subscribeOn() operators. However, that's not usually how this happens. If we provide an API to another team with a subscribeOn() operator already attached to our Observables, then any subscribeOn() placed on calling code will be ignored. This is likely to frustrate, befuddle and confound developers trying to use your API. 
 
NOTE 2: there is an exception to the ordering rule, and that is when you are using multicasting operators. Multicasting operators should always be last anyway, but if for any reason they end up BEFORE the subscribeOn() call, then you aren't going to get the desired result. This is a great reason to keep your subscribeOn() calls 
as close to the source as possible. 


### Overloading Schedulers. 
Remember that some operators and factories have a default Scheduler. In these cases, subscribeOn() isn't going to
do anything. These methods are typically overloaded w/ a possible parameter that allows to specify a different Scheduler. 

When in doubt, refer to the Rx javadoc information.

### Another note on multicasting and concurrency. 
Multicasting is very useful tool, but it isn't concurrency. It does end up placing a ton of work on a single thread. That isn't necessarily a bad thing. There is such a thing as too much concurrency. If we aimlessly just create a horizontal grid of threads per all jobs we're going to end up with a giant pancake of jobs that can't be maintained, debugged, etc. 

One of the values of multicasting is that it allows us to group work onto threads for a number of reasons. We might be able to take tasks that are related, and delegate them to a single thread because they are lightweight operations that finish quickly or are executed sparsely. Conversely, we might have a reason to compact work down to a single thread for serialization purposes, sequencing, etc. 

Try to consider balance in the design. 


## unsubscribeOn()
When disposing of observables, we have to consider that this involves the teardown of resources that were required to instantiate the factory. Some of these resources involve setting up threads, database connections, network sockets, and so on. 

Waiting on the disposal of heavier resources is likely to block the thread that called dispose(). The unsubscribeOn() operator is used to move observables that have been marked for disposal to a new thread (usually something that manages a blocking workload like io()), allowing the calling thread to be freed up for other work. 

This is extremely important for very busy systems, or resource constrained systems that need to free up resources quickly to avoid resource starvation. 

### Changing disposal scheduler
Unlike subscribeOn(), unsubscribeOn() can be called multiple times in the same chain of events. unsubscribeOn() provides a scheduler to all of the events that are disposed of before it. If another unsubscribeOn() operator is in the chain, then any events that exist after the first unsubscribeOn() call will use the next unsubscribeOn() call.

***

# observeOn()

## Reviewing subscribeOn()
This is a concurrency operator that determines the Scheduler/thread model to be used from the source Observable all the way to the final consumer/Observer. 

## observeOn()
ObserveOn is like a switch on a train track. It's entire reason for being is to catch events on a given thread and then move them to a new thread model (by swapping the Scheduler in use). 

It is intuitive to assume that order DOES matter to observeOn(), because it is diverting events to a new thread/thread model at the point in the chain it is placed. We can make some assumptions about this. 
* First of all, if we place observeOn() before subscribeOn(), then observeOn() is going to win, and subscribeOn() is ignored. 
* Second, if observeOn() immediately follows subscribeOn(), be careful. This ultimately has the effect of calling subscribeOn() with the scheduler provided to observeOn(). 

   EX: 
   
      subscribeOn(Schedulers.computation()).observeOn(Schedulers.io());
  
   is effectively the same thing as

      subscribeOn(Schedulers.io());

    but we are asking the running system to do more work managing threads. 


An important note about using observeOn() is that this is/can be an expensive operation. I have seen codebases littered with these calls before a single performance test has been run. This is an anti-pattern that needs to be eschewed. Premature optimization often causes far more problems than it solves. 

One of the advantages of observeOn() (as is the case with many of the features of Reactive Programming) is that they are easy to use. This makes it easier to add an observeOn() into a chain of Observables when performance testing or use cases dictate the need, rather than performing guess work that results in resource constraints at the context switching points. 

(You'll also note that the ease of use can be a disadvantage, as it makes it easier to haphazardly throw statements into your code without A.) understanding what they really do and B.) without thinking carefully about whether or not the use case necessitates implementing them.

### Back To the MainThread, McFly. 
There are a lot of patterns that can be derived or thought of based on threading models. One of the most common patterns is a proxy thread often associated with user interface development. (Sometimes called a UI Thread.) Many 

Generally speaking, the UI thread is a good example because it is customer-facing. The most critical aspect of any application is the point of ingress for the customer. 

Making customers wait is always frustrating, but there are limitations to compute per dollar we can deliver for customer experience. For a moment, let's ignore robust UI frameworks that offer multiple components, each of which could have their own proxy thread.

Many interfaces are single threaded. This means that if one part of the interface is updated, we can't do anything else until the computation is completed and the page updates to reflect the change. This isn't a very good user experience. While we accept the limits of computation time, there is no reason that the thread that sends/receives information being entered into the UI has to be the same thread performing the processing. 

If I click on one area of a UI that is going to require a minute to process results, I'd like to be able to use the other areas of the UI while that is happening. In order to accomplish this, we need to accept the event from that first click, and then move that job to another thread to perform the "minute of processing". By doing this, the UI thread is now able to accept other input or refresh events. While the job we clicked on still takes a minute to process, the rest of the UI is available for us to interact with, creating an illusion of responsiveness due to concurrency. 

### Danger, Will Robinson!
If I don't use subscribeOn() or observeOn() the reactive contract is in effect, and each event is passed one by one from source Observable, through each operator until it is consumed by the Observer. We're good with this. 

If we do use subscribeOn(), the source is going to block event N until event N - 1 has been consumed by the Observer. We should be good with this. (Remember, subscribeOn() just selects a thread model. A separate thread is only provided for each separate Observer, which means that each thread essentially has its own instance of the reactive contract. Events are passed one by one from source Observable through each operator until it is consumed by THAT Observer. This rings true for each Observer subscribing to the source Observable. This is concurrent, but each thread is still blocking its own chain of events. 

observeOn() changes the ball game. Once an event reaches the observeOn() operator, it is pushed forward immediately. This makes sense when you think about it, because we have changed thread model within the chain of events. 

This is actually a fairly common "word problem" in engineering interviews. It is one of many "producer/consumer" problems. 

This is like the made-to-order eggs line at a buffet or brunch. Customers are going to line up much faster than omelettes can be made. Since observeOn() fires events forward without waiting for the events ahead of it to be completed, we are firing events faster than we can consume them. 

observeOn() offers some built in enhancements for you by automatically queueing events until the Observer can consume them. Unfortunately, this is only a band-aid solution, because the observeOn(), by itself, can't fix the asymmetrical nature of the event chain. The queue will continue to grow until the system is starved for resources, and it either crashes, becomes irresponsive, etc. 

***

# Making it Parallel
I imagine a lot of folks reading this have been curious how we achieve parallelism without violating the tenets of the reactive contract. Technically we've already answered this going back to the very beginning of the lessons. 

We have multiple observers. We introduced this when discussing hotness/coldness of Observables. 

Later we dove further down by discussing a way to perform work on each of those multiple observables in varying methods of sharing time by having different threads perform different work. (We've even managed to combine them, move them back to the original thread, and other nifty manipulations)

## Revisiting flatMap(). 
flatMap() turns each event into its own Observable, and then merges them together. This pattern was designed with
the reactive contract in mind as well as reactive scheduling. As a result, if each event is assigned to a different thread, flatMap() treats them in the same manner it would if they were assigned to the same thread. 

A typical sequence of events is as follows:
* A normal Observable generates events through a factory
* this is attached to a flatMap() operator, which creates its own internal observable chain for each event.
  * subscribeOn() and/or observeOn() are used to allow each event to ensure that the events are going to request a thread from the appropriate thread pool
  * this will be followed by operators to complete the internal chain, which is reserved for the parallel processing.
  * the internal chain ends with the last "work" operator, which allows flatMap() to take each of the individual threads and merge them back into a reactive stream that adheres to the reactive contract. The flatMap() only allows one thread to "deboard" at a time in order to continue moving events down the thread to the final point of consumption. This helps guarantee the reactive contract. 
* the chain ends by being consumed (serially) by an Observer, none the wiser that the events were processed concurrently.

### Merging concurrent threads. 
As you might have noticed, the junction that occurs at the end of a flatMap() call consists of merging concurrent threads into a serialized stream. The challenge is that this can be a bottleneck on a system that has a considerable number of events (i.e. moving quickly) or many separate threads (high degree of concurrency). 

flatMap() handles this similar to the way that observeOn() works. If there is a thread that is already moving events downstream (towards the final Observer), then the other queued threads will pretty much "drop the kids off" at the thread already processing these tasks. If you compare this to the proxy/UI thread pattern I mentioned in the previous section, you'll note that this is very similar to what is happening with flatMap(). 

### Optimizing
Something that needs to be considered is the number of available threads. For computation work, where we are generally operating with a fixed number of threads, we have a predictable number of workers to process events. 

It might make sense to enumerate your pipeline with a given thread to observable ratio so that work can be distributed based on the resources at hand, creating a concurrency pipeline that uses fewer overall resources. 

I think it's fairly intuitive to kick off this approach with a 1:1 ratio of threads to processors. (I.e. you'll probably just query the system for the number of cores. After many thrilling days/weeks/months in front of jmeter, visualvm, et al., you'll probably be able to make some assertions about the performance of the system so that you can adjust towards a sweet spot ratio that works best for your use case. 

***

# Flow Control in Concurrency 
As mentioned previously, concurrency is a great tool for performing more work over less time. However, due to the nature of the way we are performing (or more accurately, allocating) tasks of work, it is possible to create undesirable side effects, where we are dispatching more work than can be performed. As the pile of work grows, it eventually tips over in the form of resource starvation, crashes etc. 

The following sections discuss countermeasures to manage these problems.

## Backpressure: Yes or No? 
The patterns used up until now involve "pushing" events through the chain. However, with backpressure management, we allow various adjustments to the pace/pattern of flow. 

The term is borrowed crappily from fluid dynamics or automotive engineering. I have no idea where it originated to be honest, but those are the contexts from which it is typically analogized. It is mentioned as some resistive force that opposes the normal flow of fluids. 

In our case, backpressure could generically be thought of as (consumer)recipient-driven flow control in push-based event systems. The normal flow of events is a push from the producer. In order to make this a proper analogy, the consumer has to be able to a.) know when to say "when" and b.) tell the producer to stop/slow down. Once the flow of events exceeds some relevant threshold, the consumer notifies it to slow down. 

Most texts are a bit ambiguous about the term, but I want to mention that backpressure either STOPS or SLOWS events from being perpetuated through the chain. This has a very clear impact on the type of solution being 
provided, and gives us a hint about whether or not backpressure is warranted in given scenarios:
* backpressure is a great solution for finite/data-driven workflows, where receipt isn't time sensitive.
* backpressure is NOT a viable solution for infinite/event-driven workflows that are time-sensitive (or at the very least time "constrained")

***

# Buffering
In RxJava, buffering is a form of batch processing that allows us to configure our tasks into batches of work, represented by collections, organized by size or intervals of time that are optimized based on the expected workload, the type of work, and the ability for the consumer to expedite results quickly enough to avoid poor responsiveness. (Or worse, crashing, resource starvation etc.)

## Fixed Vs. Time
Fixed buffers allow us to taylor buffering to a specific resource set. 

Intervallic buffers allow us to create buffers that push results based on intervals of time. This is a more realistic use of buffering.

## Buffer Manipulation
The buffers can be manipulated with the skip parameter which can create patterns of non-contiguous batches of elements or overlapping batches (where events are repeated/duplicated). The ability to create positive/negative offsets against a set of finite data is very powerful when considering data-driven workflows. 

There are a number of use cases where we might want to batch successive permutations, or compare every third value, etc. Using skip is a very simple way to introduce complex patterns of data for analysis without much effort. 

## Boundary CutOffs
The buffer() operator can actually take another Observable as an argument. The buffer() will continue to cache events from its own chain until the "argument" Observable pushes an event. 

The event pushed in the operator-provided observable becomes a trigger to tell buffer() to tie-up the batch, push out the data, and then start buffering anew. 

This is a powerful pattern because the boundary could also be triggered by some other form of signal that indicates that it is a good time to stop buffering and flush our data. 

***

# Windowing
Windowing is almost completely identical to buffering, however it involves batching up events into other observables as opposed to collections. 

The benefit is that this is a bit faster than working with collections. 
* collections have to be completed before they can be pushed further downstream, because the collection must be fully built before we have access to its length in order to determine when we have finished working with it. 
* observables can be worked with immediately after being created. We don't have to wait for all of the events to be present, because the nature of the observable provides a finalization method (onComplete) upon subscription. In other word

An easier way to describe this is to say that in collections, the consumer owns the task of determining where the 
data structure ends. In reactive programming, we are told when to stop via the onComplete() callback.

It supports all of the same features as buffer(), such as skipping and boundary cutoffs.

***

# Throttling
The details of buffer() and window() are very similar. Both of these operators focus on creating "edible" batches of work that are easier for consumers to work with. (Buffer focuses on collections, whereas window focuses on observable structures.) This mitigates bottlenecks when moving from a concurrency pattern into stages of serialization. 

One important point, is that buffer() and window() were designed to preserve data , or at least treat it with the intent that it SHOULD be preserved. 

Throttling is a way to discard events when we consider them "unwanted". Common use cases are excessive button clicks (impatient users), or even potentially a rogue service that has been misconfigured "accidentally" to attempt a DDoS against your API. 

## Implementations
There are a few different implementations of throttle() that dictate the terms concerning which event we'll allow through. 

By default, throttling is performed using Schedulers.computation(), but most of the operator implementations are overloaded to allow for a third argument to choose your own scheduler. 

### throttleFirst()/throttleLast()
These are ways to allow the first/last event that occurs within a given fixed interval of time. These are useful for data-driven workflows, workflows with (EXTREMELY)predictable schedules or when used against operations that are time-boxed in a way that makes a fixed sample intuitive.

However, neither of these methods are useful for event-driven workflows without the aforementioned constraints.

### throttleWithTimeout()
This is essentially a "fire when ready" pattern. The "timeout" is actually more of a period of acquiescence that must elapse before event perpetuation will resume. This is more useful for the scenarios described in the introduction above, but it comes with a cost. In order to actually push the event, we have to wait for the "coast is clear" time span. 

***

# SwitchMap
switchMap() has similarities to flatMap() and throttling operators. In fact, you could argue that the differences between switchMap() and flatMap() make it useful in similar scenarios as throttling.

switchMap() only emits events from the CURRENT (or LATEST) Observable. This means that once the NEXT Observable is received, the previous Observable is disposed, and any remaining events are discarded. 

as a flatMap() receives new Observables, it starts working on them immediately. However, what happens with the remaining events from the Observables it was already working on? Those events will be processed until the onComplete() is called. If flatMap() is distributing work to individual threads, we have to consider our use case. If we are receiving batches of work, that need to be completed in their entirety, then we need to ensure that our
concurrency patterns are optimized to get the most work done, in the least amount of time possible given our resources. 

However, in some cases, batches of work are successive and/or time-bound. In real-time video applications, stock tickers, text-based play-by-play updates on sports sites, and many other scenarios, we are more interested in the "here and now". In these cases, our business case might require us to drop the old work in favor of the new. This is how switchMap() operates. 

switchMap() only operates on a single Observable at any given time. If switchMap() receives an Observable while working on another unfinished observable, it will dispose (cancel) the unfinished Observable in order to start
processing the new Observable. This means throwing the baby out with the bath water. Whatever events were waiting to be processed are discarded. 

***

