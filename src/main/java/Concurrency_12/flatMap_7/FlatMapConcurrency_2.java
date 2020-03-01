package Concurrency_12.flatMap_7;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.time.LocalTime;

public class FlatMapConcurrency_2 {

    public static void main(String[] args) {

        /*
            This is a similar chain to the previous example.

            We start with a run-of-the-mill Observable using the range factory.

            The flatMap() operator is basically being used to take events from an Observable and manage the events
            concurrently.
                - we pass the event through to an "internal" source Observable that configures a scheduler to start
                assigning work asynchronously on separate threads.
                - this internal chain is bound to our work emulation operator.

                - flatMap() manages the events by merging the results back into a single Observable which is associated
                with the Observer we've written to print out the time and thread.

            The results vary as you run this.

         */
        Observable.range(1, 10)
                .flatMap(
                        integer -> Observable.just(integer)
                        .subscribeOn(Schedulers.computation())
                        .map(integer1 -> {
                            Generic.wait(Generic.getRandom(1, 3));
                            return integer1;
                        })
                )
                .subscribe(
                        integer ->
                                System.out.println("[" + Thread.currentThread().getName() + "][" + LocalTime.now()
                                        + "] - RCVD: " + integer)
                );

        Generic.wait(5);
    }
}
