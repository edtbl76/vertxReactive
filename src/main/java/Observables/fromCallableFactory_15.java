package Observables;

import io.reactivex.Observable;

public class fromCallableFactory_15 {
    public static void main(String[] args) {

        /*
            PROBLEM:
                - This error is THROWN rather than handed back up to the Observer.
                - This is a NON-REACTIVE method of error handling.
                    - The proper approach would be to hand the error back up to the Observer, who has the
                    expected task of handling the error.
         */
        // Uncomment the Following Call to see the example.
        //badExample();


        /*
            This passes the error back up to the Observer, who in this case has decided to print it out.
            fromCallable() works similar to defer() in that it allows us to wrap a calculation/call with a Reactive
            operator to ensure that it is handled reactively.

            THIS IS AN IMPORTANT CONCEPT:
            - When expecting to deliver a reactive solution, exception handling must be handled in the same design/model
            for the purposes of consistency.
         */
        goodExample();

    }

    private static void badExample() {

        Observable.just(1/0)
                .subscribe(
                        System.out::println,
                        ex -> System.out.println("Error!: " + ex)
                );
    }

    private static void goodExample() {

        Observable.fromCallable(() -> 1/0)
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        throwable -> System.out.println("ERROR:" + throwable)
                );
    }
}
