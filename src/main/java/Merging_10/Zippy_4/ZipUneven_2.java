package Merging_10.Zippy_4;

import io.reactivex.Observable;

public class ZipUneven_2 {

    public static void main(String[] args) {

        /*
            Observable with uneven number of events
         */
        Observable<String> promOne = Observable.just("guy", "dude", "went stag");
        Observable<String> promTwo = Observable.just("gal", "dudette");

        /*
            zip is NOT intelligent. If you have an uneven number, one of the Observables is going to reach onComplete
            first, which will end the zip() Observable as well.

            Any remaining events in the "other" chain are dropped.
         */
        Observable.zip(promOne, promTwo, (male, female) -> male + " is dancing with " + female)
                .subscribe(System.out::println);
    }
}
