package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

public class blockingForEach_10 {

    @Test
    public void testForEach() {

        /*
            Same as previous example. (Because we're trying to progressively solve the same problem with
            better (maybe) different approaches.

            Sheesh, you'd think that no one had ever read a book before.
         */
        Observable<Integer> observable = Observable.just(ThreadLocalRandom.current().nextInt(1, 10));


        /*
            This looks slightly different.

            The obvious scenario is that we're using a different operator:

                blockingForEach()


            1.) You'll note that we're just calling it. (i.e. there is no return type.)
                - naturally this is one way to get around the issue with Java's native forEach() implementation,
                which is to provide a way to do it that supports the reactive paradigm.

            2.) The part that I personally like about this is that we can pass the Consumer (the assert statement)
            into the lambda without the implementation becoming altogether too abstract.


         */
        observable.filter(integer -> integer > 5)
                .blockingForEach(integer -> assertTrue(integer > 5));



    }
}
