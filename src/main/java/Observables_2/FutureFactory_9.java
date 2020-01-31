package Observables_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureFactory_9 {

    public static void main(String[] args) {

        /*
            Creating a Future. Don't worry about this.
            NOTE: This is a Java Future, not a Vert.x Future.
         */
        Future<Integer> future =
                Executors.newSingleThreadExecutor()
                .submit(() -> {
                    Generic.wait(1);
                    return 10;
                });

        /*
            This converts a Future into an Observable, to be more flexibly manipulated with RxJava.
         */
        Observable.fromFuture(future)
                .map(Object::toString)
                .map(String::length)
                .subscribe(System.out::println);

    }
}
