package Appendix.TestAndDebug.BlockingOperators;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

public class blockingIterableTryFinally_9 {

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
            - In the previous example I mentioned the potential for OOM errors because reactive streams use
            an unbounded queue by default. This is exacerbated by forEach() which was written with " extreme
            flexibility " surrounding the nature of NOT handling


            (see below)

         */
        Iterable<Integer> greaterThan5 = observable.filter(integer -> integer > 5)
                .blockingIterable();

        /*
            This is a semi-workaround for the above case.
            If we cast the Iterable to a Disposable (or Cancellable for Flowable types) we can clean up our threads.

         */
        try {
            greaterThan5.forEach(integer -> assertTrue(integer > 5));
        } finally {
            Disposable disposable = (Disposable)greaterThan5;
            disposable.dispose();
        }

    }
}
