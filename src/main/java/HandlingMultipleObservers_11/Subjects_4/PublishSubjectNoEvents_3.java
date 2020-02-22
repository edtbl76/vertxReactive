package HandlingMultipleObservers_11.Subjects_4;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class PublishSubjectNoEvents_3 {

    public static void main(String[] args) {

        /*
            Creating a PublishSubject w/ create().
         */
        Subject<String> subject = PublishSubject.create();

        /*
            Define events to be pushed BEFORE we subscribe.
         */
        subject.onNext("You");
        subject.onNext("can't");
        subject.onNext("see");
        subject.onNext("me");
        subject.onComplete();

        /*
            Order matters.
            - The events above have already happened by the time we connect operators and an Observer to
            our subject.
            - as a result, we just get an empty Observable.
         */
        subject.map(String::length).subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("DONE!")
        );


    }
}
