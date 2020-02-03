package Operators_9.Reducers_3;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class ReducerAny_4 {

    public static void main(String[] args) {

        Observable<Integer> observable = Observable.just(2, 3, 4, 5);

        /*
            any() with all true
         */
        observable
                .any(getPredicate(6))
                .subscribe(System.out::println);

        /*
            any() with 1 true = true
         */
        observable
                .any(getPredicate(3))
                .subscribe(System.out::println);

        /*
            any() with none true = false
         */
        observable
                .any(getPredicate(2))
                .subscribe(System.out::println);

        /*
            Principle of Vacuous Truth
            - with all() it is true, but w/ any()... it is false.
         */
        observable = Observable.empty();
        observable
                .any(getPredicate(5))
                .subscribe(System.out::println);
    }


    private static Predicate<Integer> getPredicate(int number) {
        return integer -> integer < number;
    }
}
