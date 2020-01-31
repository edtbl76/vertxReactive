package Observables_2;

import io.reactivex.Observable;

public class EmptyFactory_10 {

    public static void main(String[] args) {

        /*
           This actually emits NOTHING.
           However, it DOES call complete().

           Why?
           - useful for representing empty data sets
           - can also result from filter() operator when all emissions fail to meet a condition.
           - great way to test infra/communications.


           Often called the RxNull.
         */
        Observable<String> empty = Observable.empty();

        empty.subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Complete")
        );
    }
}
