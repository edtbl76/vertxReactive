package Operators_9.Reducers_3;

import io.reactivex.Observable;

public class ReducerCount_1 {

    public static void main(String[] args) {
        Observable.just("Stoker", "Rice", "Meyer")
                .count()
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
