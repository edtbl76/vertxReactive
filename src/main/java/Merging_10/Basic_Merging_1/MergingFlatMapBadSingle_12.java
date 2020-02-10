package Merging_10.Basic_Merging_1;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MergingFlatMapBadSingle_12 {

    public static void main(String[] args) {

        /*
            This example is an empty Observable.

            In this case, flatMap can merge everything to an Observable, so we end up where we started, with an
            empty stream.
         */
        Observable.empty()
                .flatMapSingle(s -> {
                    System.out.println("Nothing Here");
                    return Single.just(s);
                })
                .subscribe(
                        s -> System.out.println("onSuccess(): " + s),
                        throwable -> System.out.println("ERROR: " + throwable),
                        () -> System.out.println("DONE")
                );

        /*
            In this example, I don't get a Maybe as a return, I get the single.. which results in an error.

            This is an important gotcha...
         */
        Maybe.empty()
                .flatMapSingle(s -> {
                    System.out.println("Nothing Here");
                    return Single.just(s);
                })
                .subscribe(
                        s -> System.out.println("onSuccess(): " + s),
                        throwable -> System.out.println("ERROR: " + throwable)
                );
    }
}
