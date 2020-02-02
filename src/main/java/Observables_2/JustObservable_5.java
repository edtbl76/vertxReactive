package Observables_2;

import io.reactivex.Observable;

public class JustObservable_5 {

    public static void main(String[] args) {

        /*
            Observable creation using just()
            - In this case the onNext() and onComplete() operators are provided for you, so you don't have to call them

            NOTE: if you need more than 10 items, then you can't use just. "Just" a note.
         */
        Observable<String> observable = Observable.just("Length", "Matters", "In", "This", "Exercise");

        /*
            Here is the operator chain that "ends" w/ an observer
         */
        observable
                .map(String::length)
                .filter(integer -> integer > 3)
                .subscribe(System.out::println);
    }
}
