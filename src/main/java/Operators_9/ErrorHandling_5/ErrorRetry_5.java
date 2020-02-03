package Operators_9.ErrorHandling_5;

import io.reactivex.Observable;

public class ErrorRetry_5 {

    public static void main(String[] args) {

        Observable<Integer> badNumbers = Observable.just(5, 6, 7, 8 ,9, 0, 1, 2, 3, 4);

        /*
            Retry with the number of attempts before we bail out.
            - The print statements give a better indication of what is happening.
                - we print out 0 inside the map() operator before it blows up by /0 error.
                - it pukes, so we go back to 5 and start over.
                - it pukes a second time (last retry) and we start over
                - once we get to the next one, the game is over.
            - retry() is going to go through the ENTIRE chain.
         */
        System.out.println("Example 1: Retry!");
        badNumbers
                .map(integer -> {
                    System.out.print(integer + " ");
                    return 9 / integer;
                })
                .retry(2)
                .subscribe(
                        integer -> System.out.print("[" + integer + "] "),
                        throwable -> System.out.println("\nERROR: " + throwable),
                        System.out::println
                );

        /*
            We've passed a throwable to the retry.
            - We know that an empty retry() operator results in infinite retries. BAD.
            - In this case, we've told it to retry if it's not an ArithmeticException.
                - NOTE: This is normally bad, because there dozens of other exceptions that will result in the
                same infinite retries. Normally, we want it to retry in the presence of a specific type of exception.
                - a great example of exceptions here are throttling types of exceptions, or circumstances where
                resources aren't available... BUT.... I'd still prefer not to do this infinitely.
         */
        System.out.println("\nExample 2: Retry with Throwable");
        badNumbers
                .map(integer -> {
                    System.out.print(integer + " ");
                    return 10 / integer;
                })
                .retry(throwable -> !(throwable instanceof ArithmeticException))
                .subscribe(
                        integer -> System.out.print("[" + integer + "] "),
                        throwable -> System.out.println("\nERROR: " + throwable),
                        System.out::println
                );


        /*
            Instead of doing this infinitely... we can provide a BiPredicate that combines the
            previous two examples.
                1.) The first argument to the BiPredicate is the retry count.
                2.) the second argument is the throwable, which can be used for fine tuning.

            The result here is that we can handle specific errors... specifically.
            While it is out of the scope of this code example, building on these concepts is how we
            focus on ever decreasing failure signals to achieve reliability in our software.

            While the result of this example is the same of the first, we have achieved that result w/
            a more elegant solution that specifically isolates the failure mode of this particular instance,
            allowing other failures to vary if that's what our implementation/design requires.

         */
        System.out.println("\nExample 3: BiPredicate Retry");
        badNumbers
                .map(integer -> {
                    System.out.print(integer + " ");
                    return 11 / integer;
                })
                .retry((integer, throwable) ->
                    integer <= 2 && (throwable instanceof ArithmeticException)
                )
                .subscribe(
                        integer -> System.out.print("[" + integer + "] "),
                        throwable -> System.out.println("\nERROR: " + throwable),
                        System.out::println
                );

        /*
            NOTE: If this is confusing, revisit Transformers_2.ProwlRepeat_6 (repeatUntil).

            retryUntil is like an rx 'while' loop. The argument for a retryUntil is a BooleanSupplier, such that the
            operator is executed until the supplier is no longer true.

            This example might be more practical for many designs/systems as it is associated with a resource more
            consistently compared to metrics and performance: time.

            The number of retries is ambiguous. If a retry takes 6 weeks to produce, I have more important problems to
            address. However, if I can retry an operation hundreds of thousands of times in a short span of time, then I
            am probably going to want to time box retries to avoid causing performance problems.

                NOTE: For posterity...I gave this a 5 millisecond window, and it pushed MANY more events than my
                previous examples

         */
        final long start = System.currentTimeMillis();
        System.out.println("\nExample 4: retryUntil(): ");
        badNumbers
                .map(integer -> {
                    System.out.print(integer + " ");
                    return 12 / integer;
                })
                .retryUntil(() -> System.currentTimeMillis() - start > 5)
                .subscribe(
                        integer -> System.out.print("[" + integer + "] "),
                        throwable -> System.out.println("\nERROR: " + throwable)
                );


        /*
            retryWhen() gives more control over retry() functionality.
            - this is slightly more advanced. Ignote the flatMap for now. (We'll get to those).

            - in this example, we're going to blow by the math example, because we know we can't handle it.

         */
        System.out.println("\nExample 5: retryWhen()");
        badNumbers
                .map(integer -> {
                    System.out.print(integer + " ");
                    return 13 / integer;
                })
                .retryWhen(throwableObservable -> throwableObservable.flatMap(
                        throwable -> {
                            if (!(throwable instanceof ArithmeticException))
                                return Observable.just(-1);
                            return Observable.error(throwable);
                        }))
                .subscribe(
                        integer -> System.out.print("[" + integer + "] "),
                        throwable -> System.out.print("\nERROR: " + throwable),
                        System.out::println
                );

    }
}
