package Appendix.TestAndDebug.TestClasses;

import io.reactivex.*;
import io.reactivex.observers.*;
import io.reactivex.schedulers.*;
import org.junit.*;

import java.util.concurrent.*;

public class TestSchedulerExample {

    @Test
    public void scheduler() {

        /*
            Create a test scheduler and a test observer.
         */
        TestScheduler scheduler = new TestScheduler();
        TestObserver<Long> testObserver = new TestObserver<>();

        /*
            set up an annoying Observable that will emit an event every hour.
         */
        Observable<Long> hourly  = Observable.interval(1, TimeUnit.HOURS, scheduler);


        /*
            subscribe, passing our testObserver.
         */
        hourly.subscribe(testObserver);

        /*
            fast forward 45 minutes and assert that nothing has happened yet
         */
        scheduler.advanceTimeBy(45, TimeUnit.MINUTES);
        testObserver.assertValueCount(0);

        /*
            advance time to  61 minutes, (i.e. 1 minute after the first event)
         */
        scheduler.advanceTimeTo(61, TimeUnit.MINUTES);
        testObserver.assertValueCount(1);

        /*
            go to a day
         */
        scheduler.advanceTimeTo(24, TimeUnit.HOURS);
        testObserver.assertValueCount(24);




    }
}
