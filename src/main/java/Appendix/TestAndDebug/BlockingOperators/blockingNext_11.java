package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import org.junit.*;

import java.util.concurrent.*;

public class blockingNext_11 {

    @Test
    public void blockingTheNext() {

        /*
            Infinite observable that should in theory spit out 500 events.
         */
        Observable<Long> observable =
                Observable.interval(1, TimeUnit.MICROSECONDS).take(500);

        /*
            BlockingNext()
            - returns an iterable
            - blocks each iterator's next() until the next value is provided.
                NOTE:
                    - any events that arrive
                        - after the last successful next() but
                        - before the current next()
                        - ARE IGNORED
         */
        Iterable<Long> iterable = observable.blockingNext();

        iterable.forEach(System.out::println);




    }
}

