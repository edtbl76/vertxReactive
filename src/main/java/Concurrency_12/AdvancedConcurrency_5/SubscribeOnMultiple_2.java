package Concurrency_12.AdvancedConcurrency_5;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SubscribeOnMultiple_2 {

    public static void main(String[] args) {


        /*
            This is an observable with a "time taker" step that emulates ambiguous compute time as well as
            ambiguity in the results.
         */
        Observable<Integer> observable = Observable.range(1, 5)
                .subscribeOn(Schedulers.computation())
                .map(integer -> {
                    /*
                        Groundbreaking algorithm to do something amazing... or just something I typed randomly to
                        create processing time and new results.
                     */
                    int generic = Generic.getRandom(1, 2);
                    Generic.wait(generic);
                    return integer * (generic * Generic.getRandom(1, generic));
                });

        /*
            You may have noticed that most of the scheduler and concurrency examples only had a single observer.
            In those cases, we were just looking at syntax (as it pertains to a single thread).
            However, realistically, for these to be true examples of concurrency, we need to have multiple threads
            going.

            That means we need multiple Observers.
            - Each Observer below is going to represent work being done on a single thread.
            - The results should be displayed asynchronously (looks like round-robin/interleaving in the console)
            - The results also announce which thread they are working against.

            The thread output is pretty obvious. <SchedulerName>-<threadNumber>
         */
        observable.subscribe(integer ->
                System.out.println("[" + Thread.currentThread().getName() + "] - RCVD: " + integer)
        );
        observable.subscribe(integer ->
            System.out.println("[" + Thread.currentThread().getName() + "] - RCVD: " + integer)
        );

        /*
            Obligatory sleep.
         */
        Generic.wait(10);

    }
}
