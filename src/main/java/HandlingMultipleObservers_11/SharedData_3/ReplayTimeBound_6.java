package HandlingMultipleObservers_11.SharedData_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ReplayTimeBound_6 {

    public static void main(String[] args) {

        /*
            Here is an observable that is using a time-based argument to replay(). Rather than data driven,
            this is going to capture data for N amount of time to be replayed.

            This is more useful if we have variable data sizes, unknowns etc. (hey... maybe a NON-data driven workflow?)

            The map is just an accumulator.
         */
        Observable<Long> observable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .replay(1, TimeUnit.SECONDS)
                .autoConnect();

        /*
            Here are my Observers
            - the first one is going to run for 2 seconds. (10 events)
            - the cache stored 1 seconds worth of data (which should be the last 4 or 5 events, usually 4).
                - this is the only real difference from the previous use cases. Rather than populating the
                cache based on emissions, we're doing it based on time passed.
            - the new observer joins, chews up the contents of the cache
            - real-time concurrent consumption begins.


         */
        observable.subscribe(l -> System.out.println("[Observer 1]: " + l));
        Generic.wait(2);
        observable.subscribe(l -> System.out.println("[Observer 2]: " + l));
        Generic.wait(2);
    }
}
