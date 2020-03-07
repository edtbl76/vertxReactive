package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomBackpressure_4 {

    public static void main(String[] args) {

        /*
            This demonstrates how we can manually change the way we handle backpressure.

            This is a bit hard to understand, so let's start by suggesting that the default behavior of backpressure
            can't quite be altered.

            - We'll see our onSubscribe() message that suggests we are going to request 50 items.
            - then we'll generate 128 events.
            - HERE is where the behavior is altered.
            - I'm still going to have 96 events pushed, but the first "increment" is 50 events, and then
              each onNext() call gets 25 at a time until we reach the total of 96 events.


          NOTE:
            The request() calls only go to the previous operator... not all the way upstream. This means that
            we are carving up our flowable chain quite a bit.



         */
        Flowable.range(1, 500)
                .doOnNext(integer -> System.out.println("Event Emitted: " + integer))
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<>() {

                    /*
                        We are creating our own custom subscription object.
                     */
                    Subscription customSubscription;
                    AtomicInteger counter = new AtomicInteger(0);

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.customSubscription = subscription;
                        System.out.println("Custom Default: 50 Items Requested");
                        subscription.request(50);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Generic.waitMillis(25);
                        System.out.println("Subscriber RCVD: " + integer);

                        if (counter.incrementAndGet() % 25 == 0 && counter.get() >= 50)
                            System.out.println("Gimme 25 more!");

                        customSubscription.request(25);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Finished");
                    }
                });
        Generic.wait(20);

    }
}
