package CustomOperators_15.Transformers_1;


import com.google.common.collect.ImmutableList;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class FlowableTransformer_3 {

    public static void main(String[] args) {


        /*
           A flowable implementation of the previous example

         */
        Flowable.just("Beagle" , "Boxer", "Coonhound", "Stafforshire Terrier")
                .compose(toGuavaImmutableList())
                .subscribe(System.out::println);


        Flowable.range(1, 5)
                .compose(toGuavaImmutableList())
                .subscribe(System.out::println);

    }

    /*
        Here is our magic transformerator.
     */
    public static <T> FlowableTransformer<T, ImmutableList<T>> toGuavaImmutableList() {

        /*

            The impl below is cleaner than doing this:

                return new FlowableTransformer<T, ImmutableList<T>>() {
                @Override
                public FlowableSource<ImmutableList<T>> apply(Flowable<T> flowable) {
                    // impl
            }

            I'm showing this to praise lambdas and to expose the underlying impl.
     };

         */
        return flowable -> flowable
                .collect(ImmutableList::<T>builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .toFlowable();
    }
}
