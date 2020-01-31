package Dispose_8;

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
        Disposable d3 = observable.subscribe(l -> System.out.println("Observer3: " + l));
        Disposable d4 = observable.subscribe(l -> System.out.println("Observer4: " + l));

        /*
            Batch the two together so that we can operate against both of them. These are convenience functions
            that allow centralized management of resources. Some designs may not require this, however this can also
            be a useful "panic button". (There are better ways to do this though).

            This is contrived, but the point is to demonstrate that you can add them as a batch or one-by-one.
         */
        disposables.addAll(d1, d2);
        disposables.add(d3);


        /*
            Sleep so we can have them do work.
         */
        Generic.wait(3);

        /*
            Dispose!
         */
        disposables.dispose();

        /*
            Add another disposable.... This isn't going to behave the way you think.
            This will be added by the disposable and run with the others, and it will be disposed at the same time.
         */
        disposables.add(d4);

        /*
            Sleep again to prove it's empty
         */
        System.out.println("All Done?");
        Generic.wait(3);
    }


}
