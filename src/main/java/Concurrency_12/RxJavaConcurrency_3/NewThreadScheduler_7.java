package Concurrency_12.RxJavaConcurrency_3;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class NewThreadScheduler_7 {

    public static void main(String[] args) {


        /*
            Here is an Observable of three strings.
            - I map them to themselves, w/ logic to print out the entire "event" (which is a string of space-separated
                strings)
            - Then we create a new Thread scheduler.
            - then we flat map them to their hierarchically next layer down (just a string of chars)
            - Then we print out the result, which is just the resulting thread.

            Technically the jobs being handed to flatMap are concurrent.

         */
        Observable.just("Create a thread", "Use a Thread", "Destroy a thread")
                .map(s -> {
                    System.out.println("[" + s + "]");
                    return s;
                })
                .subscribeOn(Schedulers.newThread())
                .flatMap(s -> Observable.fromArray(s.split(" ")))
                .blockingSubscribe(
                        s -> System.out.println("[" + s + "]")
                );
    }
}
