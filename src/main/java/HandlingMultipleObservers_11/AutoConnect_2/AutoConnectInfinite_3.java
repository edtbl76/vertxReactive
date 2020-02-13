package HandlingMultipleObservers_11.AutoConnect_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class AutoConnectInfinite_3 {

    public static void main(String[] args) {

        /*
            Here we are firing off a time-based chain of events. AutoConnect is set to the default, which is 1.

            This has the effect of guaranteeing that at least ONE Observer will get all the events, but the
            subsequent subscribers get no guarantees.

         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();


        /*
            Here we have 2 observers.
            - The observers are staggered, so the first one fires immediately... that's the one with the gold ticket
            guarantee.
            - The second one hit snooze too many times, so it showed up 5 seconds late, so it only gets leftovers.
            (i.e. no bacon, the eggs are runny, and more than likely all of the good Chobani flavors have been taken.)
         */
        observable.subscribe(integer -> System.out.println("1: " + integer));
        Generic.wait(5);
        observable.subscribe(integer -> System.out.println("2: " + integer));
        Generic.wait(5);


    }
}
