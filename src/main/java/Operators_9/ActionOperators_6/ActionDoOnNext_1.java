package Operators_9.ActionOperators_6;

import io.reactivex.Observable;

public class ActionDoOnNext_1 {
    public static void main(String[] args) {

        /*
            Here we've created a Consumer<T> Lambda in doOnNext that is printing the emission
            before we actually put it into the emission chain.

            This offers all sorts of interesting possibilities.
                - tracing?
                - logging?
                - debugging?

            I deliberately filtered out one of the results to demonstrate (and hopefully reinforce) the
            simplicity with which we manage concurrent streams of data.

         */
        System.out.println("DoOnNext: ");
        Observable.just("Whiskey", "Tango", "Foxtrot")
                .doOnNext(s -> System.out.println("Pushing: " + s))
                .map(String::length)
                .filter(integer -> integer > 5)
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));

        /*
            If you execute this, the result becomes immediately apparent.
            - doOnAction happens BEFORE emission is passed downstream.
            - doAfterAction happens AFTER emission is passed downstream.

            The interesting factoid here is that when we execute doAfter, we are suppressing the output of the
            "subscribe" action.

            Use cases might be to provide notifications that an event has occurred to trigger other actions.
            The value of this is that we are emitting the Action after we know the event has been safely and
            successfully been pushed downstream.
         */
        System.out.println("\nDoAfterNext: ");
        Observable.just("Oscar", "Mike", "Golf")
                .doAfterNext(s -> System.out.println("Pushed: " + s))
                .map(String::length)
                .filter(integer -> integer > 5)
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done"));
    }
}
