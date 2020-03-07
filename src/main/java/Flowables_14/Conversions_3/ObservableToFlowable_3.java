package Flowables_14.Conversions_3;

import Utils.Generic;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservableToFlowable_3 {

    public static void main(String[] args) {

        /*
            Normal Observable, but our factory is now falling into the "thousands of events" category.

            In this case, we might want to move it to a flowable...
         */
        Observable<Integer> observable = Observable.range(1, 10_000);

        /*
            This is a simple way of converting an Observable chain into a flowable chain.

            Naturally, when we are moving to a Flowable, we want to identify the backpressure strategy to be used.
         */
        observable
                .toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
        Generic.wait(10);
    }
}
