package HandlingMultipleObservers_11.Multicasting_1;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class MulticastPipelineExample_6 {

    public static void main(String[] args) {

        /*
            A "preferred" example of multicasting.
            - This demonstrates a "Cold Observable that has remained in the fridge until it's time to feed the family"
            (I guess my analogy is that we have to feed more than one person.)

            Once it's time to feed the family, we call publish() to break it down into a single proxy observer to
            avoid redundant processing of the same data to yield the same result.
         */
        ConnectableObservable<Integer> connectableObservable =
                Observable.range(1, 5)
                .map(integer -> Generic.getRandom())
                .publish();


        /*
            Below, we have three separate observers doing 3 different things. THese operations are intended to yield
            separate results, so publish() has to be called before these events.
         */

        /*
            Observer 1 (maybe a UI, console, logger etc.)
            print each one
         */
        connectableObservable
                .subscribe(integer -> System.out.println("RCVD: " + integer));

        /*
            Observer 2 (sums them up. some sort of accumulator/result)
         */
        connectableObservable
                .reduce(0, (sum, next) -> sum + next)
                .subscribe(integer -> System.out.println("TOTAL: " + integer));

        /*
            Observer 3
                - get the largest. (I could also just dump it to a list and do min, max, etc.)
         */
        int highest = 0;
        connectableObservable
                .map(integer -> (integer >=  highest ? integer : highest))
                .subscribe(integer -> System.out.println("MAX: " + integer));

        // Do the connect dance
        connectableObservable.connect();



    }
}
