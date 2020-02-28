package Concurrency_12.observeOn_6;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class observeOnSchedulerSwitching_2 {

    public static void main(String[] args) {

        /*
            This is one of my favorite examples.  This truly demonstrates the nature of swapping between schedulers.
            The example of the data is stupid, so just focus on the process itself. At some point I'll probably think
            of a better "workflow" for the example.

            1.) We have an Observable executing on an io() scheduler. We'll say this is information being read in from
            a KV store or over a REST call.

            - flatmap is used to separate our values from our keys.
            - our action operator (You haven't forgotten about these ruffians have you??) prints the data and the
            thread is was processed against.

                Here we are working on the RxCachedThreadScheduler (i.e. the io() scheduler).

            2.) Now that we've read in from a socket, let's say that we have to do some SERIOUS processing. This
            example is lightweight just to avoid any confusion over what is going on.

            - our observeOn switches our context to RxComputationThreadPool (computation() scheduler).
            - we're filtering out the keys and then essentially creating a single string from each of the "words"
            - our action operator does the same thing as the previous "chunk" of our chain, but it occurs on
            "success" as opposed to the firing of an event.

            The output is going to be the entire "reduced" string, but the thread is from the RxComputationThreadPool.

            Remember, in the real world, switching contexts between threads is expensive, so you are only going to
            do this for workflows that are doing "REAL WORK". This is a contrived example to demonstrate how it works,
            but you'd never actually do this. The tasks being performed are so incredibly lightweight, that there isn't
            a real benefit in switching threads.

            3.) We make one final swap back to the io() scheduler so that we can "write" our data back out to the web,
            or maybe to a file.

            Our only "task" was to move the thread back to io() before being consumed by the Observer. While I'm just
            printing out the event via onNext(), we're pretending that this is some write-intensive blocking operation.

            The reason I opted to print it out is so you can see the value.
            The previous step ended with the doOnSuccess() action operator, which essentially means that the chain is
            "over". The event I printed at that step is identical to the data being printed out when the event "hits"
            the Observer.

            BUT. We moved from RxComputationThreadPool to RxCachedThreadScheduler.

            That is precisely how observeOn() works!


         */
        Observable.fromArray("1:Hello", "2:World", "3:This", "4:Is", "5:Silly")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Observable.fromArray(s.split(":")))
                .doOnNext(s -> System.out.println("[" + Thread.currentThread().getName() + "] " + s))
                .observeOn(Schedulers.computation())
                .filter(s -> !s.matches("[0-9]+"))
                .reduce((total, next) -> total + " " + next)
                .doOnSuccess(s -> System.out.println("[" + Thread.currentThread().getName() + "] " + s))
                .observeOn(Schedulers.io())
                .subscribe(
                        s -> System.out.println("[" + Thread.currentThread().getName() + "] " + s)
        );

        Generic.wait(10);
    }
}
