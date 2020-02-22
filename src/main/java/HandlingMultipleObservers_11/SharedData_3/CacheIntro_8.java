package HandlingMultipleObservers_11.SharedData_3;

import io.reactivex.Observable;

public class CacheIntro_8 {

    public static void main(String[] args) {
        /*
            This makes use of scan() to create a running total of numbers. (creates a good demonstration
            of how cache works).

            cache() is used instead of replay().
         */
        Observable<Integer> observable =
                Observable.range(1, 10)
                .scan(0, Integer::sum)
                .cache();

        /*
            Three observers subscribe in succession.
            Since this is a finite data set, each Observer is going to fire after onComplete() is fired from
            the previous Observer.
            - the result of the output is indecipherable from how a cold observable works.
            - each Observer will consume all events, after the previous observer has completed its work.
         */
        observable.subscribe(integer -> System.out.println("1: " + integer));
        observable.subscribe(integer -> System.out.println("2: " + integer));
        observable.subscribe(integer -> System.out.println("3: " + integer));


    }
}
