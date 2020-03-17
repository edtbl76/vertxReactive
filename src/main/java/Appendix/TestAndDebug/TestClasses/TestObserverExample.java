package Appendix.TestAndDebug.TestClasses;

import io.reactivex.Observable;
import io.reactivex.observers.*;
import org.junit.*;

import java.util.concurrent.*;

public class TestObserverExample {

    @Test
    public void testObserver() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).take(8);

        TestObserver<Long> testObserver = new TestObserver<>();

        testObserver.assertNotSubscribed();

        observable.subscribe(testObserver);
        testObserver.assertSubscribed();

        testObserver.awaitTerminalEvent();
        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(8);
        testObserver.assertValues(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L);
    }
}
