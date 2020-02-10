package Merging_10.Ambiguous_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AmbIntro_1 {

    public static void main(String[] args) {

        /*
            We've created 2 infinite observables, capped them at 10 via take() and then
            mapped the result so we can identify them later.
         */
        Observable<String> winner =
                Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(l -> "100ms " + ((l + 1) * 100));

        Observable<String> loser =
                Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(10)
                .map(l -> "200ms " + ((l + 1) * 200));


        /*
            Pass an Iterable to the amb factory and subscribe to it.

            We've passed TWO observables to Observable.amb(), and let them race.

            Clearly, this was a fixed race, as the first one is NAMED winner :) However, the point is evident when
            you look at the output.
            Only the events from the FIRST Observable to fire are pushed.
         */
        Observable.amb(Arrays.asList(winner, loser))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("done.")
                );

        Generic.wait(10);


        /*
            Unlike the flavors of concat(), this isn't about sequence, but about time.
            If I reorder the Observable arguments in the list, the Observable named "winner" fires first, so it is the
            only one that is emitted.
         */
        Observable.amb(Arrays.asList(loser, winner))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("done.")
                );

        Generic.wait(10);
    }
}
