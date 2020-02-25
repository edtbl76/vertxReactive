package Concurrency_12.RxJavaConcurrency_3;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SubscribeOn_3 {

    public static void main(String[] args) {

        /*
            These are concurrent observables. Each one is going to get its own thread (separate from the main
            thread performing the work.)
         */
        createConcurrentObservable(1, "One");
        createConcurrentObservable(4, "Two");

        /*
            Since we allocated those Observables to new threads, they no longer have any context associated with
            the main thread. As far as it is concerned, its job is done. This means that we need a keep alive (or
            some other mechanism) to ensure that the main thread is around to receive the response from the
            Observables it launched.

            i.e. we're back to the interval() pattern!
         */
        Generic.wait(8);
    }

    private static void createConcurrentObservable(int start, String tag) {

        /*
            This is similar to the Observable template we built in the previous example.
            We feed it a "starting" number and a tag, and then it generates an observable that

                - counts out 3 numbers from the given starting number
                - it waits for a period of time between 1 and the counting number
                    (this is our faux-process that takes time)
                -   subscribe() does its thing.

            The difference here is that between the range() factory and map() operator we have inserted
            subscribeOn() which does two things
             1.) it allows each event to be assigned asynchronously
             2.) it allows you (the developer!) to provide a specific Scheduler implementation to manage concurrency
                according to your given use cases.
         */
        Observable.range(start, 3)
                .subscribeOn(Schedulers.computation())
                .map(integer -> {
                    Generic.wait(Generic.getRandom(1, integer));
                    return integer;
                })
                .subscribe(
                        integer -> System.out.println("Observer " + tag + ": " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("[" + tag + "] complete")
                );
    }
}
