package ConcurrencyFlowControl_13.Throttling_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ThrottleFirst_3 {

    public static void main(String[] args) {

        /*

            Welcome to throttleFirst()...
            The purpose is to only allow the FIRST event within the given FIXED sample of time.
            (This should be intuitive by now given the explanation from the previous example.)

         */
        Observable.concat(
                getMyObservable(100L, "One", 10),
                getMyObservable(600L, "Two", 5),
                getMyObservable(1500L, "Tre", 2))
                .throttleFirst(1, TimeUnit.SECONDS)
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
