package Observables_2;

import io.reactivex.Observable;

import java.util.stream.Stream;

public class ProcessingInTheMiddle_3 {

    public static void main(String[] args) {

        /*
            Here is... ANOTHER.. Observable
         */
        Stream<String> stream = Stream.of("this", "stream", "bytes");
        Observable<String> observable = Observable.create(observableEmitter -> {
            try {
                stream.forEach(observableEmitter::onNext);
                observableEmitter.onComplete();
            } catch (Throwable throwable) {
                observableEmitter.onError(throwable);
            }
        });

        /*
            Let's do some processing (One at a time first just to be annoying)
            - Actually the point is to demonstrate that the return type of most operators are some form of
            Observable. This means that they can be CHAINED together.

            Step 1.) Convert each string into its character length
            Step 2.) filter out anything that is greater than 4.

            YES. THESE ARE OBSERVABLES. This means that each one has an onNext, OnComplete and OnError function internally

         */
        Observable<Integer> stringLengths = observable.map(String::length);
        Observable<Integer> lessThanFive = stringLengths.filter(integer -> integer < 5);

        /*
            Observer that does something with what is left
            - based on the input above.. only one will actually print.
         */
        lessThanFive.subscribe(s -> System.out.println("RCVD: " + s));
    }
}
