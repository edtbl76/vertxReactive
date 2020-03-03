package ConcurrencyFlowControl_13.Buffering_1;

import io.reactivex.Observable;

public class SkipBuffer_3 {

    public static void main(String[] args) {

        /*
            skipping is like adding a fine tune "knob" to the count parameter.

            The initial parameter (count) tells us how many items should be buffered.
            The second parameter (skip) tells us how many items should be skipped before starting a new buffer.

            These two numbers are used irrespective of the other. As a result, the following example
            is the same as not using skip at all.

            If count == skip, then we'll create a new buffer for every batch.


         */
        System.out.println("count == skip");
        Observable.range(1, 40)
                .buffer(5, 5)
                .subscribe(System.out::println);


        /*
           This example shows count as less than skip.

           This has allowed me to create a sort of "clock"

           We buffer 60 events, but we skip 100, which has the effect of emitting 60 second intervals that start
           evenly on the hundreds mark.

           This is fairly contrived, but it could be useful if we wanted to force each list to start with a predictable
           number. It is also useful if we need to extract a subset of emissions to create a concurrency pattern.
         */
        System.out.println("\ncount < skip");
        Observable.range(0, 400)
                .buffer(60, 100)
                .subscribe(System.out::println);


        /*
            This example demonstrates a negative offset by using a skip argument that is less than the count. The
            result is overlapping batches.

            This could be useful for generating batches that are "previous, current" events, consecutive permutations
            etc.
        */
        System.out.println("\ncount > skip");
        Observable.range(1, 10)
                .buffer(5, 1)
                .subscribe(System.out::println);
    }
}
