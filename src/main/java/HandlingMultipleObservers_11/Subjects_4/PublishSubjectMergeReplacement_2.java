package HandlingMultipleObservers_11.Subjects_4;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.TimeUnit;

public class PublishSubjectMergeReplacement_2 {

    public static void main(String[] args) {

        /*
            I've created two infinite observables firing at different intervals of time.
         */
        Observable<String> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> (l + 1) + "s");

        Observable<String> observable2 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(l -> ((l + 1) * 200) + "ms");

        /*
            create a Subject factory.
         */
        Subject<String> subject = PublishSubject.create();

        /*
            Subject Observable.
         */
        subject.subscribe(System.out::println);

        /*
            have each Observable subscribe to the subject.
         */
        observable1.subscribe(subject);
        observable2.subscribe(subject);

        Generic.wait(10);

        /*
            OUTPUT NOTES:
            - this is going to have the same result of using merge().

            merge() works poorly in a decoupled codebase, because the Observables aren't going to exist at the
            time the merge() would have been called.
         */

    }
}
