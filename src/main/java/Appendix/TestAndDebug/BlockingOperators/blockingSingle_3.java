package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class blockingSingle_3 {

    /*
        blockingSingle()
        - stops calling thread
        - This only allows a single Event.

     */
    @Test
    public void testFirstEvent() {

        /*
            Observable that spits out one string event.
            - we'll pretend it's coming from a real IO source.
            - we capitalize it and swap it to a comp thread.
         */
        Observable<String> observable =
                Observable.just("Hello")
                        .subscribeOn(Schedulers.io())
                        .map(String::toUpperCase)
                        .observeOn(Schedulers.computation());

        /*
            This is similar to our previous example, but its unnecessary to filter it down, because we are only
            expecting a single event. If we get more than one... then it will fail.

         */
        String capturedByLength = observable.blockingSingle();

        assertEquals("HELLO", capturedByLength);
    }
}
