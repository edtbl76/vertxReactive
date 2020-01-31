package Observables_2;

import io.reactivex.Observable;

public class RangeFactory_7 {
    public static void main(String[] args) {

        /*
            This behaves similar to Stream.rangeclosed(), where the last value isn't an exclusive bound, but rather
            the number of events to send
         */
        Observable.range(1, 10).subscribe(s -> System.out.println("RCVD: [" + s + "]"));

        /*
            This demonstrates it better.
         */
        Observable.range(100, 10).subscribe(s -> System.out.println("RCVD: [" + s + "]"));

    }
}
