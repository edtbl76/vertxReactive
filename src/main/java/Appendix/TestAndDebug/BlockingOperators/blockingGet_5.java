package Appendix.TestAndDebug.BlockingOperators;

import com.google.common.collect.Lists;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class blockingGet_5 {

    @Test
    public void blockingGetTest() {


        List<Integer> resultInt = Arrays.asList(1, 2, 3, 4, 5);

        /*
            NOTE: I've disabled this inspection.
            This is an intellij warning for monadic behavior.

            If you look at the description, this specific case changes the actual behavior.
            The alternate solution (Java Streams) is an eager evaluated solution, while the Guava impl.
            is lazy evaluated.

            IMHO... Be careful when listening to the suggestions of your IDE.

            While apples are always apples, sometimes you feel like a Granny Smith.
         */
        //noinspection StaticPseudoFunctionalStyleMethod
        List<String> resultStr = Lists.transform(resultInt, String::valueOf);
        /*
            This is an observable of ints that we're turning into strings.
         */
        Observable<String> observable =
                Observable.fromIterable(resultInt)
                    .subscribeOn(Schedulers.io())
                    .map(String::valueOf);

        /*
            We want to create a list of Strings, so we map it to List.
            However, this changes our Reactive structure from:
                Observable<String> to Single<List>

            blockingFirst() and blockingSingle() don't work on Singles or Maybes, so we have to use blockingGet(),
            which is basically the same principle that extracts the data structures from singles.

            However, instead of thinking of "first" or "single", it gets ONLY.
            - I know it may seem like we are splitting hairs here, but in terms of underlying implementation these
            are all different things.

            While each of these "terms" might return a single result, there are different assertions that can/should
            be made.
         */
        List<String> list =
                observable
                       .toList().blockingGet();

        assertEquals(resultStr, list);
    }

}
