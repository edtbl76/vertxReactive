package Flowables_14.Conversions_3;

import Utils.Generic;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FlowableCreation_2 {

    public static void main(String[] args) {

        /*
            This is almost identiceal to the preceding example.

            Naming conventions aside, the only difference is that we used isCancelled() instead of isDisposed()
            and we have an extra argument in the create() factory, the Backpressure Strategy.
         */
        Flowable<Integer> flowable =
                Flowable.create(flowableEmitter -> {
                   for (int i = 0; i <= 5000; i++) {
                       if (flowableEmitter.isCancelled())
                           return;
                       flowableEmitter.onNext(i);
                   }
                   flowableEmitter.onComplete();
                }, BackpressureStrategy.BUFFER);

        flowable
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
        Generic.wait(10);
    }
}
