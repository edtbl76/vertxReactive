package Operators_9;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class SuppressorSkipWhile_6 {
    public static void main(String[] args) {

        Predicate<Integer> predicate = integer -> integer < 5;

        /*
            This is a smarter version of skip(), such that I will emit events while the conditions of the provided
            predicate are true.
         */
        Observable.range(1, 10)
                .skipWhile(predicate)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("fini")
                );

    }
}

