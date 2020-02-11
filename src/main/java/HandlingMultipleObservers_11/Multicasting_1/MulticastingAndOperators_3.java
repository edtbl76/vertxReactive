package HandlingMultipleObservers_11.Multicasting_1;

import Utils.Generic;
import io.reactivex.Observable;

public class MulticastingAndOperators_3 {

    public static void main(String[] args) {

        /*
            As the wiki suggests, all of our hard work might be undone by inserting operators that split out chains
            after our publish() call.... like below...

         */
        Observable<Integer> randomInts = Observable.range(1, 5)
                .map(integer -> Generic.getRandom());

        /*
            Down to 2 subscribers. Print is dead.
         */
        randomInts.subscribe(integer -> System.out.println("Observer 1: " + integer));
        randomInts.subscribe(integer -> System.out.println("Observer 2: " + integer));


        /*
            Look at the output. This looks like a run-of-the-mill Cold Observable.
            Unfortunately, the differences aren't immediately apparent if you don't know what is going on under the
            hood.

            1.) The Observable Factory kicks off a stream. We have 2 subscribe() methods, so we generate 2 streams.
            2.) Each stream has its own instance of the map() operator.

                (This is why each Observer gets DIFFERENT random numbers...)
            3.) Each Observer chews up its own personal stream.

                                Observable
                                /       \
                          Stream 1      Stream 2
                              |             |
                          Map 1         Map 2
                            |               |
                          Observer 1    Observer 2


                ya dig?
         */
    }
}
