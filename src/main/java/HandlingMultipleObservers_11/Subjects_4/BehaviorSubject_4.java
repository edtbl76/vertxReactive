package HandlingMultipleObservers_11.Subjects_4;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class BehaviorSubject_4 {

    public static void main(String[] args) {

        /*
            Behavior Subject factory...
         */
        Subject<String> subject = BehaviorSubject.create();

        /*
            Create our Observer first, so that we have  "teeth" to eat with.
         */
        subject.subscribe(
                s -> System.out.println("1: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("1 DONE")
        );

        /*
            Firing off these two events will immediately result in them being eaten up by the Big Bad
            Observer #1.
            The output will be a printed line for each string.
         */
        subject.onNext("cookie");
        subject.onNext("steak");


        /*
            Remember how PublishSubject missed events if the Observer was defined afterwards?
            BehaviorSubjects solve this (partially) by replaying the last event to be pushed.

            The output here will be "steak"
         */
        subject.subscribe(
                s -> System.out.println("2: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("2 DONE")
        );

        /*
            We are calling onComplete() here to finish the stream.
         */
        subject.onComplete();

        /*
            We are calling a new Observer, but the stream is closed.
            As a result, we will only get an Empty Observable (which only calls onComplete())
         */
        subject.subscribe(
                s -> System.out.println("3: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("DONE")
        );

    }
}
