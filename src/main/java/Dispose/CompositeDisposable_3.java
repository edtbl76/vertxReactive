package Dispose;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class CompositeDisposable_3 {

    private static final CompositeDisposable disposables = new CompositeDisposable();

    public static void main(String[] args) {

        /*
            Standard interval() observable factory
         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        /*
            "Subscribe and Capture" - This seems to be the industry standard for discussing how we acquire
            disposables, but it sounds more like a criminal offense. Nonetheless, I'm going with it.

            - I'm creating two different disposables here. The point is we have more than one.
         */
        Disposable d1 = observable.subscribe(l -> System.out.println("Observer1: " + l));
        Disposable d2 = observable.subscribe(l -> System.out.println("Observer2: " + l));


        /*
            Batch the two together so that we can operate against both of them.
         */
        disposables.addAll(d1, d2);


        /*
            Sleep so we can have them do work.
         */
        Generic.wait(3);

        /*
            Dispose!
         */
        disposables.dispose();

        /*
            Sleep again to prove it's empty
         */
        Generic.wait(3);
    }


}
