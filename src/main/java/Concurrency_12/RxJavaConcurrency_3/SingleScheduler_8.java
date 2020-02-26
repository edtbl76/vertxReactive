package Concurrency_12.RxJavaConcurrency_3;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SingleScheduler_8 {

    public static void main(String[] args) {

        /*
            This looks the same as the rest of the examples, and I should probably do a better job here.
            Let's say that these operations are transactions in an order management system that aren't using a
            remediation step like compensating transactions or rollback.

            These steps 1 through 5, would be very fragile, as processing them out of order would result in
            data inconsistency/validation issues.

            By using single, we guarantee that only ONE thread is processing these events. This gives us greater control
            over sequencing.
         */
        Observable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.single())
                .blockingSubscribe(System.out::println);
    }
}
