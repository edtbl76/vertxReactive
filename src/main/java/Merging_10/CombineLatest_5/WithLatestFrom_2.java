package Merging_10.CombineLatest_5;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class WithLatestFrom_2 {

    public static void main(String[] args) {

        // Just some observables. We've got this part down ?
        Observable<Long> faster = Observable.interval(100, TimeUnit.MILLISECONDS);
        Observable<Long> slower = Observable.interval(500, TimeUnit.MILLISECONDS);

        /*
            This works slightly different than combineLatest().
            - combineLatest() is ambiguous in that it treats every emission equally.
            - withLatestFrom() picks ONE emission from each, but it favors THIS Observable.

            in this first example, we'll get 1 result for each of the 500ms emissions, and that means since
            the 100ms emissions fire faster, we'll skip all of the intermediary emissions.
         */
        slower.withLatestFrom(
                faster, (s, f) -> "500ms " + s + " 100ms " + f)
                .subscribe(System.out::println);
        Generic.wait(5);

        /*
            IN this second example, we'll get 1 result for each of the faster emissions, and the slower emissions
            will be repeated

                HOWEVER.. there is NO DOUBLE DIP. This means that at each 500ms mark, we only get a single emission. we
                won't get the double emission that occurs w/ the overlap.
         */
        faster.withLatestFrom(
                slower, (f, s) -> "100ms " + f + " 500ms " + s)
                .subscribe(System.out::println);
        Generic.wait(5);
    }
}
