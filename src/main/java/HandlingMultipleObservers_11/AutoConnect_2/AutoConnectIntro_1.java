package HandlingMultipleObservers_11.AutoConnect_2;

import Utils.Generic;
import io.reactivex.Observable;

public class AutoConnectIntro_1 {
    public static void main(String[] args) {

        /*
            This is a basic example of using autoConnect()

            This changes the order of events so that "connect" is called before the subscribers are defined.
            (be very afraid).
         */
        Observable<Integer> observable =
                Observable.range(1, 5)
                        .map(integer -> Generic.getRandom())
                        .publish()
                        .autoConnect(2);

        observable
                .subscribe(integer -> System.out.println("Observer 1: - Printed - " + integer));
        observable
                .reduce(0, Integer::sum)
                .subscribe(integer -> System.out.println("Observer 2: - Reduced - " + integer));


    }
}
