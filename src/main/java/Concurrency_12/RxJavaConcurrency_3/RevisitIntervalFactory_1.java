package Concurrency_12.RxJavaConcurrency_3;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class RevisitIntervalFactory_1 {

    public static void main(String[] args) {

        /*
            Remember this?
            This was our basic infinite counting observable.
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> l + "...")
                .subscribe(System.out::println);

        /*
            However, without this little keep-alive trick, the main thread would just end.

            When I introduced the Interval Factory in "Observables", I was also sneakily introducing concurrency.

            The main thread is more or less just an administrative thread in this case. It was responsible for
            firing off the Observable.interval(). However, it actually delegated that work to a separate computation
            thread instead.

            These calls are asynchronous, so as far as the main thread is concerned, its work is done just by
            placing the factory on its own thread. Since it thinks it is done, it progresses to the end of the main
            method, and terminates before the interval() has a chance to push out its events.

            Our "keep alive" is designed to emulate a long running process or daemon that remains active long enough for
            work delegated to other threads to be completed.
         */
        Generic.wait(5);
    }
}
