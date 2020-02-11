package HandlingMultipleObservers_11.Multicasting_1;

import io.reactivex.Observable;

public class RevistingTheCold_1 {

    public static void main(String[] args) {

        // Range factory. Remember me?
        Observable<Integer> ints = Observable.range(1, 10);

        /*
            Multiple Observers (Cold Observables) result in each event being replayed by ALL Observers.
            - This is a data driven/finite-data set workflow.

            As the wiki suggested, this is the default case:
            - we created 5 streams (1 for each observer).

            This happens sequentially (Reactive Contract!)
            - Based on the default case, each Observer is treated serially, such that event 1 - 10 is pushed via
            onNext(), then onComplete() is called.
            - this entire process is repeated...sequentially for each Observer.

            This is slow. If I have 100 clients, the last one in line is going to die of boredom by the time it
            consumes any data.
         */
        ints.subscribe(integer -> System.out.println("Observer 1: " + integer));
        ints.subscribe(integer -> System.out.println("Observer 2: " + integer));
        ints.subscribe(integer -> System.out.println("Observer 3: " + integer));
        ints.subscribe(integer -> System.out.println("Observer 4: " + integer));
        ints.subscribe(integer -> System.out.println("Observer 5: " + integer));
    }
}
