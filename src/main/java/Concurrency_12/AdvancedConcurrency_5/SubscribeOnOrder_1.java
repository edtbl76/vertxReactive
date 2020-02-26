package Concurrency_12.AdvancedConcurrency_5;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SubscribeOnOrder_1 {

    public static void main(String[] args) {

        /*
            SubscribeOn is placed "last" or closest to the final observer.
         */
        Observable.just(1, 2, 3)
                .map(integer -> integer * integer)
                .filter(integer ->  integer > 5)
                .subscribeOn(Schedulers.computation())
                .blockingSubscribe(System.out::println);

        /*
            SubscribeOn is placed "first" or closest to the source observable.
            NOTE: this is preferred.
         */
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.computation())
                .map(integer -> integer * integer)
                .filter(integer ->  integer > 5)
                .blockingSubscribe(System.out::println);
    }
}
