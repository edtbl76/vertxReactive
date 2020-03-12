package CustomOperators_15.Transformers_1;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableTransformer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class CompletableTransformer_10 {

    public static void main(String[] args) {

        /*
            Overriding the CompletableObserver and using it to provide the output for our examples.
         */
        CompletableObserver completableObserver = new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("Completable Subscription Has Occurred");
            }

            @Override
            public void onComplete() {
                System.out.println("\tYou complete me!");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        };


        // We have two Singles with data that we want to convert to completables.
        Completable hasData = Single.just("I'm Complete!")
                .ignoreElement()
                .compose(addActionBeforeResult());

        Completable hasError = Single.error(new Exception("You'll never catch me!"))
                .ignoreElement()
                .compose(addActionBeforeResult());


        // (Subscribers)
        hasData.subscribe(completableObserver);
        hasError.subscribe(completableObserver);

        }

    static CompletableTransformer addActionBeforeResult() {
        Action action = () -> System.out.println("\tI want to insert some action right before the completable fires");

        return completable -> Completable.fromAction(action).concatWith(completable);

    }
}
