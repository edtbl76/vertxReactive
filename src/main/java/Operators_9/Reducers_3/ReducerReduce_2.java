package Operators_9.Reducers_3;

import io.reactivex.Observable;

public class ReducerReduce_2 {

    /*
        Reduce is almost identical to scan(), but it returns either a Single or Maybe, and it ONLY gets you
        the result of the accumulation operation, whereas scan is going to give you a rolling total.
     */
    public static void main(String[] args) {

        /*
            Basic usage of reduce.
            - Integer::sum is the method expression of (sum, next) -> sum + next
         */
        Observable.just(1, 2, 3, 4, 5)
                .reduce(Integer::sum)
                .subscribe(System.out::println, Throwable::printStackTrace);


        /*
            Making reduce behave ALMOST like scan(), just to be a bunch of silly-heads
         */
        Observable.just(1, 2, 3, 4, 5)
                /*
                    seed values should be immutable.

                    An example is if we want to reflect events into a collection.
                    - using reduce() will use the SAME collection every time.
                    - a different option... collect()), will create an empty collection each time.
                 */
                .reduce("",
                        (sum, next) -> sum + (sum.equals("") ? "" : ", ") + next)
                .subscribe(System.out::println, Throwable::printStackTrace);

    }
}
