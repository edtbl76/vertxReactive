package Operators_9.ActionOperators_6;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ActionDoFinally_7 {

    public static void main(String[] args) {

        /*
            This is doFinally capturing onComplete... from a Single, just to remind you that
            Observables aren't the only way to push events!
         */
        System.out.println("Example 1: doFinally() triggered by onComplete()");
        Single.just("one")
                .doFinally(() -> System.out.println("I'm fired off when onComplete() is called!"))
                .subscribe(
                        /*
                            Remember, the callback structure for a Single is different than an Observable.
                            There is an onSuccess and an onError.
                            This also demonstrates another interesting point...

                            onSuccess ACTUALLY calls onComplete.
                         */
                        s -> System.out.println("I'm a single: " + s),
                        Throwable::printStackTrace
                );



        /*
            This is doFinally capturing onError
         */
        System.out.println("\nExample 2: doFinally() triggered by onError()");
        Observable.just(1, 0)
                .map(integer -> 1 / integer)
                .doFinally(() -> System.out.println("I'm fired off when onError() is called"))
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        (throwable -> System.out.println("ERROR: " + throwable)),
                        () -> System.out.println("onError gets triggered, so I don't!")
                );


        /*
            This is the exact same example as DoOnDispose, but I replaced doOnDispose with doFinally().
         */
        System.out.println("\nExample 3: doFinally triggered by onDispose()");
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnSubscribe(
                        disposable -> System.out.println("I'm a Consumer<Disposable>, and I'm subscribing!"))
                .doFinally(() -> System.out.println("I'm fired off when dispose() is called!"))
                .subscribe(
                        second -> System.out.println("RCVD: " + second),
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE")
                );

        Generic.wait(2);
        d.dispose();
        Generic.wait(2);

    }
}
