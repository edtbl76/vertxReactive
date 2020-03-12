package Appendix.TestAndDebug.BlockingSubscribers;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class UnitTestWillPass_2 {

    /*
        This passes!
        - This makes sense if you associate the problem presented here with the concurrency section we went through.
        blockingSubscribe() was one of the solutions we provided to keep threads alive in order for
        the events to be consumed.

        CAVEAT 1:
            - if you don't terminate the observable/flowable, the test will never end. We solve this here by
                calling take(5) (Get it??)

        CAVEAT 2:
            -  WE ARE BLOCKING! This is a great way to test certain paths within concurrent applications, but
            the fact that we are blocking makes this a non-asynchronous, which is one of the primary motives of
            reactive programming.

            This CAN (and SHOULD) be used in testing.
            This MAY occasionally in rare instances be used in production code to keep a thread alive. However, we
            discussed a number of other means of accomplishing it.. so be creative.
     */

    @Test
    public void testBlockingSubscribe() {
        AtomicInteger count = new AtomicInteger();
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).take(5);

        observable.blockingSubscribe(l -> count.incrementAndGet());
        assertTrue(count.get() == 5);
    }
}
