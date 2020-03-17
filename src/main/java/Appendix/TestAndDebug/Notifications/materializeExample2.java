package Appendix.TestAndDebug.Notifications;

import io.reactivex.*;

public class materializeExample2 {

    public static void main(String[] args) {

        /*
           This is a hot observable w/ autoConnect setup.
           - note that we've fed '3' to the autoConnect call so we know that there will only be
           3 subscribers. This is necessary if we want onComplete() to be called.
           - materialize creates the notification
         */
        Observable<Notification<String>> observable =
                Observable.just("Contagion", "Pandemic", "Virus", "Disease", "Spread")
                .materialize()
                .publish()
                .autoConnect(3);

        // This is our isOnNext() notification which pulls the value out of the onNext() calls
        observable
                .filter(Notification::isOnNext)
                .subscribe(stringNotification -> System.out.println("onNext() = " + stringNotification.getValue()));

        // This is our isOnComplete() notification which generates notifications when the chain has completed.
        observable
                .filter(Notification::isOnComplete)
                .subscribe(stringNotification -> System.out.println("onComplete()"));

        // This should be self explanatory by now
        observable
                .filter(Notification::isOnError)
                .subscribe(stringNotification -> System.out.println("onError()"));
    }
}
