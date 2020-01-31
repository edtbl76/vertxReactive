package Maybe_6;

import io.reactivex.Observable;

public class ObservablesThatReturnMaybe_2 {

    public static void main(String[] args) {

        Observable<String> observable = Observable.just("King", "Carpenter", "Craven", "Koontz");

        /*
            firstElement() is like first(), but instead of returning a Single, it returns a Maybe.
         */
        observable.firstElement()
                .subscribe(
                        s -> System.out.println("RCVD: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Complete")
                );
    }
}
