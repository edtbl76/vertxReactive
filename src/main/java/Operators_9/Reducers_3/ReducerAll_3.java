package Operators_9.Reducers_3;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class ReducerAll_3 {

    public static void main(String[] args) {

        Observable<Integer> numbers = Observable.just(1, 2, 3, 4, 5);

        /*
            Example of a working "all" - all 5 values pass the test.
         */
        System.out.print("Example 1: (True)          : ");
        numbers
                .all(getPredicate(6))
                .subscribe(System.out::println, Throwable::printStackTrace);

        /*
            Example of a working "all" - some values fail, therefore the whole Observable fails.
         */
        System.out.print("Example 2: (False)         : ");
        numbers
                .all(getPredicate(1))
                .subscribe(System.out::println, Throwable::printStackTrace);

        /*
            Example of Vacuous Truth (i.e. Empty returns True)
         */
        System.out.print("Example 3: (Vacuous Truth) : ");
        numbers = Observable.empty();
        numbers.all(getPredicate(10))
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private static Predicate<Integer> getPredicate(int bar) {
        return integer -> integer < bar;
    }
}
