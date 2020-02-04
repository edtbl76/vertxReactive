package Operators_9.ActionOperators_6;

import io.reactivex.Observable;

public class ActionDoOnTerminate_6 {

    public static void main(String[] args) {

        /*
            Terminates based on onComplete()
         */
        Observable.just("John", "Sarah")
                .doOnTerminate(() -> System.out.println("I'll be back"))
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("COMPLETE")
                );

        /*
            This will terminate based on an error
         */
        System.out.println();
        Observable.just(1, 0)
                .map(integer -> 1 / integer)
                .doOnTerminate(() -> System.out.println("Hasta La Vista, Baby"))
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        throwable -> System.out.println("Oops: " + throwable)
                );

    }
}
