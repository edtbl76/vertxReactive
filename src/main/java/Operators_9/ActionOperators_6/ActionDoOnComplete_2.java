package Operators_9.ActionOperators_6;

import io.reactivex.Observable;

public class ActionDoOnComplete_2 {

    public static void main(String[] args) {

        /*
            This example demonstrates the ever-so-slight difference between doOnComplete, and onComplete.

            while doOnComplete() is executed when onComplete() is called, it is actually executed FIRST.
            This allows us to fire off actions just prior to completion.

            Similar to the other actions, this is a great debugging/tracing point to determine what is done
         */
        Observable.just("Foxtrot", "Umbrella", "Bravo", "Alpha", "Romeo")
                .doOnComplete(() -> System.out.println("I'm doOnComplete()!"))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("I'm onComplete()")
                );
    }
}
