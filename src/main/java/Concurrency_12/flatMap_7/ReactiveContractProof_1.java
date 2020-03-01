package Concurrency_12.flatMap_7;

import Utils.Generic;
import io.reactivex.Observable;

import java.time.LocalTime;

public class ReactiveContractProof_1 {

    public static void main(String[] args) {

        /*
            Before we get into the deeper examples, let's revisit some concepts from earlier.

            our map() operator is going to emulate a random computation that lasts between 1 and 3 seconds.

            Our consumer is going to spit out the time as well as the data, so we can witness the reactive
            contract in action. Each event blocks all subsequent operations.

            While the results vary each time you execute this (because we are using random values), in the real world
            computation time might depend on more predictable scenarios such as the size of data in the event, etc.

            Let's say that the first event takes 5 seconds and the second event takes 1 second.

            - In this model, the second event actually takes 6 seconds, 5 seconds waiting for the first event to complete
            and then 1 second for its own processing.

            This is due to the nature of the reactive contract. This is a clear demonstration of how some events
            spend idle time waiting to be processed.

         */
        Observable.range(1, 10)
                .map(integer -> {
                    Generic.wait(Generic.getRandom(1, 3));
                    return integer;
                })
                .subscribe(integer -> System.out.println("RCVD: " + integer + " at " + LocalTime.now()));


        /*
            RESULT NOTES:

            Let's think about the problem that we are observing here.

            We have 10 events, that can each take between 1 and 3 seconds. This means that our total processing time
            ranges between 10 and 30 seconds.

            Assuming a worst case scenario, if each event belongs to a separate customer, then the last event
            spends 27 seconds waiting, and 3 seconds at work. This is (N - 1) times T duration of wastage (where N is the
            number of events and T is the longest processing time for a given event in the chain.

            If we could do all of these things at once (in parallel) then the worst case scenario for  time spent
            processing is the duration of the slowest event processing period.

            This is a considerable improvement, because (all things being equal) events don't spend any time waiting.
            (Practically speaking there is a limit to the number of events that can be processed simultaneously, but
            as discussed in previous sections, the time spent idling is reduced by an order of magnitude, and therefore
            grows much slower).


         */
    }
}
