package Operators_9;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class SuppressorFilter_1 {

    public static void main(String[] args) {

        /*
            filter() is a Suppressing Operator that accepts a Predicate<T>.

            All events that evaluate to true given the conditions of the Predicate will be emitted to the next observer.
         */
        Observable.
                just("Seahawks", "49ers", "Cardinals", "Rams")
                .filter(getStringLength(5))
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));


        /*
            This example demonstrates the use case where none of the events fulfill the Predicate, so it
            behaves exactly like the Observable.isEmpty() factory. (i.e. onComplete() is still executed)
         */
        Observable.
                just("Seahawks", "49ers", "Cardinals", "Rams")
                .filter(getStringLength(50))
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));
    }


    private static Predicate<String> getStringLength(int value) {
        return s -> s.length() > value;
    }
}
