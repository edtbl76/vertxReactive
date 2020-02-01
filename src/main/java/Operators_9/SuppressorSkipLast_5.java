package Operators_9;

import io.reactivex.Observable;

public class SuppressorSkipLast_5 {

    public static void main(String[] args) {

        /*
            This is the opposite of takeLast OR, it can be thought of as equal to

                take(E - N) where E = the number of events and N = the parameter given to skipLast.
         */
        Observable
                .just("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet")
                .skipLast(3)
                .subscribe(
                        s -> System.out.println("Color: "),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }
}
