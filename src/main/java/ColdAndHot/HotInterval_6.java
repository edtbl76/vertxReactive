package ColdAndHot;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class HotInterval_6 {

    public static void main(String[] args) {

        /*
            This example demonstrates how to force a Observable.interval() to be emit hot events.
         */
        ConnectableObservable<Long> connectableObservable = Observable.interval(1, TimeUnit.SECONDS).publish();

        connectableObservable.subscribe(l -> System.out.println("Observer 1: [" + l + "]"));

        /*
            We have to call connect here so that the first Observer will start emitting immediately.
         */
        connectableObservable.connect();

        /*
            We set the timer here, so that the first observer will chew on events for 5 seconds before the next
            observer starts.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
            The second observer starts and begins chewing on the CURRENT events. The timer here is a keep alive
            to make sure that we have time to see the results.
         */
        connectableObservable.subscribe(l -> System.out.println("Observer 2: [" + l + "]"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
