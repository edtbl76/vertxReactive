package Merging_10.Zippy_4;

import io.reactivex.Single;

public class ZipWith_3 {

    public static void main(String[] args) {

        /*
            This is a very silly, simple way to use zipWith().

            I used Singles because I'm tired of creating Observables :)

            The lax approach is hopefully to demonstrate that the whole blahWith() operator approach is pervasive, and
            they all behave in the same manner given their specific type.
         */
        Single.just("Key")
                .zipWith(Single.just("Value"), (first, second) -> first + ": " + second)
                .subscribe(System.out::println);
    }
}
