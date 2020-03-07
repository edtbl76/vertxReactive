package Flowables_14.ObservableReview_1;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class OverwhelmedObserver_2 {

    public static void main(String[] args) {

        /*
            I had to up the ante a bit. Here is 100 million events, creating 100 million new objects.
            However, we are doing this asynchronously now, which means that we'll continue to generate new
            objects without waiting for the Observer to consume them.

            At about 16 million events Generated on the main thread, the fans on my 12 proc laptop are spinning like

            I placed the tabs in the output so the RCVD will be easier to spot.

                This is a snippet from my output showing us how far behind the consumer is.

                [main]Generated Event [5089721]
			        [RxCachedThreadScheduler-1]RCVD: 629
                [main]Generated Event [5089722]

            Our consumer is sufficiently overwhelmed by the tidal wave of events we have created. In fact, it is so
            far behind, it's possible that the system is going to run out of memory.
         */
        Observable.range(1, 100_000_000)
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
