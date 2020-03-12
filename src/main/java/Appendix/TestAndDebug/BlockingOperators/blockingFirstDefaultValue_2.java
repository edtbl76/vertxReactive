package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class blockingFirstDefaultValue_2 {

    @Test
    public void testFirstEvent() {

        /*
            Same example as before, but there aren't any events.

         */
        Observable<String> observable =
                Observable.<String>empty()
                        .subscribeOn(Schedulers.io())
                        .map(String::toUpperCase)
                        .observeOn(Schedulers.computation());

        /*
            If blockingFirst doesn't get any events, it throws an exception.
            - Here we've provided a default item that passes the test.
         */
        String capturedByLength =
                observable
                        .filter(s -> s.length() == 5)
                        .blockingFirst("GotNothing");

        assertEquals("GotNothing", capturedByLength);
    }
}
