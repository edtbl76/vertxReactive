package ColdAndHot_4;

import io.reactivex.Observable;

public class AColdDayInRx {
    public static void main(String[] args) {

        /*
            "Nothing Special" Observable
         */
        Observable<String> coldThings = Observable.just("dognose", "iceberg", "icecream", "donaldtrumpsblackheart");


        /*
            Observer 1 and 2. (Cold)
         */
        coldThings.subscribe(s ->  System.out.println("OBSERVER 1 - RCVD - [" + s + "]"));
        coldThings.subscribe(s ->  System.out.println("OBSERVER 2 - RCVD - [" + s + "]"));

    }
}
