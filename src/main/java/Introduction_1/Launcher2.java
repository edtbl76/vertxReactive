package Introduction_1;

import io.reactivex.Observable;

public class Launcher2 {
    public static void main(String[] args) {

        /*
            Here is another Observable
         */
        Observable<String> numbers = Observable.just("one", "two", "three", "four", "five");

        /*
            Here is an ObservER
            - Note the difference.

            map() has transformed the event from the value to the length of the value's characters.
            We are subscribing to the transformation

         */
        numbers
                .map(String::length)
                .subscribe(System.out::println);
    }
}
