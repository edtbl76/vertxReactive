package Concurrency_12.flatMap_7;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class FlatMapConcurrencyOptimized_3 {

    public static void main(String[] args) {

        /*
                This isn't a real threadScheduler. We're essentially doing the math ahead of creating our event chain.
         */
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        AtomicInteger threadScheduler = new AtomicInteger(0);

        /*
            This is similar to our previous example, however groupBy() is created a GroupedObservable. This is a trick I
            learned from another book that I stuffed away in one of my private repos for safekeeping.

            groupBy() is optimizing the previous pattern (which fires off observables per each emission).
            Here, we are providing a preconfigured number of observables to a flatMap() so that we maintain the
            thread-to-observable ratio.

            NOTE: this also changes the behavior of our "internal" chain. In the previous example, the first task of
            flatMap was to request a thread for each event using subscribeOn(), this isn't going to work in this case,
            because subscribeOn() reaches back to the source observable, which isn't the GroupedObservable, but
            rather our range() factory.

            observeOn() to the rescue! Since we are more or less, "manually" handling the initial thread mapping by
            doing a little math in advance, we can be more liberal with the scheduler. I used newThread() here, but
            single or io will work fine as well.

            The remainder of this example is the same..... However we're providing an optimization for resources.


         */
        Observable.range(1, 10)
                .groupBy(integer -> threadScheduler.incrementAndGet() % numberOfThreads)
                .flatMap(integerIntegerGroupedObservable ->
                    integerIntegerGroupedObservable.observeOn(Schedulers.newThread())
                            .map(integer -> {
                                Generic.wait(Generic.getRandom(1, 3));
                                return integer;
                            })
                ).subscribe(
                        integer ->
                                System.out.println("[" + Thread.currentThread().getName() + "][" + LocalTime.now()
                                        + "] - RCVD: " + integer)
        );

        Generic.wait(5);


    }
}
