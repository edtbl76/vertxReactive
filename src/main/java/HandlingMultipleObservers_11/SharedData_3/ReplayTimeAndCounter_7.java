package HandlingMultipleObservers_11.SharedData_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ReplayTimeAndCounter_7 {

    public static void main(String[] args) {

        /*
            But wait! There's more!

            You can "overload the overload" and shove another parameter into replay() that allows you to
            collect only N number of events over the last X units of time.

            This is a more specific use case, but it does exist in the wild.
         */
        Observable<Long> observable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l  -> (l + 1) * 200)
                .replay(2, 1, TimeUnit.SECONDS)
                .autoConnect();

        /*
            The output is similar to the last example:

            You'll see the first consumers events across the first second of time (10 events)
            Since we've only allowed our cache to store two events, the output looks like a "hiccup".

            It is going to reach back and replay the last 2 events pushed within the last 1 second.
            (This is the last 2 emissions, 1800 and 2000).

            Then we're back to our regularly scheduled programming (the output pattern of concurrent real-time
            streams that prints out to the console in an interleaved pattern).

         */
        observable.subscribe(l -> System.out.println("1: " + l));
        Generic.wait(2);
        observable.subscribe(l -> System.out.println("2: " + l));
        Generic.wait(2);
    }
}
