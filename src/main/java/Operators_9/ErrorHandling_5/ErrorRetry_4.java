package Operators_9.ErrorHandling_5;

import io.reactivex.Observable;

public class ErrorRetry_4 {

    public static void main(String[] args) {

        // Just a bunch o' numbers
        Observable<Integer> badNumbers = Observable.just(5, 6, 7, 8 ,9, 0, 1, 2, 3, 4);

        /*
            This runs infinitely.

            Look carefully at the code.
         */
        System.out.println("Example 1: Retry!");
        badNumbers
                .map(integer -> 9 / integer)
                .retry()
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

    }
}
