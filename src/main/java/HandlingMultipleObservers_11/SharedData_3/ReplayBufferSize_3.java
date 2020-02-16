package HandlingMultipleObservers_11.SharedData_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ReplayBufferSize_3 {

    public static void main(String[] args) {

        /*
            Same as previous example... b
                but we've supplied a buffersize for caching events.
                and we've bumped the period just to make the output more readable
         */
        Observable<Long> observable = Observable.interval(250, TimeUnit.MILLISECONDS)
                .replay(5)
                .autoConnect();

        /*
            Same as previous, but I adjusted the timing to make the output/visualization more civilized.

            Observer 1 is going to push 16 events.
                - replay buffer stores the last 5.

                NOTE: nothing special is going on here. it behaves like a normal cache.

            Observer 2 comes along and immediately chew up the contents of the cache.

            Observer 1 and 2 now begin to concurrently consume events in real time

         */
        observable.subscribe(l -> System.out.println("1: " + l));
        Generic.wait(4);
        observable.subscribe(l -> System.out.println("2: " + l));
        Generic.wait(4);

    }
}
