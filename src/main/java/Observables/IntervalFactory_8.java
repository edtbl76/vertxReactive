package Observables;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class IntervalFactory_8 {
    public static void main(String[] args) {

        /*
            Intervals are slightly more tricky.
            - interval() actually emits INFINITELY at the provided interval.
            - it runs on a SEPARATE THREAD from the main() method, specifically on the COMPUTATION SCHEDULER.
            - main() doesn't wait for this thread, so we have to perform "some" action to ensure that it is allowed to
            fire
                - in this case we provided Thread.sleep() for 10 seconds.

            NOTE: in production, the application will likely be kept alive by non-daemon threads (i.e. web services,
            apps, blah etc.)

            NOTE2:
                - these are COLD OBSERVABLES.
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s + " Mississippi!"));


        Generic.wait(10000);

    }

}
