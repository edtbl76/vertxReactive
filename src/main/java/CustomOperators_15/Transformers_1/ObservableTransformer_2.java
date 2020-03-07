package CustomOperators_15.Transformers_1;


import com.google.common.collect.ImmutableList;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class ObservableTransformer_2 {

    public static void main(String[] args) {


        /*
            Executing this is much cleaner than repeating the same steps over and over.

            - we've created a method that returns the ObservableTransformer<>.
            - we provide the method call to compose() as a first order function
            - we marvel at how clean our code looks AND good OOP practices by hiding the HOW in favor of the WHAT.

         */
        Observable.just("Beagle" , "Boxer", "Coonhound", "Stafforshire Terrier")
                .compose(toGuavaImmutableList())
                .subscribe(System.out::println);


        Observable.range(1, 5)
                .compose(toGuavaImmutableList())
                .subscribe(System.out::println);

    }

    /*
        Here is our magic transformerator.
     */
    public static <T> ObservableTransformer<T, ImmutableList<T>> toGuavaImmutableList() {

        /*

            The impl below is cleaner than doing this:

                return new ObservableTransformer<T, ImmutableList<T>>() {
                @Override
                public ObservableSource<ImmutableList<T>> apply(Observable<T> observable) {
                    // impl
            }

            I'm showing this to praise lambdas and to expose the underlying impl.
     };

         */
        return observable -> observable
                .collect(ImmutableList::<T>builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .toObservable();
    }
}
