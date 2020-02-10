package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MergingFlatMapSingle_11 {

    public static void main(String[] args) {

        // Observable of strings. (Got tired of using just())
        Observable<String> singles = Observable.fromIterable(List.of("all", "the", "single", "ladies"));

        /*
            The print statements are to illustrate the relationships.

            1.) The first thing that happens in flatMapSingle is that we print out the element of the SOURCE observable
            as "just a string".

            2.) we return a Single<String> for each element.

            3.) however flatMap of any flavor has a last step to dynamically merge reactive elements into a single
            stream of events...

            4.) the map() call is getting an Observable<String> as an input (which is ultimately what we started with
            in this silly contrived example). We are using StringUtils to do the transformation.

            5.) I deliberately evaluated all three callbacks of the Observable as proof that the final structure is an
            Observable<String> and not a Single.

         */
        singles
                .flatMapSingle(s -> {
                    System.out.println("\nI'm not a single yet: " + s);
                    return Single.just(s);
                })
                .map(s -> StringUtils.abbreviate(s, 4))
                .subscribe(
                        s -> System.out.println("onNext(): " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE")
                );

    }
}
