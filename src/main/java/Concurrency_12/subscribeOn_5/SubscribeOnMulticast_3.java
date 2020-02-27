package Concurrency_12.subscribeOn_5;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SubscribeOnMulticast_3 {

    public static void main(String[] args) {

        /*
            This is a twist on our previous example.
            It's the same exact code, but we've appended two multicast calls to the end.

            publish() is going to turn our cold observable hot
            autoConnect() is going to attach the first two Observers to this source.
         */
        Observable<Integer> observable = Observable.range(1, 5)
                .subscribeOn(Schedulers.computation())
                .map(integer -> {
                    int generic = Generic.getRandom(1, 2);
                    Generic.wait(generic);
                    return integer * (generic * Generic.getRandom(1, generic));
                })
                .publish()
                .autoConnect(2);

        /*
            Here are my Observables.
         */
        observable.subscribe(integer ->
                System.out.println("[" + Thread.currentThread().getName() + "] - RCVD: " + integer)
        );
        observable.subscribe(integer ->
                System.out.println("[" + Thread.currentThread().getName() + "] - RCVD: " + integer)
        );

        // Here is the obligatory keep alive.
        Generic.wait(10);

        /*
            Notice anything different about the output?
            - YES!

            - All of the work was multicast to the same thread. We didn't get the advantage of concurrency, so this
            job (in theory) might have taken twice as long to perform.

         */
    }
}
