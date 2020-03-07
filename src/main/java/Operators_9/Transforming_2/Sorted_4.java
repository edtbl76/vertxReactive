package Operators_9.Transforming_2;

import io.reactivex.Observable;

import java.util.Comparator;

public class Sorted_4 {
    public static void main(String[] args) {

        /*
            This is a basic Observable of ints
         */
        Observable<Integer> numbers = Observable.just(6,3,8,9,2,5,3,7,8,3);

        /*
            Unsorted
         */
        System.out.print("Unsorted :\t");
        numbers.subscribe(s -> System.out.print(s + " "),
                Throwable::printStackTrace,
                System.out::println
        );

        /*
            I'm sorting them
         */
        System.out.print("Sorted   :\t");
        numbers
                .sorted()
                // Yes... this is even shorter.
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

        /*
            Reverse Sorted using Comparator
         */
        System.out.print("Reverse  :\t");
        numbers
                .sorted(Comparator.reverseOrder())
                // Yes... this is even shorter.
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

    }
}
