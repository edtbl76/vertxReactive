package Operators_9.Transforming_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Delay_5 {

    public static void main(String[] args) {
        Observable.just("Now", "wait", "just", "a", "few", "seconds!")
                .delay(2, TimeUnit.SECONDS)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE"));

        /*
            Delay works like interval(). You need to ensure that the main thread is going to remain open
            long enough for the computation scheduler to complete the task.
         */
        Generic.wait(5);
    }
}
