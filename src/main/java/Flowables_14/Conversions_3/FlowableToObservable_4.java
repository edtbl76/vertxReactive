package Flowables_14.Conversions_3;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FlowableToObservable_4 {

    public static void main(String[] args) {

        /*
            Vanilla Flowable
         */
        Flowable<Integer> flowable = Flowable.range(1, 9);

        /*
            This Observable spits out 5 letters.
            We flatMap it down so that for each event, we are going to take the flowable above
            as an argument, munge the data together and then convert the flowable to an Observable so that
            it can be consumed by an Observer.

            Flowables will use backpressure upstream until they are converted. At this point, they will accept
            Long.MAX_VALUE emissions.

            To be honest, while this is possible, it's not as common in the wild because it is potentially dangerous.
            If we have a use case that warrants a Flowable, we have to be absolutely sure that scaling down to an
            Observable makes mathematical sense.

            If there are corner cases, it's better to be safe than sorry. Stay Flowable my friends.
         */
        Observable.just("A", "B", "C", "D", "E")
                .flatMap(s ->
                    flowable
                            .subscribeOn(Schedulers.computation())
                            .map(integer -> s + ":" + integer)
                            .toObservable()
                ).blockingSubscribe(System.out::println);


    }
}
