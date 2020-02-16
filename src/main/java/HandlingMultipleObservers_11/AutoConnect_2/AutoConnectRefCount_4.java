package HandlingMultipleObservers_11.AutoConnect_2;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class AutoConnectRefCount_4 {

    public static void main(String[] args) {

        /*
            We are using refCount here instead of autoconnect.
            Remember, the behavior here is that we do NOT persist connections between observers and "the proxy"
            once they are terminated/disposed.

         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .refCount();


        /*
            Observer 1 asks for 5 events, and sets a keep alive of 3 seconds.
            - we'll consume 3 events for this block
         */
        observable
                .take(5)
                .subscribe(l -> System.out.println("1: " + l));
        Generic.wait(3);

        /*
            Observer 2 asks for 2 events, and sets a keep alive of 3 seconds.
            - Observer 1 will consume it's remaining 2 events
            - Observer 2 will consume the same 2 events.
         */
        observable
                .take(2)
                .subscribe(l -> System.out.println("2: " + l));
        Generic.wait(3);


        // WE SHOULDN'T HAVE ANY MORE EVENTS.

        /*
            Since we used refCount(), Observer 3 was allowed to join the club and consume events for
            the duration of its keep alive (3 more seconds).

         */
        observable
                .subscribe(l -> System.out.println("3: " + l));
        Generic.wait(3);


        /*
            NOTE on results:

            Observer 1 will consume 0, 1, and 2 for the first "block"

            Observer 1 will consume 3 and 4 for the second "block"
            Since the same reactive stream is still active (i.e. not disposed), Observer 2 will consume the same
            data (3 and 4).

            Then, the Observers are done (based on the take() commands), so they reach onComplete() and are terminated)

            Then, observer 3 hit the alarm WAY late and the continental breakfast was completely closed. However,
            since we used refCount(), it was allowed to reconnect to the "proxy" Observer from publish() and
            start a new stream.

         */

    }
}
