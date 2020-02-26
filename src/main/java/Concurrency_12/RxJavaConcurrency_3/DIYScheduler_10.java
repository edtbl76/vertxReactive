package Concurrency_12.RxJavaConcurrency_3;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DIYScheduler_10 {

    public static void main(String[] args) {


        /*
            DIY = do it your self (i.e. use from())
            - I'm creating my own thread pool here using the old-fashioned java.util.concurrent.ExecutorService.
         */
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Scheduler scheduler = Schedulers.from(executorService);

        /*
            Here is an Observable leveraging a custom built scheduler.
            The implementation isn't that much different. We are passing the scheduler to subscribeOn(), making the
            integration pretty easily.

            However, ExecutorService is the wild west in terms of keep alives. It is going to keep this Observable
            listening indefinitely. We don't necessarily want that. We want it to listen to events, shove them out and
            then shut down to avoid resource leaks.

            Executors have shutdown() methods for calling it quits, so we have to make sure to pass the shutdown
            method as a callback to doFinally() to prevent leakage.

            NOTE: If we don't do this, the threadPool will remain allocated even though the Observable has been
            disposed of.

         */
        Observable.just("nothing", "special", "here", "to", "see")
                .subscribeOn(scheduler)
                .doFinally(executorService::shutdown)
                .subscribe(System.out::println);
    }
}
