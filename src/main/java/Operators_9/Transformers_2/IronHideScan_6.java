package Operators_9.Transformers_2;

import io.reactivex.Observable;

public class IronHideScan_6 {

    public static void main(String[] args) {

        /*
            This is the basic implementation of scan.

            The emission order:
                no accumulation + 1     = 1
                1 + 2                   = 3
                3 + 3                   = 6
                6 + 4                   = 10
                10 + 5                  = 15
         */
        System.out.print("Accumulator Example 1: ");
        Observable.range(1, 5)
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

        /*
            Here is a more practical example. We have a bunch of events that we want to count.
         */
        System.out.print("Accumulator Example 2: ");
        Observable.just("One", "Two", "Three", "Four", "Five")
                .scan(1, (sum, next) -> sum + 1)
                .subscribe(s -> System.out.print(s + " "), Throwable::printStackTrace, System.out::println);

    }
}
