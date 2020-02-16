package HandlingMultipleObservers_11.SharedData_3;

import io.reactivex.Observable;

public class ReplayAutoConnect_5 {

    public static void main(String[] args) {

        /*
            Same example as the previous, but we've replaced refCount() with autoConnect()
         */
        Observable<Integer> observable = Observable.range(11, 5)
                .replay(1)
                .autoConnect();

        /*
            Auto Connect keeps things warm.
            Since we have used autoConnect(), despite the fact that the first subscriber has called onComplete(),
            the CACHE IS STILL POPULATED WITH THE LAST EVENT.

            While this is probably the desired effect as far as functional use, we have to be careful.
            Even after all of the Observers have gone home for the night, as long as the service providing the
            source is still up, then the last event will persist in the cache.

            This isn't quite like leaving your laptop unlocked, but it is like leaving it on a bench at the airport for
            8 hours unsupervised.
         */
        observable.subscribe(integer -> System.out.println("[O1]: " + integer));
        observable.subscribe(integer -> System.out.println("[O2]: " + integer));

    }
}
