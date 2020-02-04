package Operators_9.ActionOperators_6;

import io.reactivex.Observable;

public class ActionDoOnEach_4 {

    public static void main(String[] args) {

        /*
            Do On Each is a utility player...
            - in this example it is capturing onNext() and onComplete()
         */
        Observable.just("Echo", "Papa", "Alpha", "Mike")
                .doOnEach(stringNotification -> System.out.println("Something Happened: " + stringNotification))
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("COMPLETED")
                );

        /*
            This snags onNext and onError
         */
        System.out.println();
        Observable.just(1, 2, 0, 9)
                .map(integer -> 1 / integer)
                .doOnEach(integerNotification -> System.out.println("Something Happened: " + integerNotification))
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        throwable -> System.out.println("ERROR: " + throwable),
                        () -> System.out.println("COMPLETED")
                );

    }
}
