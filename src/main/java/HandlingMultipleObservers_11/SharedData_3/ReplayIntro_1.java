package HandlingMultipleObservers_11.SharedData_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ReplayIntro_1 {

    public static void main(String[] args) {

        /*
            This is an autoConnect() Hot Observable.
            We've inserted an empty replay() call (no params).
         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .replay()
                .autoConnect();

        /*
            Here are my consumers w/ the pattern
            - consume 1
            - 3- second delay/keepalive
            - consume 2
            - 3- second keepalive
         */
        observable.subscribe(l -> System.out.println("O1: " + l));
        Generic.wait(3);
        observable.subscribe(l -> System.out.println("O2: " + l));
        Generic.wait(3);

        /*
            OUTPUT NOTES:
            - if the wiki wasn't clear, the output will be.

            1.) the first subscriber is a one-person show for 3 seconds. So we'll see 3 events, all consumed by that
                subscriber

            2.) the second subscriber joins in, and immediately replays all of the events (as if it were a cold
                observable)

            3.) once the 2nd subscriber is caught up, both subscribers receive "real-time" events concurrently (i.e.
                multicast)
         */
    }
}
