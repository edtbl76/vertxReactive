package Dispose_8;

import io.reactivex.Observable;

import java.util.stream.IntStream;


public class LongRunningObservables_4 {

    /*
        This runs forever... because there is nothing to capture it or stop it. Blah!
     */
    public static void main(String[] args) {

        Observable<Integer> observable = Observable.create(observableEmitter -> {
            try {

                IntStream.rangeClosed(1, 10_000).forEach(
                        i -> {
                            while(!observableEmitter.isDisposed())
                                observableEmitter.onNext(i);

                            if (observableEmitter.isDisposed())
                                return;
                        }
                );

                observableEmitter.onComplete();
            } catch (Throwable throwable) {
                observableEmitter.onError(throwable);
            }
        });


        observable.subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Done!")
        );
    }



}
