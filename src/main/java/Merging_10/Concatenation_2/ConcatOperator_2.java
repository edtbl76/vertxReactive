package Merging_10.Concatenation_2;

import io.reactivex.Observable;

public class ConcatOperator_2 {

    public static void main(String[] args) {

        // Observables... nothing special
        Observable<Character> firstName = Observable.just('o', 's', 'c', 'a', 'r');
        Observable<Character> secondName = Observable.just('m', 'e', 'y', 'e', 'r');

        /*
            This is the operator flavor of Rx concatenation, concatWith().

            The code is pretty self-explanatory.
         */
        firstName.concatWith(secondName)
                .subscribe(
                        System.out::print,
                        Throwable::printStackTrace,
                        System.out::println
                );

        secondName.concatWith(firstName)
                .subscribe(
                        System.out::print,
                        Throwable::printStackTrace,
                        System.out::println
                );


    }
}
