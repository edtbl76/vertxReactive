package ConcurrencyFlowControl_13.Buffering_1;

import io.reactivex.Observable;

import java.util.HashSet;

public class BufferingChooseCollection_2 {

    public static void main(String[] args) {


        int cores = Runtime.getRuntime().availableProcessors();
        /*
            This is an example of an overload where I've specified the type of Collection I'd like to use
            in order to batch up my events for processing.
         */
        Observable.range(1, 97)
                .buffer(cores, HashSet::new)
                .subscribe(System.out::println);
    }
}
