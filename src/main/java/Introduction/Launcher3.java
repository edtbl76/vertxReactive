package Introduction;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Launcher3 {
    public static void main(String[] args) {

        /*
            Observable/Emitter
            - Here we are generating events every 1 second.
         */
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        /*
            Observer isn't doing anything fancy
         */
        seconds.subscribe(System.out::println);

        /*
        NOTE:
            - I have to create a sleep command here so that the Observable has a chance to operate.

            WHY?
            - Observables emit on a COMPUTE thread (separate from the main thread of the application) .
            - The main thread isn't going to wait for the Observable.

            - The Thread.sleep() command is going to pause the main thread, which gives the Observable time to
            emit intervallic events.
            - Since we passed a value of 10 seconds, we'll get 10 emissions consumed by the observer.
            - Once the sleep() is over, the main thread terminates, taking the compute thread with it.

         */
        Generic.wait(10);

    }
}
