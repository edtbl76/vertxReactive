package Dispose;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;

import java.util.concurrent.TimeUnit;

public class ResourceObserver_2 {

    public static void main(String[] args) {

        // Observable... Nothing special.
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        /*
            ResourceObserver implements both the Observer and Disposable interfaces.
            - This allows us to manage the tasks of onNext, onError, onComplete.
         */
        ResourceObserver<Long> observer = new ResourceObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };

        /*
            subscribe() = what we usually use, however this is a void method, so it returns no value.
            subscribeWith() essentially returns the observer "as is"

            - in this particular case, since this is a "ResourceObserver" that supports "cancellation of tasks",
            it can be assigned to a Disposable.

         */
        Disposable disposable = observable.subscribeWith(observer);

        /*
            This is our "keepalive" in the main thread to ensure Observable.interval() can be executed by computation
            scheduler.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
            Demonstrating how we can use the methods of Disposable.
            - The Observable is NOT cancelled, then we dispose it and check our work.

         */
        System.out.println(disposable.isDisposed());
        disposable.dispose();
        System.out.println(disposable.isDisposed());
    }
}
