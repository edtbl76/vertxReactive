package Single_5;

import io.reactivex.Observable;

public class ObservablesThatReturnSingle_2 {
    public static void main(String[] args) {
        Observable<String> words = Observable.just("Alpha", "Bravo", "Charlie", "Delta", "Echo");

        /*
            first() is a utility operator of an Observable that emits he first element of the Observable as a Single
            - NOTE: while it isn't required, you can provide a default item in the event that it returns "empty"
         */
        words.first("Nil").subscribe(System.out::println);
    }
}

