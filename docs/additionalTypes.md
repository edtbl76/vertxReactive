# Single
This is a special type of Observable that emits 1, and only 1 event. It has a smaller subset of operators than
Observable. (The developers were kind enough to ensure that only the operators relevant to a single emission 
were included!) 

    Single Interface
    
        interface SingleObserver<T> {
            void onSubscribe(Disposable D);
            void onSuccess(T value);
            void onError(Throwable throwable);
        
        }

## onSuccess 
The onSuccess() callback is an amalgam of onNext and onComplete.

## Getting Back to an Observable. 
toObservable() is a way to get back to an Observable if necessary 

***

# Maybe
This is a single that supports empty(), i.e. a special type of Observable that allows 1 or 0 emissions.

    Maybe Interface
    
        interface MaybeObservable<T> {
            void onSubscribe(Disposable d);
            void onSuccess(T value);        <-- Replaces onNext()
            void onError(Throwable throwable);
            void onComplete();
        } 


## CallBacks
onSuccess() is only called when an event is present to be emitted. 

onComplete() is called whether there is an event or not. This is an important distinction from a Single. 

## Single vs. Maybe
A Single w/o an event results in onError(). 
A Maybe w/o an event results in onComplete()

A Single with an event results in onSuccess()
A Maybe with an event results in onSuccess() followed by onComplete(). 

*** 

# Completable
This is a special "Observable" that doesn't receive any emissions. 

The completable has one job - to determine whether or not an event pushed to it actually took place. If you think about this carefully, it is checking for termination of events (onError and onComplete)

 Completable Interface
    
    interface CompletableObserver<T> {
        void onSubscribe(Disposable d);
        void onComplete();
        void onError(Throwable throwable);
    }


... But are these the only ways that a chain can terminate? NO! Walk with me to the next page.