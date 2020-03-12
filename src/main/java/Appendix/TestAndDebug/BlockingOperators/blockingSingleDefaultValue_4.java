package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class blockingSingleDefaultValue_4 {

    /*
        blockingSingle()
        - stops calling thread
        - This only allows a single Event.
     */
    @Test
    public void testFirstEvent() {

        /*
            Empty Observable
         */
        Observable<String> observable =
                Observable.<String>empty()
                        .subscribeOn(Schedulers.io())
                        .map(String::toUpperCase)
                        .observeOn(Schedulers.computation());

        /*
            like blockingFirst() we can also provide a default value to prevent an exception in case
            no events are provided.
         */
        String capturedByLength = observable.blockingSingle("world?");

        assertEquals("world?", capturedByLength);
    }
}
