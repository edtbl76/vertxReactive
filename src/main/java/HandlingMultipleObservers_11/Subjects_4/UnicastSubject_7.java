package HandlingMultipleObservers_11.Subjects_4;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;
import io.reactivex.subjects.UnicastSubject;

import java.util.concurrent.TimeUnit;

public class UnicastSubject_7 {

    public static void main(String[] args) {

        /*
            Unicast Subject Factory
         */
        Subject<Long> subject = UnicastSubject.create();


        /*
            Create Infinite Observable and subscribe to the subject we created above.
            (Remember, Subject implements the Observer interface, so we can pass a Subject to subscribe())

         */
        Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .subscribe(subject);

        /*
            There is print statement here that annotates the beginning of our subject.
            - while this wait statement is ticking off, events are firing, but rather than being delivered
            for consumption, they are persisted in cache.
         */
        System.out.println("Nothing is happening! (Yes it is!)");
        Generic.wait(10);

        /*
            Now we have a bonafide Observer subscribing to the Observable.

            - Immediately the stored events are fired off, and the contents of the cache are dumped.
         */
        subject.subscribe(s -> System.out.println("RCVD: " + s));

        /*
            The print statement marks the beginning of "normal operation".
            By "normal", we mean PublishSubject.
         */
        System.out.println("This is better...");
        Generic.wait(10);


    }
}
