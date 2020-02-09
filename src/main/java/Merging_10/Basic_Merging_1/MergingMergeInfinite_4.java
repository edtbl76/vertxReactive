package Merging_10.Basic_Merging_1;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class MergingMergeInfinite_4 {

    public static void main(String[] args) {

        /*
            Create two Observables that emit at different intervals.
         */
        Observable<String> ms1000 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .map(l -> "Elapsed (1000 ms) " + l);

        Observable<String> ms425 = Observable.interval(425, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300)
                .map(l -> "Elapsed ( 425 ms) " + l);
        /*
            This is one of our better examples to this point.
            - Watching the output creates a better indication of what reactive streams are going to look like in the
            wild.
            - we will see the result of two basic accumulators operating in counterpoint.
         */
        Observable.merge(ms1000, ms425)
                .subscribe(System.out::println);

        Generic.wait(5);
    }
}
