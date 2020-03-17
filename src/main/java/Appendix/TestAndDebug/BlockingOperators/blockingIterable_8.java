package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

public class blockingIterable_8 {

    @Test
    public void testIter() {

        /*
            I've created a random stream of Integer events.
         */
        Observable<Integer> observable = Observable.just(ThreadLocalRandom.current().nextInt(1, 10));

        /*
           We're doing our processing here:
           - whittle the stream down to events larger than 5, and then skip out of our Rxable code using the
           blockingIterable().

           NOTE:
            - the iterable continues to enqueue events until the Iterator can consume them.
            - For large/infinite workflows, this leads to OOM.
         */
        Iterable<Integer> greaterThan5 = observable.filter(integer -> integer > 5)
                .blockingIterable();

        /*
            Our passing assertion.
         */
        greaterThan5.forEach(integer -> assertTrue(integer > 5));
    }
}
