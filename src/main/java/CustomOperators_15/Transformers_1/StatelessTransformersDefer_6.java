package CustomOperators_15.Transformers_1;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

import java.util.concurrent.atomic.AtomicInteger;

public class StatelessTransformersDefer_6 {

    public static void main(String[] args) {


        /*
           There is no difference here. Our top level (abstracted) source Observable doesn't have to be touched.
           (This is a good demonstration of the value of transformers!)
         */
        Observable<IndexedObject<Integer>> observable =
                Observable.range(1, 10).compose(getIndex());


        /*
            These are TWO subscribers.

        */
        observable.subscribe(integerIndexedObject -> System.out.println("Your Data  - " + integerIndexedObject));
        observable.subscribe(integerIndexedObject -> System.out.println("My   Data  - " + integerIndexedObject));

    }

    /*
        The guts of our transformer have been changed in order to make the impl stateless.
        (You remember defer() don't you!!!)

        In order to consolidate the cost of resource setup (which can get expensive), most observables are eagerly
        instantiated. This means that the observable hangs around (as well as all of its data). This means that
        when another subscriber comes to the party... it is going to be filthy with some other subscriber's data!

        Defer() lazy loads Observables, so that setup/teardown happens based on the lifecycle of the subscriber/consumer/
        etc. Each time a subscriber is created, a new Observable is created, with a fresh slate of state.

     */
    static <T> ObservableTransformer<T, IndexedObject<T>> getIndex() {

        return observable -> Observable.defer(() -> {
            AtomicInteger index = new AtomicInteger(-1);
            return observable
                    .map(value -> new IndexedObject<T>(index.incrementAndGet(), value));
        });
    }

    /*
        This is a basic pojo that includes a value and an index.
     */
    static final class IndexedObject<T> {
        final int index;
        final T value;

        IndexedObject(int index, T value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + index + "]" + value;
        }
    }


}
