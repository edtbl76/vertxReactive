package ColdAndHot;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ColdInterval_5 {

    public static void main(String[] args) {

        /*
            This is a contrived little demo that demonstrates how Observable.interval() is actually a cold observer

            - Observer 2 starts 5 seconds after the first Observer, however it starts counting at 0. This demonstrates
            that it is in fact using a finite data set and is COLD.

            If we want to force the events to be hot, we need to use a ConnectableObservable.
         */

        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        // Create my observers
        observable.subscribe(l -> System.out.println("Observer 1: [" + l + "]"));
        Generic.wait(5000);


        observable.subscribe(l -> System.out.println("Observer 2: [" + l + "]"));
        Generic.wait(5000);

    }
}
