package HandlingMultipleObservers_11.Multicasting_1;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class WarmingThingsUpWithMulticasting_2 {

    public static void main(String[] args) {

        /*
            Yes - this can be done all in one statement... however I deliberately did it this way as a reminder
            that it is more likely that we'd be taking a cold

            Otherwise:
                    ConnectableObservable<Integer> blah = Observable.range(start, count).publish();
         */
        Observable<Integer> ints = Observable.range(1, 9);
        ConnectableObservable<Integer> multicastInts = ints.publish();


        /*
            Only four observers this time. Like the newspaper, subscription is down.
         */
        multicastInts.subscribe(integer -> System.out.println("Subscriber 1: " + integer));
        multicastInts.subscribe(integer -> System.out.println("Subscriber 2: " + integer));
        multicastInts.subscribe(integer -> System.out.println("Subscriber 3: " + integer));
        multicastInts.subscribe(integer -> System.out.println("Subscriber 4: " + integer));

        /*
            Nothing happens until we call connect to multicast all of the events.

            This time the results are a bit more civilized for each client.
            This spits out each event simultaneously to all subscribers, one at a time.

            So let's review the comparison.
         */
        multicastInts.connect();
    }
}
