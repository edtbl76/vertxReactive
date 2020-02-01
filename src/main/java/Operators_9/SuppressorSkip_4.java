package Operators_9;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class SuppressorSkip_4 {

    public static void main(String[] args) {

        /*
            skip() accepts N, where n is the number of events to skip before beginning to emit.
         */
        Observable.range(1, 100)
                .skip(99)
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("What isn't skipped makes you stronger!")
                );

        /*
            If the skip parameter is larger than the number of events, it will behave like
            Observable.isEmpty().
            (Only onComplete()) will be called.
         */
        Observable.range(1, 5)
                .skip(99)
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("Everything is skipped!")
                );

        /*
            NOTE: The results are inconsistent, and I didn't want to play with timing issues in a system designed to
            maximize concurrency :)

            This is a skip() configured for 20 microseconds. This will skip events for the given duration of time
            before it starts letting them through.
         */
        Observable.range(1, 1050)
                .skip(20, TimeUnit.MICROSECONDS)
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("Time's up!")
                );

        Generic.wait(5);
    }
}
