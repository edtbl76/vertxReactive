package Appendix.TestAndDebug.Notifications;

import io.reactivex.*;

public class materializeExample {

    public static void main(String[] args) {

        Observable<Integer> observable = Observable.range(1, 10);

        observable.materialize().subscribe(System.out::println);
    }
}
