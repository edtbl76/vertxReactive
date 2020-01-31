package Observers_3;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.Arrays;

public class ObserverIntro_1 {

    public static void main(String[] args) {

        /*
            Quick and dirty Observable.
         */
        Observable<String> linebackers = Observable.fromIterable(Arrays.asList("Bobby", "K.J.", "Mychal"));

        /*
            Home grown Observer Implementation (This is just overriding the implementation!)
            - This Sucks. If we had to do this every time we wanted to write our own Observer, Rx wouldn't be very
            useful...
         */
        Observer<Integer> observer = new Observer<>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("RCVD: " + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Finished!");
            }
        };


        /*
            Here is the Chain that results in actually subscribing using the implementation we've built

            NOTE: where subscribe is usually filled with a callback to a function, we are passing in the implementation
            of our custom observer. Slightly different here.
         */
        linebackers
                .map(String::length)
                .filter(integer -> integer > 4)
                .subscribe(observer);
    }
}

/*
    NOTE ON THE RESULTS:

    1.) source of events is 'linebackers', our Observable
    2.) We create our own flavor of observable and pass it to subscribe()

    Each String spit out from source is going to
        - be converted from a String to an Integer representing its length
        - THEN it is filtered by the value of that length
        - if it's still valid, it is passed to onNext() (which we have overridden to print the value)

    Once there are no more events, onComplete() is called (which announces it is done in our implementation)
 */
