package Merging_10.Basic_Merging_1;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class MergingFlatMapInfinite_7 {

    public static void main(String[] args) {

        /*
            This is an Observable that has 4 integers.

            The cool part of this is that these 4 integers can be used to spawn intervals for doing "a thing"
            In this case I've created 4 infinite observables that are merged together into one single stream that
            pools the results of whatever is happening every 1, 2, 5 and 10 seconds.

                NOTE: This is REDUNDANT. EX: at 10 seconds, all 4 Observables were fired, so merging them into
                a single stream has 4 similar results.


         */
        Observable<Integer> intervals = Observable.just(1, 2, 5, 10);

        intervals.flatMap(integer ->
                Observable.interval(integer, TimeUnit.SECONDS)
                        .map(i -> integer + "s interval: " + (integer + (integer * i)) + " seconds elapsed")
        ).subscribe(System.out::println);

        // 1 minute wait.
        Generic.wait(60);

    }
}
