package Merging_10.Concatenation_2;

import io.reactivex.Observable;

public class ConcatMap_9 {

    public static void main(String[] args) {

        // Nothing special
        Observable<String> soup = Observable.just("abcdef", "ghijklm", "nopqrs", "tuvwxyz");

        /*
            There really isn't anything interesting about this example that we didn't already learn from
            merge().

            The only difference is that we are guaranteeing that insert order is emission order.

            As we've mentioned several times, this is more useful for data-driven "finite" workflows.
            Infinite, real-time reactive streams are better (only) served by flatMap()
         */
        soup
                .concatMap(
                        s -> Observable.fromArray(s.split("")))
                .subscribe(
                        s -> System.out.print(s.toUpperCase() + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );

    }
}
