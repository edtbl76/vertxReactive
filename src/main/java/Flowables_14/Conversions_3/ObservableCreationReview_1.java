package Flowables_14.Conversions_3;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservableCreationReview_1 {

    public static void main(String[] args) {

        /*
            This is our DIY Observable built from scratch.
         */
        Observable<Integer> observable =
                Observable.create(observableEmitter -> {
                   for (int i = 0; i <= 500; i++) {
                       if (observableEmitter.isDisposed())
                           return;
                       observableEmitter.onNext(i);
                   }
                   observableEmitter.onComplete();
                });

        /*
            This is our Observer, followed by a keepalive.
         */
        observable
                .observeOn(Schedulers.io())
                .subscribe(
                        integer -> System.out.println("[" + Thread.currentThread().getName() + "] - " + integer));

        Generic.wait(5);

    }
}
