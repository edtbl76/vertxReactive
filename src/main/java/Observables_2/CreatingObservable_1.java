package Observables_2;

import io.reactivex.Observable;

public class CreatingObservable_1 {

    public static void main(String[] args) {

        /*
            Here is an Observable that we have created.
            We are providing WHAT TO DO onNext() and onComplete(). This is just a peek at under the covers. Don't use
            this as an example of how to actually create them...


         */
        Observable<String> numeros = Observable.create(
                observableEmitter -> {
                    observableEmitter.onNext("Uno");
                    observableEmitter.onNext("Dos");
                    observableEmitter.onNext("Tres");
                    observableEmitter.onNext("Quattro");
                    observableEmitter.onComplete();
                });

        /*
            This is the Observer.
            The callback/closure is more clear here because we are using a lambda rather than a lazy method expression.
         */
        numeros.subscribe(s -> System.out.println("RCVD: " + s));
    }

}
