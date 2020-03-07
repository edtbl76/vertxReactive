package Flowables_14.OnBackpressure_4;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class Drop_4 {

    public static void main(String[] args) {

        /*
            This is the easiest of all the operators. There is no buffer. If the Subscriber falls behind
            then the event being generated at that moment is canned.

            One of the nice features is that there is an onDrop() callback we can use to handle the drops.
            - retry? DLQ ? Log and Neg ?

            If you don't do anything with onDrop(), then this will behave almost like onBackpressureLatest(), but
            it will have 1 less event, because it doesn't have that buffer capturing the latest event.
         */
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop(l -> System.out.println("Falling Behind...dropping " + l))
                .observeOn(Schedulers.io())
                .subscribe(l -> {
                    Generic.waitMillis(3);
                    System.out.println(l);
                });
        Generic.wait(5);
    }
}
