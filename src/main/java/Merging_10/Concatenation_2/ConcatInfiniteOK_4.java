package Merging_10.Concatenation_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class ConcatInfiniteOK_4 {

    public static void main(String[] args) {

        Observable<Long> finite = Observable.just(200L, 300L, 400L, 500L);
        Observable<Long> infinite = Observable.interval(1, TimeUnit.SECONDS);

        /*
           The only (significant) thing different about this example is that we changed the order of the chains we
           added.

           This provides us with a "slightly better" solution, as it guarantees that the infinite chain is last, such
           that it won't block any finite chains.

           However, this is a challenging solution, as it can easily be missed later in redesign/refactoring attempts,
           especially given that the consumers and producers are likely to exist in different classes.
         */
        Observable.concat(finite, infinite)
                .subscribe(
                        s -> System.out.print(s + "..."),
                        Throwable::printStackTrace,
                        System.out::println);


        Generic.wait(10);
    }
}
