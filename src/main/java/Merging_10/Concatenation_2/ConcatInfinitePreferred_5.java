package Merging_10.Concatenation_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ConcatInfinitePreferred_5 {

    public static void main(String[] args) {

        /*

            This combines several separate concepts...
            - I'll probably eventually split this out, but for now I'm going to try to clarify this here.

            1.) I'm demonstrating the preferred way to handle infinite chains using concat()
                - this applies to a concat() that has only 1 infinite chain (like the previous 2 examples)
                - this applies to a concat() that has multiple infinite chains.

            2.) This also demonstrates how to convert an infinite chain into a finite data set using the take()
            operator
                - NOTE: this is functionally only. The initial chains use the interval() factory, so they are still
                executed by a computation scheduler away from the main thread.

            3.) Lastly, I ended up demonstrating the order/sequence disparity between merge and concat in favor of MERGE.
                - using interval in the way I've demonstrated is usually as a counter or accumulator.
                (This is fairly consistent for time-driven workflows)
                - merge() is going to present this information in real-time, the way we would expect.
                - concat() guarantees ordering, so it takes each chain in turn, but the numbers no longer reflect
                real-time measurement.

            The point here is a major reveal about usage of concat() vs merge()

            merge() usually favors TIME
            concat() usually favors DATA

         */

        /*
            Here we have 2 infinite observables with different intervals
         */
        Observable<Long> forever1 = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> forever2 = Observable.interval(2, TimeUnit.SECONDS);


        /*
            Here we are performing some modifications to convert an infinite into a finite by using take().
            - I separated this deliberately just to emphasize the conversion as well as to remind young whippersnappers
            to keep code abstract when it is plausible. In this manner, the observables above can still be used in
            other classes, but here we have a more specific implementation that will be used in a smaller set of
            use cases.
         */
        Observable<String> observable1 = forever1.take(3).map(l -> "1s " + (l + 1));
        Observable<String> observable2 = forever2.take(3).map(l -> "2s " + ((l + 1) * 2));

        /*
            I did the merge first, because it demonstrates how ordering isn't preserved (in favor of the example I've
            used.)
            In this case, order isn't preserved, but the overall concept of TIME is preserved, such that events are
            fired at the correct moment in time.
         */
        Observable.merge(observable1, observable2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("merge done.")
                );
        Generic.wait(10);


        /*
            This demonstrates how ordering is preserved, despite the fact that the second observable starts
            counting by 2s after the first observable is completed.
            - This might be the effect you want for some bizarre reason, but realistically it's more common to
            overlay different intervals (as we've seen in the merge examples before).

            While this is the PREFERRED way to handle infinite observables using concat(),
            my specific example is probably slightly confusing... because the end result is most likely less
            desirable than what could have been accomplished w/ merge (and without having to convert infinite chains into
            finite data sets).



         */
        Observable.concat(observable1, observable2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("concat done.")
                );


        /*
            YES, this is still required. Despite the fact that we have used take() to functionally convert an
            infinite -> finite, the logic of Observable.interval() still pushes the execution responsibility to a
            computation scheduler.
         */
        Generic.wait(10);
    }
}
