package HandlingMultipleObservers_11.SharedData_3;

import io.reactivex.Observable;

public class ReplayRefCount_4 {

    public static void main(String[] args) {

        /*
            There are two differences with this observable
                1.) I used range (no keepalive required) (i.e. this is a FINITE data set)
                2.) I used refCount() instead of autoConnect().

                    autoConnect() persists connections at termination/disposal, refCount does not.
         */
        Observable<Integer> observable = Observable.range(1, 5)
                .replay(1)
                .refCount();

        /*
            Here are two consumers, and this is what happens when everything executes.

            1.) The first consumer eats 5 events. and it is DONE -- it calls onComplete()
            2.) once onComplete() is called, since we called refCount(), the connection is also cleaned up, and any
            3.) further subscriber is going to execute from the start of the execution chain.
                - This means that the second consumer eats all of the same events.

            Ed, are you saying that we just created a 'scenic' route to creating a Cold Observable?

            If by scenic you mean, wasting cycles and using more expensive procedures to do something that can be
            achieved in a simple way... then yes. (I'm exaggerating. The overhead is definitely larger, but it is
            unlikely that it is going to be a major problem unless you are performing highly concurrent/heavy user
            workloads (i.e. webscale)).

            - If you are performing those highly concurrent workloads, then this scenario is still unlikely, because
            subscribers are more than likely going to overlap, preventing the "restart" scenario associated with
            refCount()

            No one is going to INTEND to create a cold observable this way. This is probably a corner case/side effect
            of many designs that are intended to take advantage of multicasting and event storage/caching. The reason
            I mention "cold observable", is that if you find yourself getting these results more often than not,
            you've probably jumped the gun with multicasting/caching when a cold observable was a simpler and more
            appropriate design choice. (Which fortifies the best practices tenet: When in doubt... keep 'em cold).



         */
        observable.subscribe(integer ->  System.out.println("O1: " + integer));
        observable.subscribe(integer ->  System.out.println("O2: " + integer));
    }
}
