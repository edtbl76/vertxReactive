package HandlingMultipleObservers_11.Subjects_4;

import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ReplaySubject_5 {

    public static void main(String[] args) {

        /*
            Replay Subject Factory
         */
        Subject<String> subject = ReplaySubject.create();

        /*
            super simple Observer that just prints out my stuff.
         */
        subject.subscribe(
                s -> System.out.println("No. 1: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("1 DONE"));

        /*
            Bad joke embedded in a string of events w/ string data. (Say that 10 times fast)
         */
        subject.onNext("you'd");
        subject.onNext("better");
        subject.onNext("hope");
        subject.onNext("I'm");
        subject.onNext("not");
        subject.onNext("infinite");

         /*
            Another super simple Observer that gets everything, because we are using cache().
            - This happens before onComplete(), but we get it all.
         */
        subject.subscribe(
                s -> System.out.println("Before onComplete: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("BEFORE DONE")
        );

        /*
            NOW we call onComplete()
            - onComplete() will be simultaneously called against the first two observables.
         */
        subject.onComplete();

        /*
            Another super simple Observer that gets everything, because we are using cache()
            - This happens after onComplete(), but we get it all.
         */
        subject.subscribe(
                s -> System.out.println("After onComplete: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("AFTER DONE")
        );

    }
}
