package Concurrency_12.RxJavaConcurrency_3;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ComputationScheduler_5 {

    public static void main(String[] args) {

        /*
            This is a basic implementation of a computation optimized scheduler .

            Our task is basic math.
         */
        Observable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.computation())
                .map(integer -> integer * integer)
        .blockingSubscribe(System.out::println);

    }


}
