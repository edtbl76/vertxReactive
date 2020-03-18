# Transformers
These aren't the same as Transforming Operators. Transforming operators are methods for manipulating data in a reactive stream. Transformers are ways of creating patterns of operators to create a new operator. For example, if you find yourself using the same "general" pattern of a handful of operators in succession, doing the same things, it makes sense to simplify them down into a "super operator" that does all of that work for you. 

If you think about the kind of abstraction being provided here, we are starting to move away from the "guts" of a distributed system towards the user. Transformers aren't just making code more readable, they are hiding implementation details. If we name these methods descriptively, they'll properly describe "what" the transformer is doing. The first thing that comes to my mind is API design.

## compose()
The compose() method takes an ObservableTransformer/FlowableTransformer as an argument. Typically, we'll probably create a method that returns an ObservableTransformer/FlowableTransformer, such that the method performs the steps we'd like to repeat. 

## Managing State
Pay close attention to any state introduced in transformers. In most cases, you probably don't want to share state. If you don't, then review defer()/fromCallable() and hit the code examples to see the impls. 

## Don't forget the other Rx types
There are also SingleTransformers, MaybeTransformers and CompletableTransformers. 

SingleTransformer and MaybeTransformer work pretty much like Observable/Flowable versions, but naturally with the Observer overloads for those given Rx types. (i.e. Single uses onComplete and onError), Maybe has (onSuccess/onComplete and onError). 

CompletableTransformer is also similar, but it doesn't take type parameters.

***

# Converters. 
This is kind of a wishy-washy place to put these. There are a number of operators in RxJava prepended with the word "to" for converting between reactive object types. (i.e. toObservable(), toFlowable()). 

There are also some basic conversion operators that can be used to convert an Observable into some other data structure (i.e. toList(), toMap(), etc.) 

There are even a number of operators we've already used that do this (buffer()??). 

In most cases these converters aren't custom. They have a specific purpose. However, there is just a vanilla "unspecified" to() operator for supplying your own data structure (or some other third party data structure). 

## to()
TODO (no pun intended).

***

# Creating Your Own Operators
This is challenging and error prone. 

Checklist:
1.) Did you try to use compose() w/ an ObservableTransformer and FlowableTransformer? 
2.) Did you buy/borrow/read a book on RxJava (even the old ones)? 
3.) Did you Google it? 

If yes, and you still think you need to create your own operator:
* https://github.com/akarnokd/RxJavaExtensions
* https://github.com/davidmoten/rxjava2-extras

If you still need to make your own... here are tips that I have stolen from all of the above resources :)

## The Basic Concepts
With Transformers, we essentially created a pipeline of operators that we reused enough to create an abstraction. 
Custom operators are acting like an in-betweener operator. We are going to intercept events from an upstream source (acting like an Observer) and then emit those events downstream to our next-hop consumer. 

## Implementation
The first thing we have to do is create our operator, which is a function that returns either an ObservableOperator (or FlowableOperator) (The text here is focused on Observables. You can find/replace for Observable/Flowable, Observer/Subscriber in your head to apply these concepts to both structures).

NOTE: ObservableOperator<Downstream, Upstream> defines two types. For some reason I'm very stupid with concept of upstream and downstream. (I must have evolved from salmon).
* Downstream is towards the consumer
* Upstream is towards the producer. (Against the flow) 

Once we've created our Operator, we have to override its apply() method. (NOTE: this detail is abstracted when using lambdas/method expressions. As a result, I've provided two examples for comparison). 
* The apply()function is the "meat" of creating operators. 
  * The input is the "Observer" provided by the previous Observable/Flowable
  * the output is the 

In order to wire our custom operator into an event chain, we use the lift() operator. This operator takes our ObservableOperator<Downstream, Upstream> as an argument and returns an Observable. If you consider how operators are chained (Observable to Observer), the Observer returned by our custom operator is wired to be the "end" of the chain segment "sourced" from the Observable returned by lift(). This is how we inject our custom code into the chain. 

### EXAMPLES NOTE:
These examples are very very simple use cases. It is very easy to look at them and get a sense of false bravado towards building your own operators. Every concept we've discussed to date has to be considered in the implementation
* do we need to prevent shared state? How are we going to protect against it? 
* are we going to manage concurrency? 

Creating your own operators puts you in the driver's seat with respect to the preservation of the reactive contract. 
