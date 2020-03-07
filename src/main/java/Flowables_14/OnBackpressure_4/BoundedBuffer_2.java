package Flowables_14.OnBackpressure_4;

import Utils.Generic;
import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class BoundedBuffer_2 {

    public static void main(String[] args) {

        /*
            This is similar to the previous example, but we've used one of the overloaded impls of
            onBackpressureBuffer.

            - we've specified a capacity ( a small one so that your console should be lit up like a Christmas Tree
            when executing this.)
            - we've provided a lambda to print out a notification (but we'd probably use this for a log event in
            real life)
            - Lastly, we specify what behavior we want when we reach the cap.

            The default in the code here is DROP_LATEST, but I've commented out the other two strategies you can
            use just for easy toggling to view the behavior.

            DROP_LATEST is going to drop the latest events in the queue in favor of the old data.
                This means that your output is going to be contiguous. (If we have 10 events numbered 1 - 10, we'd
                chop off 10, then 9, then 8, etc.)

            ERROR results in a MissingBackpressureException

            DROP_OLDEST is the opposite of DROP_LATEST. We'll drop the oldest event in favor of newer data. This
            means that our output will have drop outs or gaps.

            These strategies ultimately depend on the use case of your application. If latest is greatest, use
            DROP_OLDEST, if retaining history is critical, use DROP_LATEST, and ensure that the events you are
            dropping can be replayed if necessary.


         */
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(
                        5,
                        () -> System.out.println("Warning... Queue Capacity Has Been Reached"),
//                        BackpressureOverflowStrategy.ERROR
//                        BackpressureOverflowStrategy.DROP_OLDEST
                        BackpressureOverflowStrategy.DROP_LATEST
                )
                .observeOn(Schedulers.io())
                .subscribe(l -> {
                    Generic.waitMillis(10);
                    System.out.println(l);
                });
        Generic.wait(4);
    }
}
