package ConcurrencyFlowControl_13.switchMap_4;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import javax.swing.plaf.synth.SynthCheckBoxUI;
import java.util.concurrent.TimeUnit;

public class concurrentBatchSwitchMap_4 {

    public static void main(String[] args) {


        /*
            We've added a new dimension to our example, but placing a subscribeOn to our range comand.
                - we're going to pretend this is our io()
         */

        Observable<Integer> source = Observable.range(1,10)
                .subscribeOn(Schedulers.io());

        /*
            Likewise, there is no change here.
         */
        Observable<Integer> computation = source.concatMap(
                integer -> Observable.just(integer).delay(Generic.getRandom(1, 2000), TimeUnit.MILLISECONDS)
        );

        /*

            The only adjustment in this example is using observeOn() to
            place each batch on a different thread.

         */
        Observable.interval(5, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .switchMap(l ->
                        computation
                                .observeOn(Schedulers.computation())
                                .doOnNext(
                                        s -> System.out.print(
                                                "[" + Thread.currentThread().getName() + "] Batch " + l + ": " + s))
                                .doOnDispose(() -> System.out.println("Disposing " + l))
                                .doOnComplete(() -> System.out.println("Batch " + l + " Completed!"))
                )
        .subscribe(
                integer -> System.out.println(),
                Throwable::printStackTrace,
                () -> System.out.println("Done")
        );

        Generic.wait(60);

    }
}
