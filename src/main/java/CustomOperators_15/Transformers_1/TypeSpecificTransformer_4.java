package CustomOperators_15.Transformers_1;

import Utils.Generic;
import com.google.common.base.Splitter;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class TypeSpecificTransformer_4 {

    public static void main(String[] args) {


        /*
            This is an observable generator that does the following:
                - we randomly emit the numbers 0-9
                - We map the numbers to strings
                - we cache them 10 at a time and flatMap them back down to a single by concatenating them into a
                    10 digit string.
                - chop off anything that starts with 0.
         */
        Observable<String> phoneNumbers = Observable.generate(
                emitter -> emitter
                        .onNext(ThreadLocalRandom.current().nextInt(0, 10)))
                .subscribeOn(Schedulers.computation())
                .doOnNext(o -> System.out.println("[" + Thread.currentThread().getName() + "] - Generating - " + o))
                .map(Object::toString)
                .window(10)
                .flatMapSingle(
                        stringObservable -> stringObservable
                            .reduce("", (conc, next) -> conc + next))
                .doOnNext(o -> System.out.println("[" + Thread.currentThread().getName() + "] - Reduced - " + o))
                .filter(s -> !s.startsWith("0"));


        /*
            Here is our Flowable of separators (we could use an Observable here if we wanted....)
            This takes in separators that will be inserted into our numbers for whatever reason. Maybe we're

            This uses out flatMap pattern to batch our stream of numbers against each separator.

         */
        Observable.fromIterable(Arrays.asList(".", "-", " "))
                .flatMap(s -> phoneNumbers
                        .compose(format(s)))
                .subscribe(System.out::println);

        Generic.wait(10);



    }

    /*
        This is a bit more involved than the previous example.

        Rather than accepting generic input, this will only allow Integer events, which makes this type
        specific.

        The first thing we do is create a Guava splitter so we can break this down into phone numbers (I
        picked that at random).

        - The initial input is a 10 our ten digit number
        - We flat map this down into a new flowable of events resulting from the Guava split
        - we place a window on the result (to cache in batches of 3)
        - we flatMapSingle the result which builds our strings from the separators and split numberstrings.

     */
    private static ObservableTransformer<String, String> format(String separator) {
        Splitter splitter = Splitter.fixedLength(3);
        return observable -> observable
                .doOnNext(s -> System.out.println("[" + Thread.currentThread().getName() + "] - Transforming " + s))
                .observeOn(Schedulers.trampoline())
                .flatMap(s ->
                    Observable.fromIterable(splitter.limit(3).split(s))
                )
                .window(3)
                .flatMapSingle(
                        stringObservable -> stringObservable
                                .collect(StringBuilder::new, (o, integer) -> {
                                    if (o.length() == 0) {
                                        o.append(integer);
                                    } else {
                                        o.append(separator).append(integer);
                                    }
                                })
                                .map(StringBuilder::toString)

                );

   }

}
