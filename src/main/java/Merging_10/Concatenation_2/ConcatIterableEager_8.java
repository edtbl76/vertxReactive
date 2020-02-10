package Merging_10.Concatenation_2;

import io.reactivex.Observable;

import java.util.List;

public class ConcatIterableEager_8 {

    public static void main(String[] args) {

        Observable<String> first = Observable.just("ten", "little", "monkeys");
        Observable<String> last = Observable.just("no", "more", "monkeys");
        Observable<String> always = Observable.just("jumping", "on", "the", "bed");

        List<Observable<String>> observables = List.of(first, always, last, always);

        /*
            There is only ONE difference between this and the example before it...

            we are using concatEager().

            The difference between this and concatEager is that it allows the sources to be read in concurrently,
            but order is maintained, so that values are cached until it is their turn to be spit out.

            (standard concat has to READ THEM IN ORDER TOO)
         */
        Observable.concatEager(observables)
                .subscribe(System.out::println);

    }
}
