package Merging_10.Ambiguous_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class AmbWith_2 {

    public static void main(String[] args) {

        /*
            Let's say that the first Observable represents a chain of events we want to emit regularly at 1 second
            intervals as a SAMPLE. I.e. we'll fire it off and get a 5 second slice of information. (maybe for tracing or
            debugging or something.)

            However, the second observable represents a special chain of events that fires faster due to peak hours or
            some other circumstance that dictates the need for more granularity.

            The third is just to provide proof that the standard period can win.
         */
        Observable<String> period =  Observable.interval(1, TimeUnit.SECONDS)
                .take(5) // this is funny
                .map(l -> "1s " + (l + 1));

        Observable<String> interruptus = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10) // This would normally be 50, but since we know it's going to win.. we'll cut it down
                         // for brevity
                .map(l -> "100ms " + ((l + 1) * 100));

        Observable<String> neverFires = Observable.interval(100, TimeUnit.SECONDS)
                .take(100)
                .map(l -> "dumb example. Never fires.");

        /*
            THIS Observable is going to be "lose" to THAT Observable.
         */
        period.ambWith(interruptus)
                .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("done"));
        Generic.wait(10);

        /*
            THIS Observable is going to WIN to THAT Observable.
         */
        period.ambWith(neverFires)
                .subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("done"));
        Generic.wait(10);



    }
}
