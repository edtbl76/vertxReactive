package ConcurrencyFlowControl_13.switchMap_4;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class flatMapReview_2 {

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
            3.) flatMap is doing most of the work here.
                - we are passing each event received from map() into an internal pipeline that uses
                some action operators to expose the behavior.

                    doOnNext() is printing a "Batch" Label that is created each time flatMap calls computation
                                and then iterates through ALL events from that Observable

                        Remember this.

                                As the "next" Observable is received, it's events are interleaved with the remaining
                                events from the previous batch.

                                This is where things get hairy. What if a new batch supercedes an old batch?
                                What if it isn't a "batch" at all? What if it is the term of a Raft log?
                                What if it is real time video? Heck, what if it is ANYTHING that is time sensitive?

                                flatMap() is great, but sometimes we don't want to allocate resources to finishing
                                some Observables that might be old hat...


         */
        Observable.interval(5, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .flatMap(l ->
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
