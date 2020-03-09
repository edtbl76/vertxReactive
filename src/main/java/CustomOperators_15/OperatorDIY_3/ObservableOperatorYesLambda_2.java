package CustomOperators_15.OperatorDIY_3;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

public class ObservableOperatorYesLambda_2 {

    public static void main(String[] args) {

        /*
            This is a consolidated, easier to read version of the previous example.
         */
        Observable.range(1, 10)
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain1] RCVD: " + integer));

        Observable.<Integer>empty()
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain2] RCVD: " + integer));
    }

    public static <T> ObservableOperator<T, T> doOnEmpty(Action action) {

        return observer -> new DisposableObserver<>() {

            boolean isEmpty = true;

            @Override
            public void onNext(T t) {
                isEmpty = false;
                observer.onNext(t);
            }

            @Override
            public void onError(Throwable throwable) {
                observer.onError(throwable);
            }

            @Override
            public void onComplete() {
                if (isEmpty) {
                    try {
                        action.run();
                    } catch (Exception e) {
                        onError(e);
                        return;
                    }
                }
                observer.onComplete();
            }
        };
    }
}
