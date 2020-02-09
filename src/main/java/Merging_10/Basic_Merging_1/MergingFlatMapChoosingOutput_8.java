package Merging_10.Basic_Merging_1;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class MergingFlatMapChoosingOutput_8 {

    public static void main(String[] args) {

        /*
            Observable w/ some integers we're going to use in a similar manner as we did in
            Example 7

            I changed the intervals around, and shortened the overall length (a minute is a long damn time).

            However, the point is to demonstrate how we can make decisions inside a flatMap based on the
            elements that are being mapped.

                There is a 0 here that would screw us up. Maybe it's being read in by a rogue service. Maybe it is
                bad code. Maybe it is a just a bad joke. Nonetheless, the 0 is there.

                Yes, we could use filter().map() in this case to prevent a zero from getting in. However, the point is
                that flatMap can be used to prune or drain or whatever else you can think of.


         */
        Observable<Integer> intervals = Observable.just(3, 0, 5, 10, 15);
        intervals
                .flatMap(integer ->
                        (integer == 0) ? Observable.empty() : Observable.interval(integer, TimeUnit.SECONDS)
                        .map(i -> integer + "s interval: " + (integer + (integer * i)) + " seconds elapsed")
                ).subscribe(System.out::println);

        Generic.wait(30);
    }
}
