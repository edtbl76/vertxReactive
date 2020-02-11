package Merging_10.Zippy_4;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZipIterable_5 {

    public static void main(String[] args) {

        /*
            The first three lines are the same as the previous example.
         */
        Observable<String> words = Observable.just("one", "two", "three");
        Observable<String> numbers = Observable.just("1", "2", "3");
        List<Observable<String>> observables  = Arrays.asList(words, numbers);

        /*
           This is STILL ugly.

           zipIterable() offers two parameters that are very helpful.

           delayError allows the delay of errors until all ObserableSources terminate.
           - the normal behavior of zip() is to terminate IMMEDIATELY, which would result in the disposal of
           events that you otherwise want to be consumed.

           bufferSize is the number of events to "prefetch" from each Observable. This is exactly what you think it
           does. It stores the data in "local" storage/caching so it can be consumed/matched very quickly, giving you
           a head start at doing any cache catch up)
         */
        Observable.zipIterable(
                observables,
                obj -> Stream.of(obj).map(o -> (String) o).collect(Collectors.toList()),
                true,
                10)
                .subscribe(System.out::println, Throwable::printStackTrace, System.out::println);
    }
}
