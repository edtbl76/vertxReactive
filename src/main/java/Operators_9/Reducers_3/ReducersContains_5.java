package Operators_9.Reducers_3;

import io.reactivex.Observable;

public class ReducersContains_5 {

    public static void main(String[] args) {

        /*
            Contains is a great way to determine if a value exists in an Observable
            (perhaps to be cherry picked for further processing, suppression, transformation, etc.)
         */
        System.out.print("Example 1 (Found)     :");
        Observable.range(1, 10_000)
                .contains(500)
                .subscribe(System.out::println, Throwable::printStackTrace);

        // ... Unless you can't find what you are looking for
        System.out.print("Example 2 (Not Found) :");
        Observable.just("Hello", "World")
                .contains("planet")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
