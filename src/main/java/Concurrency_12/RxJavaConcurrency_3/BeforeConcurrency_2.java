package Concurrency_12.RxJavaConcurrency_3;

import Utils.Generic;
import io.reactivex.Observable;

public class BeforeConcurrency_2 {

    public static void main(String[] args) {

        /*
            I've set up my little "task" that I can call over and over again.
            The problem here is that there is no concurrency happening.

            When I execute the first observable, it is going to take N amount of time, and then once onComplete() is
            called, the second observable is executed.

            There is a lot of dead space here where we are waiting for things to happen.

            This is slow and wasteful!
         */
        createMyObservable(1, "1");
        createMyObservable(4, "2");


    }

    private static void createMyObservable(int start, String thread)  {

        /*
            This is an observable that counts to 3.
            - The map is just a random 1 to 3 second delay to emulate time passing, and then we return the
            same result.
         */
        Observable.range(start, 3)
                .map(integer -> {
                    Generic.wait(Generic.getRandom(1, 3));
                    return integer;
                })
                .subscribe(
                        integer -> System.out.println(thread + ": " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println(thread + " done")
                );


    }

}
