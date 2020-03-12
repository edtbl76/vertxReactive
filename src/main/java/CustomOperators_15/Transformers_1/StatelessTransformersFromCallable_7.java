package CustomOperators_15.Transformers_1;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

import java.util.concurrent.atomic.AtomicInteger;

public class StatelessTransformersFromCallable_7 {

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
        This is our second version.

        fromCallable() is often an alternative for using defer().


     */
    static <T> ObservableTransformer<T, IndexedObject<T>> getIndex() {

        return observable -> Observable.fromCallable(
                () ->  new AtomicInteger(-1))
                .flatMap(atomicInteger ->
                    observable.map(value -> new IndexedObject<>(atomicInteger.incrementAndGet(), value))
                );
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
