package Observers;

import io.reactivex.Observable;

public class ObserverCallbacks_3 {

    public static void main(String[] args) {

        /*
            This is just another way to write the previous example (i.e. without defining the callbacks
            outside of the subscribe() operator)

            Personally, my approach is the following:
            - If the implementation of any of the lambdas is going to be "involved", I'll separate the implementation
            from the subscribe() operator.
            - Typically, one-liners (like below), can remain in the subscribe() operator.

            As always... readability first!
         */

        Observable<String> stableOfBacks =
                Observable.just("Marshawn", "Turbin", "Carson", "Penny", "Homer");

        stableOfBacks
                .map(String::length)
                .filter(integer -> integer > 5)
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }
}
