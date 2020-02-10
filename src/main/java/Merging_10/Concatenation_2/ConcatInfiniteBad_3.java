package Merging_10.Concatenation_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ConcatInfiniteBad_3 {

    public static void main(String[] args) {

        Observable<Long> finite = Observable.just(100L, 200L, 300L, 400L, 500L);
        Observable<Long> infinite = Observable.interval(1, TimeUnit.SECONDS);

        /*
            If you comment out my keep alive below and execute this it is going to foreshadow what the result is
            going to be.

            - the second Observable never fires, because the first observable can't reach onComplete.
            - As soon as the main thread dies, the computation thread goes with it, and the "added" chain is never
            touched.
         */
        Observable.concat(infinite, finite)
                .subscribe(
                        s -> System.out.print(s + "..."),
                        Throwable::printStackTrace,
                        System.out::println);


        Generic.wait(10);


    }
}
