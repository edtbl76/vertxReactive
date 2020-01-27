package Observables;

import io.reactivex.Observable;

public class ErrorFactory_13 {

    public static void main(String[] args) {

        // Same idea as the previous, but with different syntax
        Observable.error(() ->
            new Exception("Stuff done be broke!")
        ).subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("You know the drill, and it ain't Black and Decker.")
        );
    }
}
