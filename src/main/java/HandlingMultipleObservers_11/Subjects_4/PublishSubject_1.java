package HandlingMultipleObservers_11.Subjects_4;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class PublishSubject_1 {

    public static void main(String[] args) {

        /*
            Creating a PublishSubject w/ create().
         */
        Subject<String> subject = PublishSubject.create();

        /*
            This determines a blueprint for the Observable.

            1.) map each event to it's length
            2.) onSubscribe,
                - print the event data onNext
                - print the stracktrace onError
                - print DONE onComplete.
         */
        subject.map(String::length).subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("DONE!")
        );

        /*
            Now we define the Observer itself.
            Here we push 3 events and declare an "EOL" w/ onComplete.
         */
        subject.onNext("tre");
        subject.onNext("four");
        subject.onNext("fives");
        subject.onComplete();
    }
}
