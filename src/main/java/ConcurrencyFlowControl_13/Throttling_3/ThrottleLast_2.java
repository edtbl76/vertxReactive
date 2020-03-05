package ConcurrencyFlowControl_13.Throttling_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ThrottleLast_2 {

    public static void main(String[] args) {

        /*

            Welcome to throttleLast()...
            The purpose is to only allow the LAST event within the given FIXED sample of time.
            This is probably the most common use case for throttling, as it guarantees the "latest" (usually the
            most relevant) event.

            - This is often misunderstood. The sample size (or duration) is essentially a time-bound cache or buffer of
            events. We are only going to take the last one.

            - This means that larger sample sizes result in fewer emissions, and smaller sample sizes result in
            more emissions.

                EXAMPLE: Let's say that we have 60 seconds of events.

                If I select a sample size of 10 seconds, I'm going to allow 6 events.
                If I select a sample size of 1 second, I'm going to allow 60 events.
         */
        Observable.concat(
                getMyObservable(100L, "One", 10),
                getMyObservable(600L, "Two", 5),
                getMyObservable(1500L, "Tre", 2))
                .throttleLast(1, TimeUnit.SECONDS)
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
