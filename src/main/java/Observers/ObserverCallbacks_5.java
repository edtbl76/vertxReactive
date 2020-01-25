package Observers;

import io.reactivex.Observable;

public class ObserverCallbacks_5 {

    public static void main(String[] args) {
        /*
            Heck, what's another callback between friends? subscribe() will even allow you to throw
            caution to the wind and omit the call to onError()

            - This is probably not the best idea by the way.
         */

        Observable<String> observable = Observable.just("this", "probably", "isn't", "the", "safest", "option");

        /*
            Hey. Waiiiitaminute!
            This is pretty much where we ended the previous set of examples!

            You're absolutely right!.
         */
        observable
                .map(String::length)
                .filter(integer -> integer > 3)
                .subscribe(integer -> System.out.println("Length: " + integer));
    }
}
