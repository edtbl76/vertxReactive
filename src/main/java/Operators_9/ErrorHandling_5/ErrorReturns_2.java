package Operators_9.ErrorHandling_5;

import io.reactivex.Observable;

public class ErrorReturns_2 {

    public static void main(String[] args) {

        Observable<Integer> numbers = Observable.just(8,9,0,5);

        /*
            onErrorReturnItem.
            This is an alternative value.
            - this is similar to having a default value, but rather than having the default event
            emitted for empty Observables, the default value is emitted in place of an error .


         */
        System.out.print("Example 1: onErrorReturnItem: ");
        numbers
                .map(integer -> 1 / integer)
                .onErrorReturnItem(-1)
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );


        /*
            onErrorReturn can do the same thing as onErrorReturnItem, but it also gives you access to the throwable,
            so that you can use to determine the returned value
         */
        System.out.print("Example 2: onErrorReturn    : ");
        numbers
                .map(integer -> 2 / integer)
                .onErrorReturn(throwable -> -1)
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        throwable -> System.out.println("ERROR: " + throwable),
                        System.out::println
                );

        /*
            onErrorReturn, printing the throwable AND returning the value.
            - more accurately, you might insert the default value to avoid kicking the error back up the chain,
            but at the same point using access to the throwable to probably generate some log fodder. (maybe a warning)
            or potentially a DLQ. (Dead Letter Queue)
         */
        System.out.print("Example 3: onErrorReturn    : ");
        numbers
                .map(integer -> 3 / integer)
                .onErrorReturn(throwable -> {
                    System.out.println("\nERROR: " + throwable);
                    return -1;

                })
                .subscribe(
                        integer-> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

        /*
            Hey Ed!.
            I think I found a bug in RxJava. These examples aren't printing the result of the last event/data???

            Nope. You have not found a bug. This is normal behavior. Once the error is encountered, the game is over,
            and events stop flowing.

            Ed, how do I solve this?

            Reactively my good friend! You would need to handle this INSIDE the operator prior to the onErrorReturnBlah().
            - Possibly, like this!
         */
        System.out.print("Example 4: WackyHackyError : ");
        numbers
                .map(
                        integer -> {
                            try {
                                return 4 / integer;
                            } catch (ArithmeticException ex) {
                                /*
                                    Hey! This is just like using the onErrorReturnBlah() thing!

                                    - close but not quite. Remember that there is a distinct Observable to Observer
                                    transition when moving between operators that isn't taking place here.

                                    - I prefer to think of this as a summarization step, where we are summarizing the
                                    functionality of the onErrorReturn operator due to a lack of existing functionality.
                                 */
                                return -1;
                            }
                        })
                .subscribe(
                        integer -> System.out.print(integer + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );



    }
}
