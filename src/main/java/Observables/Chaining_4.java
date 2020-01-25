package Observables;

import io.reactivex.Observable;

import java.util.stream.Stream;

public class Chaining_4 {

    public static void main(String[] args) {

        Stream<String> beatles = Stream.of("George", "John", "Paul", "Ringo");


        /*
            Observable, nothing special.
         */
        Observable<String> observable = Observable.create(observableEmitter -> {
            try {
                beatles.forEach(observableEmitter::onNext);
                observableEmitter.onComplete();
            } catch (Throwable throwable) {
                observableEmitter.onError(throwable);
            }
        });

        /*
            The observer becomes the end of the chain of operators in this case.
         */
        observable
                .map(String::length)
                .filter(integer -> integer < 5)
                .subscribe(System.out::println);

    }
}
