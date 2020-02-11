package Merging_10.CombineLatest_5;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class CombineLatestIntro_1 {

    public static void main(String[] args) {

        /*
            Two infinite observables emitting events at different rates.
         */
        Observable<Long> one = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> two = Observable.interval(2, TimeUnit.SECONDS);

        /*
            Here is combine latest printing out "whatever it has" each time it fires.

            NOTE: there is a double dip.
            - when SRC1 fires it's new event, it takes the latest event from SRC2 and vice versa.

            Technically, based on our intervals, they are firing at the same time every 2 seconds.
         */
        Observable.combineLatest(
                one, two, (first, second) -> "SRC1: " + first + " SRC2: " + second)
                .subscribe(System.out::println);

        Generic.wait(10);
    }
}
