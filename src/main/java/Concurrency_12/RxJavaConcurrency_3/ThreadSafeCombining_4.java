package Concurrency_12.RxJavaConcurrency_3;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class ThreadSafeCombining_4 {

    public static void main(String[] args) {

        /*
            Create two lists of strings.
         */
        List<String> atlantic = Arrays.asList(
                "Boston", "Tampa Bay", "Toronto", "Florida", "Buffalo", "Montreal", "Ottawa", "Detroit");
        List<String> metropolitan = Arrays.asList(
                "Washington", "Pittsburgh", "Philadelphia", "New York I", "Columbus", "Carolina", "New York R",
                "New Jersey");

        /*
            I'm creating two Observables with my lists.
         */
        Observable<String> hockey1 = getObservable(atlantic);
        Observable<String> hockey2 = getObservable(metropolitan);

        /*
            Here we are.
            I've used zip to create a mini asynchronous multitasking schedule maker.

            We've got our keep alive, because the observables returned above are pushed out to
            worker threads via the Scheduler implementation passed to subscribeOn()
         */
        Observable.zip(hockey1, hockey2, (first, last) -> first + " vs. " + last)
                .subscribe(System.out::println);

        Generic.wait(10);

    }

    public static Observable<String> getObservable(List<String> list) {
        /*
            This is different from previous examples, because we've cut the chain short of subscribe() (which
            returns a Disposable).

            By ending on an operator(), we can return an Observable.
            This is a common pattern where the "setup" for a pipeline is the mostly the same, so we can
            abstract away the details and just pass our "event" into the head of the chain, do the "rote" work and
            then return an Observable that will be handed off to do something more interesting.
         */
        return Observable.fromIterable(list)
                .subscribeOn(Schedulers.computation())
                .map(s -> {
                    Generic.wait(1);
                    return s;
                });
    }
}
