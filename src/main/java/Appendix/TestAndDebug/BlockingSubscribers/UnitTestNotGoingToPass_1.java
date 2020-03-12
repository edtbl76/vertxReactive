package Appendix.TestAndDebug.BlockingSubscribers;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class UnitTestNotGoingToPass_1 {

    /*
        This won't pass... (Remember the keep alives??? Concurrency???? NO?!???!?!)

        the assertion fails because it is running on the main thread, but the Observable.interval() has pushed
        all of the events onto another thread.
     */
    @Test
    public void testBlockingSubscribe() {
        AtomicInteger count = new AtomicInteger();
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(5);

        observable.subscribe(l -> count.incrementAndGet());
        assertTrue(count.get() == 5);
    }
}
