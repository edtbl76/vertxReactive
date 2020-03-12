package CustomOperators_15.Transformers_1;

import io.reactivex.Observable;
import io.reactivex.SingleTransformer;

import java.util.Collection;
import java.util.Collections;

public class SingleTransformer_8 {

    /*
        Overriding apply() here is very similar to the custom/DIY Operators. (You might want to jump ahead to
        see it.)

        Despite that.. we are essentially doing the same thing we've done before.
     */
    public static void main(String[] args) {

        Observable.range(1, 10)
                .toList()
                .compose(toUnmodifiableCollection())
                .subscribe(System.out::println);
    }

    static <T> SingleTransformer< Collection<T>, Collection<T>> toUnmodifiableCollection() {
        return singleObserver -> singleObserver
                .map(Collections::unmodifiableCollection);

    }
}
