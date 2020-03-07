package Flowables_14.Flowables_2;

import Utils.Generic;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class SubscriberCustom_2 {

    public static void main(String[] args) {

        /*
            This is the same as the previous example, but rather than just providing method expression/lambda
            callbacks, we are implementing our own Subscriber() to demonstrate the variance (and similarities)
            between the Subscriber and Observer.
         */
        Flowable.range(1, 100)
                .doOnNext(integer -> System.out.println("Flowable Pushing: " + integer))
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<>() {
                               @Override
                               public void onSubscribe(Subscription subscription) {
                                   subscription.request(Long.MAX_VALUE);
                               }

                               @Override
                               public void onNext(Integer integer) {
                                    Generic.waitMillis(50);
                                    System.out.println("Subscriber RCVD: " + integer);
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   throwable.printStackTrace();
                               }

                               @Override
                               public void onComplete() {
                                   System.out.println("I am done.");
                               }
                           }


                );
        Generic.wait(20);
    }
}
