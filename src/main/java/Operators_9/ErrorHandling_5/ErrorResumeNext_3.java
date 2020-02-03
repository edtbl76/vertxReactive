package Operators_9.ErrorHandling_5;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class ErrorResumeNext_3 {

    public static void main(String[] args) {

        // Just a bunch o' numbers
        Observable<Integer> badNumbers = Observable.just(5, 6, 7, 8 ,9, 0, 1, 2, 3, 4);

        /*
            Once we hit an error, we start pushing "-1" (undefined) values from a
            different Observable.
            - in this case, we've emitted the same number of Observables as there were total elements
            not processed. (i.e. the first error, and then the 3 following events that were blown off given
            the exception).
         */
        System.out.println("Example 1: The counter: ");
        badNumbers
                .map(integer -> 1 / integer)
                .onErrorResumeNext(
                        Observable.just(-1)
                        .repeat(4))
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

        /*
            Here we fail quietly.
            This is similar to onErrorReturn, but instead, we stop transmitting once the error is encountered.
         */
        System.out.println("Example 2: The silencer ");
        badNumbers
                .map(integer -> 3 / integer)
                .onErrorResumeNext(
                        Observable.empty())
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

        /*
            Since you mentioned it (or I did), we CAN emulate onErrorReturn behavior... although we might as well just
            use THAT operator instead?

         */
        System.out.println("Example 3: The returner ");
        badNumbers
                .map(integer -> 5 / integer)
                .onErrorResumeNext(
                        (Throwable throwable) -> Observable.just(-1).repeat(3))
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

    }
}
