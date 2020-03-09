package CustomOperators_15.OperatorDIY_3;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

public class ObservableOperatorNoLambda_1 {

    public static void main(String[] args) {

        /*
            lift() is a special operator that takes an ObservableOperator (i.e. a customer Operator) and
            returns an Observable.

            THe first example here has events (so it shouldn't fire the custom code)
            The second example is deliberately empty (so it SHOULD fire the custom code)

         */


        Observable.range(1, 10)
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain1] RCVD: " + integer));

        Observable.<Integer>empty()
                .lift(doOnEmpty(() -> System.out.println("I Got Nothing")))
                .subscribe(integer -> System.out.println("[Chain2] RCVD: " + integer));
    }


    /*
        Here is our ObservableOperator. I want to dive into this, because we tend to abstract these details in our
        code.

        The signature is an ObservableOperator with the same type for our downstream and upstream events).
        We are creating our own "Action" operator (i.e. a DoOn). This actually exposes why they are called Action
        operators. The argument is an Action, which is an RxJava interface similar to a Runnable that allows you to
        throw a checked exceptions.
     */
    public static <T> ObservableOperator<T, T> doOnEmpty(Action action) {

        /*
            The entire function is encapsulated in this return statement.
            This means we can consolidate the code/impl into a lambda.
         */
        return new ObservableOperator<T, T>() {

            /*
                Since ObservableOperator is an interface, we have to provide an implementation of its apply()
                method.
                    - the return type is the downstream Observer (its wildcard bounded type is derived from
                    ObservableOperator's second "T" type
                    - the parameter is the upstream Observer (its wildcard bounded type is derived from
                    ObservableOperator's first "T" type
             */
            @Override
            public Observer<? super T> apply(Observer<? super T> observer) {
                /*
                    apply() automatically returns a new DisposableObserver for us.
                 */
                return new DisposableObserver<>() {

                    /*
                        We've added this to assume that our operator will be fired by default.
                     */
                    boolean isEmpty = true;

                    /*
                        if OnNext() is called, it means that we've received an event. This tells us that
                        the Observable was NOT empty.
                        - We toggle our status switch
                        - Then we call the onNext() method of the observer we were provided in order to
                        "pass-through"
                     */
                    @Override
                    public void onNext(T t) {
                        isEmpty = false;
                        observer.onNext(t);
                    }

                    /*
                        onError is strictly passthrough.
                     */
                    @Override
                    public void onError(Throwable throwable) {
                        observer.onError(throwable);
                    }

                    /*
                        If we received events, our toggle is disabled, so this is strictly passthrough.
                        However, if we didn't receive events, then the default state is ON.

                        This is where our Action takes over

                        We are using print statements, so lambdas are fairly easy to implement. For custom actions
                        with more logic you might need to create a separate impl to override run

                        Action action = new Action() {

                            @Override
                            public void run() throws Exception {
                                //
                            }
                        }

                     */
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
        };
    }
}
