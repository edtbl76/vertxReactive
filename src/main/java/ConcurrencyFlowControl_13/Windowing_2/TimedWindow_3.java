package ConcurrencyFlowControl_13.Windowing_2;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class TimedWindow_3 {

    public static void main(String[] args) {

        /*
            This is exactly the same as the buffer example, but I'm using window, and in order to
            return an Observable, I've inserted flatMapSingle() w/ a reduce statement so it's easy to
            see the batch of work strung together.
         */
        System.out.println("Basic Timed Window");
        Disposable basic = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .window(1, TimeUnit.SECONDS)
                .flatMapSingle(longObservable -> longObservable.reduce("",
                        (first, next) -> first + (first.equals("") ? "" : "...") + next))
                .subscribe(System.out::println);

        Generic.wait(5);
        basic.dispose();

        /*
            The next three examples mimic the same "skip" patterns from a finite/data-driven approach, however
            rather the patterns are related to intervals of time as opposed to specific data.
         */
        System.out.println("\nTimed Window with Time Skip equal to timespan");
        Disposable timeskipEqual = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .window(1000, 1000, TimeUnit.MILLISECONDS)
                .flatMapSingle(longObservable -> longObservable.reduce("",
                        (first, next) -> first + (first.equals("") ? "" : "...") + next))
                .subscribe(System.out::println);
        Generic.wait(5);
        timeskipEqual.dispose();

        // Greater than means we "skip" values
        System.out.println("\nTimed Window w/ timeskip > timespan");
        Disposable timeskipGreaterThan = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .window(1000, 1400, TimeUnit.MILLISECONDS)
                .flatMapSingle(longObservable -> longObservable.reduce("",
                        (first, next) -> first + (first.equals("") ? "" : "...") + next))
                .subscribe(System.out::println);
        Generic.wait(5);
        timeskipGreaterThan.dispose();

        // Less than means that we "roll" or overlap values
        System.out.println("\nTimed Window w/ timeskip < timespan");
        Disposable timeskipLessThan = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 200)
                .window(1000, 400, TimeUnit.MILLISECONDS)
                .flatMapSingle(longObservable -> longObservable.reduce("",
                        (first, next) -> first + (first.equals("") ? "" : "...") + next))
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
                .window(1,  TimeUnit.SECONDS, 4)
                .flatMapSingle(longObservable -> longObservable.reduce("",
                        (first, next) -> first + (first.equals("") ? "" : "...") + next))
                .subscribe(System.out::println);
        Generic.wait(5);
        maxBufferSize.dispose();
    }
}
