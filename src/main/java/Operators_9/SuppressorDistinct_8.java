package Operators_9;

import io.reactivex.Observable;

public class SuppressorDistinct_8 {

    public static void main(String[] args) {

        // Regular Observable.
        Observable<String> observable =
                Observable.just("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");

        /*
            Example One, just a standard Observer, mapping to length.
         */
        observable
                .map(String::length)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Cuidado")
                );


        /*
            Example Two,
            This will only print the distinct events. (Useful for indexing, sets, etc.)

            The value is that this consolidates many steps of work that would otherwise be required with
            map() and filter()


         */
        System.out.println();
        observable
                .distinct(String::length)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Arret!")
                );

        /*
            Example Three
            This uses distinctUntilChanged(). This is similar to distinct, but it only impacts CONSECUTIVE emissions.
            (This is a great way to compress repetition to a single event)
         */
        System.out.println();
        observable
                .distinctUntilChanged(String::length)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Halt!")
                );

    }
}
