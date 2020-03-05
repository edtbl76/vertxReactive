package ConcurrencyFlowControl_13.switchMap_4;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class fromFlatMapToSwitchMap_3 {

    public static void main(String[] args) {


        /*
            This is similar to our intro material, but we've prepended our consumer with more reactive happiness.
            We start the same way, with a basic range factory.
         */

        Observable<Integer> source = Observable.range(1,10);

        /*
            Likewise, there is no change here.
         */
        Observable<Integer> computation = source.concatMap(
                integer -> Observable.just(integer).delay(Generic.getRandom(1, 2000), TimeUnit.MILLISECONDS)
        );

        /*
            We've only made a FEW changes here.
            1.) we are creating a separate observable that fires off an infinite stream of longs every five seconds.
                (No change)
            2.) the map() operator adjusts the stream from interval(), which starts from 0.
                (No Change)
            3.) WE ARE USING SWITCHMAP! WOOHOO!
                - we are passing each event received from map() into an internal pipeline that uses
                some action operators to expose the behavior.

                    doOnNext() is printing a "Batch" Label that is created each time switchMap calls computation
                                and then iterates through some events from that Observable

                        This is the meat of the lesson.

                    while we left the doOnComplete() operator, it's never going to get a chance to execute, because
                    switchMap cancels previous observables as soon as the next observable comes in.
                        (This is demonstrated via the doOnDispose() operator)

         */
        Observable.interval(5, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .switchMap(l ->
                        computation
                                .doOnNext(s -> System.out.print("Batch " + l + ": " + s))
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
