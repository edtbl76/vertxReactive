package Merging_10.Concatenation_2;

import io.reactivex.Observable;

import java.util.List;

public class ConcatIterable_7 {

    public static void main(String[] args) {

        Observable<String> first = Observable.just("ten", "little", "monkeys");
        Observable<String> last = Observable.just("no", "more", "monkeys");
        Observable<String> always = Observable.just("jumping", "on", "the", "bed");

        List<Observable<String>> observables = List.of(first, always, last, always);

        /*
            This is VERY self-explanatory.
            I've passed a list to concat(), which handles it as an Iterable, spewing out the streams in the order
            in which I provided them.


            The nice value of this is that you don't have to strip out the observables. concat() does this for you,
            which is one of the better examples of how you can use non-RX structures to your advantage :)
         */
        Observable.concat(observables)
                .subscribe(System.out::println);

    }
}
