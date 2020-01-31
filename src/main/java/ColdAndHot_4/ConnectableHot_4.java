package ColdAndHot_4;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class ConnectableHot_4 {

    public static void main(String[] args) {

        /*
            Adding publish() to the end of a "regular" observable, turns it into a ConnectableObservable.
            Note: we still have to define the return type as a ConnectableObservable<>
         */
        ConnectableObservable<String> hotStuff = Observable.just("fire", "habanero", "apacheignite").publish();

        // Observer 1 and 2
        hotStuff.subscribe(s -> System.out.println("Observer 1: [" + s + "]"));
        hotStuff.map(String::length).subscribe(s -> System.out.println("Observer 2: [" + s + "]"));

        // Nothing happens until we connect to the "Connectable"
        /*
            A noted difference is that observer 1 and 2 are fired in order before iterating to the next event from the
            observable.

            This observed interleaving is actually a result of the events being received SIMULTANEOUSLY.
         */
        hotStuff.connect();
    }
}
