package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;
import io.reactivex.Single;

public class MergingMerge_1 {

    public static void main(String[] args) {

        Observable<String> afcWest = Observable.just("Kansas City", "Las Vegas", "Los Angeles (Chargers)", "Denver");
        Observable<String> nfcWest = Observable.just("Seattle", "San Francisco", "Los Angeles (Rams)", "Arizona");

        /*
            This is a factory that creates an Observable from other Observables.
         */
        Observable.merge(afcWest, nfcWest)
                .subscribe(s -> System.out.println("CITY: " + s));

        /*
            This is stupid, but it demonstrates how singles can be turned into observables, creating an Observable.

            The entire point here is actually to demonstrate a concept....
            - For whatever reason, we might have individual streams that are pushing a Single. We may have many of these.
            - The easiest way to combine these to a single stream, is actually to use merge.

            This would be useful for validation or some other similar type of process required against all objects.
         */

        Single<String> conferenceOne = Single.just("NFC");
        Single<String> conferenceTwo = Single.just("AFC");

        Observable.merge(conferenceOne.toObservable(), conferenceTwo.toObservable())
                .subscribe(System.out::println);
    }
}
