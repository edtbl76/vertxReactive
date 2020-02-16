package HandlingMultipleObservers_11.SharedData_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ReplayExpensive_2 {

    public static void main(String[] args) {

        /*
            Same concept as the previous example, but you'll note that the time unit has changed.
         */
        int counter = 0;
        Observable<Long> observable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .replay()
                .autoConnect();

        /*
            Same concept, but I've increased the delay...

            Do you see where I'm going with this?

            The pattern of output is going to be the same as it was before.

            However, instead of replaying 3 events, we are going to replay 10,000 events.

            - NOTE: This example uses a Long (8 bytes), so we are only caching 80k in 10 seconds.
                    Realistically, we are going to be probably moving objects (min size of 16 bytes)

                    The link below is to a fantastic article on how to measure java object'
                    - This is very useful for perf profiling and/or planning.
                    https://www.baeldung.com/java-size-of-object

            See the wiki for an explanation on the concerns here.


         */
        observable.subscribe(l -> System.out.println("1: " + l));
        Generic.wait(10);
        observable.subscribe(l -> System.out.println("2: " + l));
        Generic.wait(10);
    }
}
