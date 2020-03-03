package ConcurrencyFlowControl_13.Buffering_1;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class TimedBuffers_4 {

    public static void main(String[] args) {

        /*
            We've started with an intervallic emitter that generates an event every 200 milliseconds. The map()
            operator transforms the events so that the value being consumed represents "elapsed time".

            Our buffer() operator is going to batch up 1 second worth of intervals.

            Our keep alive persists the main thread for 5 seconds.

            The result will be 5 lists (1 per batch, or 1 per second).
            Each batch will contain 4 - 5 events, depending on the minutiae associated with the 5th element.
            (when you have 4 events, the 5th event was probably microseconds too late to qualify).

            The point though, is that you don't lose any events.
         */
        System.out.println("Basic Timed Buffer");
        Disposable basic = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        Generic.wait(5);
        basic.dispose();

        /*
            The next three examples mimic the same "skip" patterns from a finite/data-driven approach, however
            rather the patterns are related to intervals of time as opposed to specific data.
         */
        System.out.println("\nTimed Buffer with Time Skip equal to timespan");
        Disposable timeskipEqual = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .buffer(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);
        Generic.wait(5);
        timeskipEqual.dispose();

        // Greater than means we "skip" values
        System.out.println("\nTimed Buffer w/ timeskip > timespan");
        Disposable timeskipGreaterThan = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .buffer(1000, 1400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);
        Generic.wait(5);
        timeskipGreaterThan.dispose();

        // Less than means that we "roll" or overlap values
        System.out.println("\nTimed Buffer w/ timeskip < timespan");
        Disposable timeskipLessThan = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .buffer(1000, 400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);
        Generic.wait(5);
        timeskipLessThan.dispose();

        /*
            Providing a maximum buffer size results in a dual purpose "cutoff" for pushing out batches.
            1. - we'll get a batch at every buffer interval or
            2. - we'll get a batch at every "max buffer size value"
         */
        System.out.println("\nProviding Max Buffer Size");
        Disposable maxBufferSize = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .buffer(1,  TimeUnit.SECONDS, 4)
                .subscribe(System.out::println);
        Generic.wait(5);
        maxBufferSize.dispose();
    }
}
