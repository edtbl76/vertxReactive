package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;

public class FlowableGenerate_5 {

    public static void main(String[] args) {

        /*
            generate() is a very precise (and second best) method for creating a source flowable that
            can handle backpressure from source rather than through an operator.

            This has advantages over "backpressure strategy" by introducing backpressure at the source
            (i.e. not proxied).


         */
        Flowable.generate(emitter -> emitter.onNext(ThreadLocalRandom.current().nextInt(1,10_000)))
                .subscribeOn(Schedulers.computation())
                .doOnNext(event -> System.out.println("Emitting: " + event))
                .observeOn(Schedulers.io())
                .subscribe(event -> {
                    Generic.waitMillis(25);
                    System.out.println("[" + Thread.currentThread().getName() + "] - RCVD - " + event);
                });

        Generic.wait(4);
    }
}
