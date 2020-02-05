package Operators_9.ActionOperators_6;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ActionDoOnDispose_5 {

    public static void main(String[] args) {

        /*
            This is a disposable (remember these??)

            This example is critical, because I've read at LEAST 3 books that get this WRONG.

            doOnDispose() ONLY fires onDispose(). NOT when onComplete() is called.
            - Many texts, tutorials, examples, etc. look at these two concepts as equal, when they are not .
            onComplete() means "I'm out of events. Done, Finished, time to hit the old dustry trail, it's Millah Time"
            onDispose() means "I quit! STOP! NO MORE!"

         */
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnSubscribe(
                        disposable -> System.out.println("I'm a Consumer<Disposable>, and I'm subscribing!"))
                .doOnDispose(() -> System.out.println("I'm fired off when dispose() is called!"))
                .subscribe(
                        second -> System.out.println("RCVD: " + second),
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE")
                );

        /*
            "keepalive" for main thread to allow scheduler to push events.
         */
        Generic.wait(2);

        /*
            Call dispose.
            This is going to trigger the onDispose() method.
            - As we recall doOns are called immediately beforehand, so doOnDispose() will be called, printing out
            the message mentioned above.
         */
        d.dispose();

        /*
            Emissions stop because we disposed the Observable, but we'll demonstrate this by suspending the
            main thread for 2 more seconds.
         */
        Generic.wait(2);

    }
}
