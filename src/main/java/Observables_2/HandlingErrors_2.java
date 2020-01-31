package Observables_2;

import io.reactivex.Observable;

import java.util.stream.IntStream;

public class HandlingErrors_2 {

    public static void main(String[] args) {

        /*
            An even SMARTER observable that can swallow an exception and pass it up the chain to the observer as a
            Throwable.
         */
        Observable<Integer> nums = Observable.create(observableEmitter -> {
           try {
               /*
                    Tidier way of creating a stream of numbers to be created. Typing onNext() more than once is
                    extremely annoying.
                */
               IntStream.rangeClosed(1, 10).forEach(observableEmitter::onNext);
               observableEmitter.onComplete();
           } catch (Throwable throwable) {
               observableEmitter.onError(throwable);
           }
        });


        /*
            This is an even smarter Observer... which can handle the throwable that we passed up the chain.
         */
        nums.subscribe(s ->
            System.out.println("RCVD: " + s),
            Throwable::printStackTrace
        );
    }
}
