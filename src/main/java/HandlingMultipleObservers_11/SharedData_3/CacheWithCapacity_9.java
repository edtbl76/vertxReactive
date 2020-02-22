package HandlingMultipleObservers_11.SharedData_3;

import io.reactivex.Observable;

public class CacheWithCapacity_9 {

    public static void main(String[] args) {
        /*
            Same example as previous, but this takes advantage of eagerly loading the cache size
            using cacheWithInitialCapacity().

         */
        Observable<Integer> observable =
                Observable.range(1, 10)
                .scan(0, Integer::sum)
                .cacheWithInitialCapacity(10);

        /*
            Same as previous...
         */
        observable.subscribe(integer -> System.out.println("1: " + integer));
        observable.subscribe(integer -> System.out.println("2: " + integer));
        observable.subscribe(integer -> System.out.println("3: " + integer));


    }
}
