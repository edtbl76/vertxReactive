package HandlingMultipleObservers_11.Multicasting_1;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;


public class MulticastingWithOperatorBadly_4 {

    public static void main(String[] args) {

        /*
            I've already showed how to separate the statements, so now I'm doing the one-liner.
            It is important to remember that the multicasting is happening HERE.

            publish() creates the consolidated stream consistent with multicasting.
         */
        ConnectableObservable<Integer> ints = Observable.range(1, 5).publish();

        /*
            Now we'll use the map() operator to randomize them.

            Yes.. I separated these for a reason. Most examples create a one liner statement to abstract the effects,
            but by separating the map() statement from our ConnectableObservable it foreshadows the end result.
            (HINT.. look at the return type, and remember how it behaves in context of multiple Observers)
         */
        Observable<Integer> randomInts = ints.map(integer -> Generic.getRandom());

        // create a few observers...
        randomInts.subscribe((integer -> System.out.println("Obby Server 1: " + integer)));
        randomInts.subscribe((integer -> System.out.println("Obby Server 2: " + integer)));


        // connect our observers
        ints.connect();

        Generic.wait(5);

        /*
           RESULT NOTES:
           - if you execute this code, you'll see that the two observers are interleaved (consistent with
           concurrent emissions)

           - we get different random numbers for each observer, which means we still have two instances of map().

           So we can deduce (correctly) the following>

           1.) We created a ConnectableObservable w/ it's trusty friend publish(), which is responsible for
           collapsing multiple reactive streams down into one.

           2.) we then used the map() operator.
                - normally, this might be chained off of the ConnectableObservable, obfuscating the fact that the
                operator returns an OBSERVABLE.
                - the default behavior of an Observable is to push out a separate stream for each Observer.
                - in a nutshell we've undone the handiwork of our publish() statement at this point.
                - map() splits out a separate Observable stream for each Observer...


            STILL NO DICE.

         */

    }
}
