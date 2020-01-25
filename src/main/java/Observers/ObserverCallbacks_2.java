package Observers;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ObserverCallbacks_2 {

    public static void main(String[] args) {

        /*
            Here we are going to create objects to represent the callbacks that can replace the overly verbose
            example of overriding the interface.
         */
        Consumer<Integer> onNext = integer -> System.out.println("RCVD: " + integer);
        Action onComplete = () -> System.out.println("Completed!");
        Consumer<Throwable> onError = Throwable::printStackTrace;

        /*
            Observable
         */
        Observable<String> observable = Observable.just("Tyler", "Doug", "D.K.", "Jermaine", "Golden");

        /*
            Observer using the callbacks we provided above.
         */
        observable
                .map(String::length)
                .filter(integer -> integer > 4)
                .subscribe(onNext, onError, onComplete);

    }
}
