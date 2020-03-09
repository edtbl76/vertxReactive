package CustomOperators_15.Transformers_1;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

import java.util.concurrent.atomic.AtomicInteger;

public class StatefulTransformers_5 {

    public static void main(String[] args) {


        /*
            This is a source that will make use of the Object and the ObservableTransformer that we have created
            below (see impl for details).
         */
        Observable<IndexedObject<Integer>> observable =
                Observable.range(1, 10).compose(getIndex());


        /*
            These are TWO subscribers.

            The output is going to demonstrate a problem.
            - we have shared the same instance of the Atomic Integer we created in our Transformer. The unintentional
            side effect is that state has been shared between our subscribers....

            (You'll notice this in the output when we reach the second subscriber, the index starts after the first
            subscriber ends, rather than "restarting")
         */
        observable.subscribe(integerIndexedObject -> System.out.println("Subscriber 1 - " + integerIndexedObject));
        observable.subscribe(integerIndexedObject -> System.out.println("Subscriber 2 - " + integerIndexedObject));

    }

    /*
        This is an observable transformer that indexes objects/values as they are provided (specifically the object
        we created)
     */
    static <T> ObservableTransformer<T, IndexedObject<T>> getIndex() {
        final AtomicInteger index = new AtomicInteger(-1);
        return observable -> observable
                .map(value -> new IndexedObject<T>(index.incrementAndGet(), value));
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
