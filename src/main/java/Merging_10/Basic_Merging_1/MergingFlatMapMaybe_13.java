package Merging_10.Basic_Merging_1;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MergingFlatMapMaybe_13 {

    public static void main(String[] args) {

        /*
            This example is an empty Observable.

            In this case, flatMap can merge everything to an Observable, so we end up where we started, with an
            empty stream.
         */
        Observable.empty()
                .flatMapMaybe(s -> {
                    System.out.println("Nothing Here");
                    return Maybe.just(s);
                })
                .subscribe(
                        s -> System.out.println("onSuccess(): " + s),
                        throwable -> System.out.println("ERROR: " + throwable),
                        () -> System.out.println("DONE")
                );

        /*
            While you can't do Maybe.flatMapMaybe(), it is possible to do the following>

            Where Observable (that somehow ends up being empty) is mapped to a Single is mapped to a Maybe.
            If this is confusing, that's ok. Just remember that flatMap() is powerful, but also very very very
            dangerous if you don't know what you are doing.

            When it doubt read the documentation... twice.

         */
        Observable.empty()
                .flatMapSingle(s -> {
                    System.out.println("Nothing Here");
                    return Single.just(s);
                })
                .flatMapMaybe(s -> {
                    System.out.println("Still Nothing Here");
                    return Maybe.just(s);
                })
                .subscribe(
                        s -> System.out.println("onSuccess(): " + s),
                        throwable -> System.out.println("ERROR: " + throwable),
                        () -> System.out.println("Still Done")
                );


        /*
            Here is an actual example of Maybe doing its job
         */
        Observable.just(1, 2, 3, 4, 5)
                /*
                    Remember this? This is how we can use a nice little ternary statement to do some minor logic inside
                    a flatMap.

                    This works just as well for Maybe as it did for Observable.

                    We'll get results for the first 3 numbers, but once it crosses 3, we go to empty.
                 */
                .flatMapMaybe(integer -> (integer > 3) ? Maybe.empty() : Maybe.just(integer * integer))
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println);

    }
}
