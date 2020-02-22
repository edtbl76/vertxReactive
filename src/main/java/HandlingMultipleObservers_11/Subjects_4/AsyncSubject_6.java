package HandlingMultipleObservers_11.Subjects_4;

import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;

public class AsyncSubject_6 {

    public static void main(String[] args) {

        /*
            AsyncSubject factory
         */
        Subject<String> subject = AsyncSubject.create();

        /*
            First Observer set up.
         */
        subject.subscribe(
                s -> System.out.println("NomNom 1: "+ s),
                Throwable::printStackTrace,
                () -> System.out.println("YUM!")
        );
        /*
            Things to eat.

            Once onComplete() is called, our first observer is going to consume the the 3rd event and onComplete.

         */
        subject.onNext("Brussel Sprouts");
        subject.onNext("Asparagus");
        subject.onNext("Grilled Mushrooms & Onions");
        subject.onComplete();


        /*
            second subscriber happens after the fact.
         */
        subject.subscribe(
                s -> System.out.println("NomNom 2: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("YUM!")
        );


    }
}
