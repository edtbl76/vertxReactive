package HandlingMultipleObservers_11.Multicasting_1;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class MulticastingWithOperatorProperly_5 {

    public static void main(String[] args) {

        /*
            publish() has to be the LAST operation that happens to preserve the "hotness"
            of our observable

            1.) create an Observable using one of the myriad of happy factories we have to stream events.
            2.) perform X number of operators to kaflubblerate our events as needed.
            3.) Create a ConnectableObservable and publish a proxy Observer (which is the streams consolidation
            action)
         */
        ConnectableObservable<Integer> connectableObservable =
                Observable.range(1, 8)
                .map(integer -> Generic.getRandom())
                .publish();


        /*
            Now we have to create our Observers.
            Since this is a properly multicast operator, we'll pretend it's the revival of print on the web, so
            it has seen a resurgence of subscriptions.

            - This is my github account. If I want to make bad jokes here, I'm allowed to.

         */
        connectableObservable.subscribe(integer -> System.out.println("Uno     : " + integer));
        connectableObservable.subscribe(integer -> System.out.println("Dos     : " + integer));
        connectableObservable.subscribe(integer -> System.out.println("Tres    : " + integer));
        connectableObservable.subscribe(integer -> System.out.println("Quattro : " + integer));

        /*
            Connect our observers to the consolidated stream.
         */
        connectableObservable.connect();

        Generic.wait(8);


        /*
            NOTE ON RESULT:
                - This final result has achieved what we wanted.

                The random number selected in each event is properly DISTRIBUTED across all observers.
                This is a VERY important concept in distributed applications that have to manage concurrency.

                It is very easy to ignore this concept and end up with inconsistent (or flat out WRONG) data
                because we've failed to consider the order in which data is processed vs. the order in which data is sent.

                The same data elements are being sent to each of the consuming Observers, and our system now has
                the proper consistency expected.
         */




    }
}
