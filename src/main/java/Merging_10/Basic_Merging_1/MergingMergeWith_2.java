package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;
import io.reactivex.Single;

public class MergingMergeWith_2 {

    public static void main(String[] args) {


        Observable<String> afcEast = Observable.just("New England", "Buffalo", "New York (Jets)", "Miami");
        Observable<String> nfcEast = Observable.just("Dallas", "Washington", "Philadelphia", "New York (Giants)");

        /*
            This is an operator that changes the manner in which the merge occurs.
         */
        afcEast.mergeWith(nfcEast)
                .subscribe(s -> System.out.println("CITY: " + s));

        /*
            This is still kind of stupid (as far as examples go), but the circumstances are even less likely

            NOTE: mergeWith() is also slightly smarter... you'll note that I don't have to use Single.toObservable().
         */

        Observable<String> conferenceOne = Observable.just("NFC");
        Single<String> conferenceTwo = Single.just("AFC");

        conferenceOne.mergeWith(conferenceTwo)
                .subscribe(System.out::println);

    }
}
