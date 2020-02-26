package Concurrency_12.KeepAlive_4;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RxKeepAliveBlockingOperator_1 {

    public static void main(String[] args) {

        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.computation())
                .map(integer -> {
                    Generic.wait(1);
                    return integer;
                })
                .blockingSubscribe(System.out::println);
    }
}
