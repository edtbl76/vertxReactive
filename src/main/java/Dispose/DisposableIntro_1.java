package Dispose;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class DisposableIntro_1 {

    public static void main(String[] args) {
        Observable<Long> longObservable = Observable.interval(1, TimeUnit.SECONDS);

        Disposable disposable = longObservable.subscribe(
                l -> System.out.println("RCVD: [" + l + "]"));

        // Sleep to allow interval to execute on COMPUTATION SCHEDULER>
        Generic.wait(5000);

        // Kill it!
        disposable.dispose();

        // Sleep to demonstrate that events have stopped.
        Generic.wait(5000);

    }
}
