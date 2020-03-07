package CustomOperators_15.Transformers_1;

import com.google.common.collect.ImmutableList;
import io.reactivex.Observable;

public class Introduction_1 {

    public static void main(String[] args) {

        /*
            This is a great example of using 2 operators (collect and map) to perform a set of steps
            that could be used many times over.
            - for giggles... we are going to do just that.
         */
        Observable.just("Beagle" , "Boxer", "Coonhound", "Stafforshire Terrier")
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);

        Observable.range(1, 5)
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);


    }
}
