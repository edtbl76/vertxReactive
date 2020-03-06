package Flowables_14;

import Utils.Generic;
import io.reactivex.Observable;

public class Introduction_1 {

    public static void main(String[] args) {

        /*
            range() factory creates 10 events.
            - the first thing we do is instantiate an object via the map() operator.
                - each time we instantiate an object, the constructor prints a statement that the event is being
                created
            - then we consume the event we've created. We wait 1 second (assuming processing time) and
            we print out that we received it.

            (NOTE: at 5 ms, we'll get 200 events per second. At 4k events, this will run for 20 seconds)
            I didn't think we needed a crazy payload to demonstrate the chain of events.
         */
        Observable.range(1, 4000)
                .map(ThisEvent::new)
                .subscribe(thisEvent -> {
                    Generic.waitMillis(5L);
                    System.out.println("RCVD: " + thisEvent.getEventId());
                });
    }

    public static final class ThisEvent {
        int eventId;

        ThisEvent(int eventId) {
            this.eventId = eventId;
            System.out.println("Generated Event [" + eventId + "]");
        }

        public int getEventId() {
            return eventId;
        }
    }
}
