package CustomOperators_15.OperatorDIY_3;

import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscriber;

import java.util.concurrent.Flow;

public class FlowableOperator_4 {

    public static void main(String[] args) {

        /*
           This is a "flowable" version of ObservableOperatorYesLambda.

           You'll note the only difference in the source objects is that they are Flowables.
         */
        Flowable.range(1, 10)
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain1] RCVD: " + integer));

        Flowable.<Integer>empty()
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain2] RCVD: " + integer));
    }

    /*
        The syntax between ObservableOperator and FlowableOperator are almost identical.

        1.) we use a FlowableOperator as the return type (The type args are the same)
        2.) we return a subscriber instead of an Observer
        3.) we instantiate a DisposableSubscriber instead of a DisposableObserver

        All else are the same.
     */
    public static <T> FlowableOperator<T, T> doOnEmpty(Action action) {

        return subscriber -> new DisposableSubscriber<>() {

            boolean isEmpty = true;

            @Override
            public void onNext(T t) {
                isEmpty = false;
                subscriber.onNext(t);
            }

            @Override
            public void onError(Throwable throwable) {
                subscriber.onError(throwable);
            }

            @Override
            public void onComplete() {
                if (isEmpty) {
                    try {
                        action.run();
                    } catch (Exception e) {
                        onError(e);
                    }
                }
                subscriber.onComplete();

            }
        };
    }
}
