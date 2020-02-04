package Operators_9.ActionOperators_6;

import io.reactivex.Observable;

public class ActionDoOnError_3 {

    public static void main(String[] args) {

        /*
            This behaves pretty much the same way onComplete does, but it handles error signalling rather than
            completion.

            doOnError fires before onError.
                - in this case we see multiple errors, but we could do other things (like dump out logs, trigger
                alarms, or initiate self-destruct sequence)

            NOTE:
                - we did this twice. Why? Because we could have created meaningful messages based on WHERE in the
                chain of observables the error was captured.
         */
        Observable.just(1,5,0,9,8)
                .doOnError(
                        throwable -> System.out.println("Oops: " + throwable))
                .map(integer -> 1 / integer)
                .doOnError(
                        throwable -> System.out.println("Oops 2 doOnError: " + throwable))
                .subscribe(
                        System.out::println,
                        throwable -> System.out.println("Oops 33 1/3 onError: " + throwable),
                        () -> System.out.println("Are we done yet?")
                );
    }
}
