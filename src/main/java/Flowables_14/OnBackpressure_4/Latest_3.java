package Flowables_14.OnBackpressure_4;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class Latest_3 {

    public static void main(String[] args) {

        /*
            Watch the output carefully when you run this. It's very easy to miss what is happening.

            You'll notice that every now and then, there is a gap in the output. That is because our backpressure
            strategy is LATEST.

            This is similar to buffer with a capacity of 1, and that 1 is always the latest event. As soon as
            the Subscriber is ready to process events, it grabs whatever is in that buffer (the latest event) and
            continues processing. This means that we lose all of the events generated between the "stop" and "send me
            more" signals from the Subscriber.

         */
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(Schedulers.io())
                .subscribe(l -> {
                    Generic.waitMillis(8);
                    System.out.println(l);
                });

        Generic.wait(6);
    }
}
