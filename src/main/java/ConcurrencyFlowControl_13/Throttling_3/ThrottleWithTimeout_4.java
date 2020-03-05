package ConcurrencyFlowControl_13.Throttling_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ThrottleWithTimeout_4 {

    public static void main(String[] args) {

        /*

            throttleWithTimeout() is slightly more intelligent than the previous examples. In most event-driven
            workflows, the previous examples somewhat defeat the purpose of throttling because they aren't really
            capable of "listening" to bursts.

            Instead of time-slicing the entirety of the events, we can use time to find an "all-clear" sign when
            events aren't being pushed, so that we can start pushing again.

            This is far more useful when dealing with unpredictable, bursty, (annoyingly redundant) workloads.



         */
        Observable.concat(
                getMyObservable(100L, "One", 10),
                getMyObservable(600L, "Two", 5),
                getMyObservable(1500L, "Tre", 2))
                .throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
        Generic.wait(10);

    }

    private static Observable<String> getMyObservable(long period, String label, int take) {

        return Observable.interval(period, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * period)
                .map(l -> "Observable " + label + ": " + l)
                .take(take);
    }
}
