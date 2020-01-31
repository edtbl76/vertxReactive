package Observables;

import Utils.Generic;
import io.reactivex.Observable;

public class NeverFactory_11 {

    public static void main(String[] args) {

        /*
            This is like an empty, but it does NOT call onComplete().
            - Since there are no emissions, no errors and it never calls onComplete(), you'll need to keep this
            open in a similar manner as Observable.interval().

         */
        Observable<String> never = Observable.never();

        never.subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("I'm not going to me executed!")
        );

        Generic.wait(2000);
    }
}
