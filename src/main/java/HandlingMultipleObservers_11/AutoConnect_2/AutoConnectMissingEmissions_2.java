package HandlingMultipleObservers_11.AutoConnect_2;

import Utils.Generic;
import io.reactivex.Observable;

public class AutoConnectMissingEmissions_2 {

    public static void main(String[] args) {

        /*
            autoconnect, standard use case.
         */
        Observable<Integer> observable = Observable.just(1, 2, 3)
                .map(integer -> Generic.getRandom())
                .publish()
                .autoConnect(2);

        /*
            2 happy little subscribers.
         */
        observable
                .subscribe(integer -> System.out.println("RCVD: " + integer));
        observable
                .reduce(0, Integer::sum)
                .subscribe(integer -> System.out.println("AGGREGATE: " + integer));

        /*
            This never fires, because we've already found 2 subscribers.
         */
        observable.subscribe(integer -> System.out.println("Nothing: " + integer));
    }
}
