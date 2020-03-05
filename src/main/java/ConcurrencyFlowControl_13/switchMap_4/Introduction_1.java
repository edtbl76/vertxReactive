package ConcurrencyFlowControl_13.switchMap_4;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Introduction_1 {

    public static void main(String[] args) {


        /*
            REVIEW
            - This is just reviewing previous concepts so we can introduce a switchMap.

            1.) We have an emitter with 10 events.
            2.) We have a compute resource that guarantees ordering, as well as processes each event as its
            own Observable. (our "compute emulator can last up to 2 seconds).

            3.) We consume all of the events w/ a keep alive that persists the main thread for the longest possible
            duration.
         */

        Observable<Integer> source = Observable.range(1,10);

        Observable<Integer> computation = source.concatMap(
                integer -> Observable.just(integer).delay(Generic.getRandom(1, 2000), TimeUnit.MILLISECONDS)
        );

        computation.subscribe(System.out::println);
        Generic.wait(20);

    }
}
