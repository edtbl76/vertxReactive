package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.*;
import org.junit.*;

import java.util.concurrent.*;

public class blockingLatest_12 {

    @Test
    public void latest() {

        /*
            Similar as last example.
                - infinite observable that "should" result in 600 events
         */
        Observable<Long> observable = Observable.interval(1, TimeUnit.MICROSECONDS).take(600);

        /*
            blockingLatest()
            - returns an iterable
            - works similar to blockingNext()
                - instead of waiting for the next value it requests the last successful event
                - ignored all of the values it missed between the last successful event and the latest
                event.
         */
        Iterable<Long> iterable = observable.blockingLatest();

        iterable.forEach(System.out::println);

    }
}
