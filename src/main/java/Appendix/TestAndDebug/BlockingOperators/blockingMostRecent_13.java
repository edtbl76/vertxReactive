package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import org.junit.*;

import java.util.concurrent.*;

public class blockingMostRecent_13 {

    @Test
    public void mostRecent() {

        /*
            Similar as previous:
            - infinite observable that will push up to 100 events (every 5 ms)
            (Half a second)
         */
        Observable<Long> observable = Observable.interval(5, TimeUnit.MILLISECONDS).take(20);

        /*
            blockingMostRecent()
            - works like blockingLatest()
                - replays the latest value repeatedly for every next() call
                - has a default value to provide if next() is called, but there are no available events to
                play/replay
         */
        Iterable<Long> iterable = observable.blockingMostRecent(-1L);

        iterable.forEach(System.out::println);
    }
}
