package ConcurrencyFlowControl_13.Throttling_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ThrottleIntro_1 {

    public static void main(String[] args) {

        /*
            This is just a review.

            Remember that concat is basically a "merge" operator that guarantees ordering.
            This assures us that the first observable will generate 10 events (every 100 ms), the second
            will generate 5 events every 600 ms, and the last will generate 2 events ever 1.5 seconds)

            Since these are infinite, we allocate a separate thread using our sleep wrapper.
         */
        Observable.concat(
                getMyObservable(100L, "One", 10),
                getMyObservable(600L, "Two", 5),
                getMyObservable(1500L, "Tre", 2)
        ).subscribe(System.out::println);
        Generic.wait(10);

    }

    private static Observable<String> getMyObservable(long period, String label, int take) {

        return Observable.interval(period, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * period)
                .map(l -> "Observable " + label + ": " + l)
                .take(take);
    }
}
