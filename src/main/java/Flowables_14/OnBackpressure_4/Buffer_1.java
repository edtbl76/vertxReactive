package Flowables_14.OnBackpressure_4;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class Buffer_1 {

    public static void main(String[] args) {

        /*
            This is a basic implementation of onBackpressureBuffer().
            - remember that this uses an unbounded queue when the Subscriber falls behind the source.
            - If the Subscriber can't recover, it will eventually result in an Out Of Memory error as the queue
            saturates the available memory.
         */
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(l -> {
                    Generic.waitMillis(10);
                    System.out.println(l);
                });
        Generic.wait(4);
    }
}
