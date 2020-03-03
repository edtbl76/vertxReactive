package ConcurrencyFlowControl_13.Windowing_2;

import io.reactivex.Observable;

public class FixedWindow_1 {

    public static void main(String[] args) {

        /*
            This is essentially the same as buffer
            - window has a specified batch count, that tells us how many Observable<Integers> we're going to store.
            - flatMapSingle turns each "event" into a single. In this case, each event is one batch.

            I have two examples you can toggle between here. One gives a string of hyphen separated values per
            batch, based on my initial range factory.

            The second example gives you the added result of the five numbers put together.
         */
        Observable.range(1, 17)
                .window(5)
                .flatMapSingle(
                        integerObservable -> integerObservable.reduce("", (sum, next) -> sum
                        + (sum.equals("") ? "" : "-") + next)
//                        integerObservable -> integerObservable.reduce(0, Integer::sum)
                )
                .subscribe(System.out::println);


    }
}
