package ColdAndHot;

import io.reactivex.Observable;

public class AColdDayInRx2 {
    public static void main(String[] args) {

        /*
            "Nothing Special" Observable
         */
        Observable<String> coldThings = Observable.just("dognose", "iceberg", "icecream", "donaldtrumpsblackheart");


        /*
            Observer 1 and 2. (Cold)
            - This example is a bit different than the previous.
            - The first subscriber is essentially passthrough with some content decoration
            - However the second subscriber is similar to the example we've been using throughout this "tutorial".

            This is a great demonstration of the POWER of Reactive Programming .
            I have created one stream of emissions via my observable. However, I have created two different flavors of
            subscriber that are capable of doing different things with different data.

            RxJava is in fact Burger King. You can have it your way.

         */
        coldThings.subscribe(s ->  System.out.println("OBSERVER 1 - RCVD - [" + s + "]"));
        coldThings
                .map(String::length)
                .filter(integer -> integer > 8)
                .subscribe(s -> System.out.println("OBSERVER 2 - RCVD - [" + s + "]"));

    }
}
