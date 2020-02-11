package Merging_10.Zippy_4;

import Utils.Generic;
import io.reactivex.Observable;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class ZipInfinite_6 {

    public static void main(String[] args) {

        /*
            Observable of data to be pushed and an Observable of time intervals in which I want data to be pushed
         */
        Observable<String> events = Observable.just("gala", "party", "bash", "events", "getit?");
        Observable<Long> interval = Observable.interval(500, TimeUnit.MILLISECONDS);

        /*
            The BiFunction here is only returning the String.
            - However if you look carefully at the time stamps, you'll note that they are each separated by
            500 ms.
         */
        Observable.zip(events, interval, (s, l) -> s)
                .subscribe(
                        s -> System.out.println("RCVD: " + s + " at " + LocalTime.now()));

        Generic.wait(3);

        /*
            Another example using milliseconds. You'll note that in both cases, the difference isn't EXACTLY
            500ms.
         */
        Observable.zip(events, interval, (s, l) -> s)
                .subscribe(
                        s -> System.out.println("RCVD: " + s + " at " + System.currentTimeMillis()));
        Generic.wait(3);
    }
}
