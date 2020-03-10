package CustomOperators_15.OperatorDIY_3;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.observers.DisposableObserver;

import java.util.ArrayList;
import java.util.List;

public class CreatingOurOwnToList_3<T> {

    public static void main(String[] args) {

        Observable.just("this", "is", "how", "you", "do", "it")
                .lift(customToList())
                .subscribe(strings -> System.out.println(String.join(" ", strings)));

        Observable.<String>empty()
                .lift(customToList())
                .subscribe(System.out::println);

    }

    /*
        This is a nifty way to play around with the Observer methods to create interesting patterns.
     */
    public static <T> ObservableOperator<List<T>, T> customToList() {

        return observer -> new DisposableObserver<T>() {

            ArrayList<T> arrayList = new ArrayList<>();
            @Override
            public void onNext(T t) {
                // We are collecting the events and adding them to an internal list.
                arrayList.add(t);
            }

            @Override
            public void onError(Throwable throwable) {
                observer.onError(throwable);
            }

            @Override
            public void onComplete() {
                // We wait until onComplete() to pass a single event (our list) and then execute onComplete.
                observer.onNext(arrayList);
                observer.onComplete();
            }
        };
    }
}
