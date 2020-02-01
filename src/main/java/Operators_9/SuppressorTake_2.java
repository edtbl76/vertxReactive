package Operators_9;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class SuppressorTake_2 {
    public static void main(String[] args) {

        System.out.println("Example 1: ");
        /*
            This takes the first X number of events and calls onComplete.
         */
        Observable.just("bacon", "lettuce", "tomato", "mayo", "rye")
                .take(3)
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));


        System.out.println("Example 2: ");
        /*
            If there are fewer than the number of events we've asked for, the operator gives you the number it has
            available and then calls onComplete()
         */
        Observable.just("thing1", "thing2")
                .take(3)
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));


        System.out.println("Example 3:");
        /*

         */
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .subscribe(
                        i -> System.out.println("RCVD: " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Time's Up!")
                );

        Generic.wait(5);


    }
}
