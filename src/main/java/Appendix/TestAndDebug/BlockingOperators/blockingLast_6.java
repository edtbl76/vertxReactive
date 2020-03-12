package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class blockingLast_6 {

    /*
        blockingLast()
        - stops calling thread
        - waits for last event to be completed. (regardless of what thread it is on)

     */
    @Test
    public void testLastEvent() {

        /*
            Observable that spits out two string events.
            - we'll pretend it's coming from a real IO source.
            - we capitalize the words
            - we swap over to a computation thread.
         */
        Observable<String> observable =
                Observable.just("Hello", "World")
                        .subscribeOn(Schedulers.io())
                        .map(String::toUpperCase)
                        .observeOn(Schedulers.computation());

        /*
            We filter out the results by length and we use blockingFirst() to return the last event in a non-rx way.
            - i.e. we get String instead of Observable<String>
         */
        String capturedByLength =
                observable
                        .filter(s -> s.length() == 5)
                        .blockingLast();

        assertEquals("WORLD", capturedByLength);
    }
}
