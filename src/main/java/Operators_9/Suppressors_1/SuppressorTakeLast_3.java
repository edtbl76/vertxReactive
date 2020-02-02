package Operators_9.Suppressors_1;

import io.reactivex.Observable;

public class SuppressorTakeLast_3 {

    public static void main(String[] args) {

        /*
            This works like take(), but it takes the LAST events from the stream.
            - This is going to be slower than take(), because it has to queue ALL of the emissions
            before it can logically deduce what the last 3 will be.
         */
        Observable
                .just("Beagle", "Boxer", "Staffordshire Terrier", "Coonhound", "Husky")
                .takeLast(3)
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("All set!")
                );
    }
}
