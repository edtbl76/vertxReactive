package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class Flowable_1 {

    public static void main(String[] args) {

        /*
            The only thing I did here was replace our Observable with a Flowable. (Depending on how your IDE is
            configured, you might notice that the output of each operator is likewise a flowable instead of an
            observable, suggesting that the entire chain is built of flowables.

            The results are MUCH more civilized.
                - you'll note that the Flowables are managed in batches.
                    - we generate some events, just like before.
                    - the consumer doesn't wait to be blown over by an onslaught. Instead it tells the Flowable
                    to slow down(or stop as the case is), and it consumes (completes!) a batch. This frees up
                    memory as well as threads (depending on how you have everything set up).

            While it still takes a long time for this example to complete, it completes without overloading the
            consumer, and most importantly, events are being processed or moved forward with less of a delay per
            event, creating a better end user experience.

         */
        Flowable.range(1, 100_000_000)
                .map(ThisEvent::new)
                .observeOn(Schedulers.io())
                .subscribe(thisEvent -> {
                    Generic.waitMillis(25L);
                    System.out.println("\t\t\t[" + Thread.currentThread().getName() + "]RCVD: " + thisEvent.getEventId());
                });

        Generic.wait(120);
    }

    public static final class ThisEvent {
        int eventId;

        ThisEvent(int eventId) {
            this.eventId = eventId;
            System.out.println("[" + Thread.currentThread().getName() + "]Generated Event [" + eventId + "]");
        }

        public int getEventId() {
            return eventId;
        }
    }
}
