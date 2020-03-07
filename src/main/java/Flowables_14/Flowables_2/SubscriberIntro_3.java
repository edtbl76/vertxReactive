package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class SubscriberIntro_3 {

    public static void main(String[] args) {

        /*
            This is a Flowable that is using a Subscriber as a consumer. (Because it has to).
            However, as you can see, it is using the same "trinity" of callbacks used by an Observer
            onNext(), onError() and onComplete().
         */
        Flowable.range(1, 100)
                .doOnNext(integer -> System.out.println("Flowable Pushing: " + integer))
                .observeOn(Schedulers.io())
                .subscribe(
                        integer -> System.out.println("[" + Thread.currentThread().getName() +
                                "] Subscriber RCVD: " + integer),
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE!")

                );
        Generic.wait(20);
    }
}
