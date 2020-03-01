package Concurrency_12.subscribeOn_5;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class DisposalNoUnSubscribe_5 {

    public static void main(String[] args) {

        /*
            This is just a throwback to disposables. The only thing fancy is that we are using the
            action operator to print out what thread the disposal is happening on.
         */
        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("[" + Thread.currentThread().getName() + "] Disposing..."))
                .subscribe(integer -> System.out.println("RCVD: " + integer));

        /*
            Wait for the disposal so that our infinite event factory can work.
            Then we dispose the observable.
            Despite the 3 second keep alive, the observable has been disposed, so events no longer perpetuate.
         */
        Generic.wait(3);
        disposable.dispose();
        Generic.wait(3);

        /*
            The most noteworthy aspect of the output is that we are disposing on the main thread.

            As we've discussed, this means the main thread is going to block until the observable is disposed of. If we
            are tearing down a network socket or another thread, this might take some time. This can lead to logjams
            when trying to clean up our work.

            This prevents us from freeing resources fast enough, and we have a continuous resource leak until we end
            up thread starved.
         */

    }
}
