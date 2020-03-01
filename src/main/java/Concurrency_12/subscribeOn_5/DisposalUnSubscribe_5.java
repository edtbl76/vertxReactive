package Concurrency_12.subscribeOn_5;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class DisposalUnSubscribe_5 {

    public static void main(String[] args) {

        /*
           Same example, but I've called unsubscribeOn() so that observable cancellation is moved to a new thread so
           it doesn't block the main thread.

           In this case, the thread that is calling dispose() becomes more of a proxy or dispatcher to move
           the actual disposal work onto a separate thread (io() was chosen because it's good for blocking workloads)

         */
        Disposable betterDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("[" + Thread.currentThread().getName() + "] Disposing..."))
                .unsubscribeOn(Schedulers.io())
                .subscribe(integer -> System.out.println("RCVD: " + integer));

        /*
         */
        Generic.wait(3);
        betterDisposable.dispose();
        Generic.wait(3);

    }
}
