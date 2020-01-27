package Observables;

import io.reactivex.Observable;

public class DeferFactory_14 {

    private static int start = 1;
    private static int count = 5;

    public static void main(String[] args) {


        /*
            In the first example,
                -  we introduce a state change between the definition of the two observers.

            However, by default, Observables are eager loaded, which means the observable has really already been
            created. This means that the state change (count = 10) will only be picked up by NEWLY created observable
            objects
         */
        withoutDefer();

        /*
            This is the power of defer(). Defer allows observables to be lazy loaded whenever the subscriber is
            defined. In other words, a NEW OBSERVABLE is created each time subscribe() is called.

            - As a result, the state change that was made between the calls is now going to be recognized by the
            second observer.

            (THIS IS ALSO A GOOD FIX FOR OBSERVABLE SOURCES THAT REUSE ITERATORS THAT ONLY ITERATE DATA ONCE)
         */
        withDefer();

    }

    private static void withoutDefer() {
        System.out.println("Without Defer: ");

        Observable<Integer> observable = Observable.range(start, count);

        observable.subscribe(l -> System.out.println("Observer One: " + l));

        // STATE CHANGE INTRODUCED, but the second subscription isn't going to pick it up.
        count = 10;
        observable.subscribe(l -> System.out.println("Observer Two: " + l));

        System.out.println("\n");
    }

    private static void withDefer() {
        System.out.println("With Defer: ");

        count = 5;
        Observable<Integer> observable = Observable.defer(() -> Observable.range(start, count));

        observable.subscribe(l -> System.out.println("Observer One: " + l));

        // STATE CHANGE INTRODUCED, but the second subscription isn't going to pick it up.
        count = 10;
        observable.subscribe(l -> System.out.println("Observer Two: " + l));

    }
}
