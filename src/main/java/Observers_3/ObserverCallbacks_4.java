package Observers_3;

import io.reactivex.Observable;

public class ObserverCallbacks_4 {

    public static void main(String[] args) {
        /*
            Here is another example, using a different override for subscribe().
            - I'm omitting onComplete in the event that we don't care what happens when it ends.
         */

        // Observable
        Observable<String> observable =
                Observable.just("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet");

        // Observer/Chain
        observable
                .map(String::length)
                .filter(integer -> integer > 4)
                .subscribe(
                    integer -> System.out.println("Color Length: " + integer),
                    Throwable::printStackTrace
                        // No onComplete() method
                );

    }
}
