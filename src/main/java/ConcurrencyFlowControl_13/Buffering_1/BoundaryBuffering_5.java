package ConcurrencyFlowControl_13.Buffering_1;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class BoundaryBuffering_5 {

    public static void main(String[] args) {

        /*
            Create an infinite chain to use as a "trigger" for the buffer() operator
         */
        Observable<Long> sideChain = Observable.interval(1, TimeUnit.SECONDS);

        /*
            While we used a repeating 1 second interval, it doesn't matter what we use as the "trigger".
            Every time an event emits from the "sideChain" it becomes the cut-off point for the batch.

            This is considered the most flexible way to buffer data based on random (or seemingly random) variations
            of event patterns.
         */
        Observable.interval(250, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 250)
                .buffer(sideChain)
                .subscribe(System.out::println);

        Generic.wait(5);

    }
}
