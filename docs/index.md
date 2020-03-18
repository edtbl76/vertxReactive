# RxJava for Distributed Systems 

## The Reactive Contract
Events are passed sequentially, one at a time. 

## Being Reactive means
* lower memory usage
* robust concurrency
* disposability (i.e. cleaning up after yourself!!!!)

## Best Practices

* Observables (or their sibling types) START THE THING
* Operators DO SOMETHING TO THE THING
* Observer CONSUMES THE THING

This is it. You do NOT get points for cleverness. Simplicity and readability does a software team good.

## Things that you shouldn't do

* don't extract values out of the middle of the chain. This should be obvious. The reactive paradigm explicitly provides you a way to get values from a chain. Use it. 
* avoid using blocking processes inside a chain.
* Do not use imperative statements. These are essentially the anti-reactive paradigm.





