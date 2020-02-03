package Operators_9.ErrorHandling_5;

import io.reactivex.Observable;

public class ErrorToObserver_1 {

    public static void main(String[] args) {
        Observable.just(1)
                .map(integer -> integer / 0)
                .subscribe(
                        System.out::print,
                        throwable -> System.out.println("ERROR: " + throwable),
                        () -> System.out.println("Done I guess.")
                );
    }
}
