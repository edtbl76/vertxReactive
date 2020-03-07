package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

public class FlowableGenerateStateful_6 {

    public static void main(String[] args) {

        /*
            This is just a fancy implementation of a Flowable.generate() that allows us to
            manipulate events to our liking. The callback sets up an initial value for our state, which is
            the first value provided in our BiFunction (the second being the emitter).

            With these two pieces of data we can manipulate state between emissions (very much like we
            did with reduce() or scan() or other operators).

            The difference is that we are performing the logic as a way to generate events, rather than
            taking an event and manipulating/processing the data as a step in our pipeline.
         */
        Flowable.generate(
                () -> new AtomicInteger(100 + 1), (state, emitter) -> {
                    int currentState = state.decrementAndGet();
                    emitter.onNext(currentState);

                    if (currentState == 0)
                        emitter.onComplete();
                })
                .subscribeOn(Schedulers.computation())
                .doOnNext(event -> System.out.println("Emitting: " + event))
        .observeOn(Schedulers.io())
        .subscribe(event -> {
            Generic.waitMillis(15);
            System.out.println("RCVD: " + event);
        });
        Generic.wait(10);
    }
}
