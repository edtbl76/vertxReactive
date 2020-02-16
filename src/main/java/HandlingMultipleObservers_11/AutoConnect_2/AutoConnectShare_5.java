package HandlingMultipleObservers_11.AutoConnect_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class AutoConnectShare_5 {

    public static void main(String[] args) {

        /*
           Ed, did you just copy the previous example and replace publish().refCount() with share() ?

           I sure did.

         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).share();

        // Just a bunch of observables.
        observable
                .take(5)
                .subscribe(l -> System.out.println("1: " + l));
        Generic.wait(3);

        observable
                .take(2)
                .subscribe(l -> System.out.println("2: " + l));
        Generic.wait(3);

        observable
                .subscribe(l -> System.out.println("3: " + l));
        Generic.wait(3);

    }
}
