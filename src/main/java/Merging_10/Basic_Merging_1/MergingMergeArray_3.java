package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class MergingMergeArray_3 {

    public static void main(String[] args) {

        Observable<String> afcNorth = Observable.just("Pittsburgh", "Cleveland", "Baltimore", "Cincinnati");
        Observable<String> nfcNorth = Observable.just("Detroit", "Green Bay", "Chicago", "Minnesota");
        Observable<String> afcSouth = Observable.just("Tennessee", "Jacksonville", "Houston", "Indianapolis");
        Observable<String> nfcSouth = Observable.just("Carolina", "Tampa Bay", "Atlanta", "New Orleans");
        Observable<String> defunctCities = Observable.just("Oakland", "San Diego");

        /*
            When you need to merge more than 4 Reactive Streams together, you use mergeArray.
         */
        Observable.mergeArray(afcNorth, afcSouth, nfcNorth, nfcSouth, defunctCities)
                .subscribe(s -> System.out.println("CITY: " + s));

        /*
            You'll notice that Observable.mergeArray() is highlighted above. (its a type safety issue).
            There is another way to get around this.
         */
        List<Observable<String>> observables = Arrays.asList(afcNorth, afcSouth, nfcNorth, nfcSouth, defunctCities);
        Observable.merge(observables).subscribe(s -> System.out.println("Type Safe City: " + s));

        /*
            Is this non-reactive?
            Technically, Kinda, but not really.

            We are taking chains and stuffing them into other chains as opposed to
            ripping data out of the middle of a chain.

            Likewise, we are also performing a "logical grouping of streams" with a collection, so it fits.

            I would also argue that type safety makes the code more stable.

            I'm personally not a fan of any of the blahArray() factories specifically for this reason.
         */
    }
}
