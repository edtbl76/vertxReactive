package Merging_10.Zippy_4;

import io.reactivex.Observable;

public class ZipIntro_1 {

    public static void main(String[] args) {

        /*
            I can't seem to do a tutorial/examples w/o a Justice League reference.
            Sorry folks. Nostalgia wins.
         */
        Observable<String> first = Observable.just("Clark", "Bruce", "Diana", "Barry", "Hal", "Arthur", "J'onn");
        Observable<String> last = Observable.just("Kent", "Wayne", "Prince", "Allen", "Jordan", "Curry", "J'onzz");

        /*
            We've provided the two sources and then a BiFunction demonstrates how we want to combine them.

            Besides that, this is pretty self explanatory.
         */
        Observable
                .zip(first, last, (uno, dos) -> uno + " " + dos)
                .subscribe(System.out::println);




    }
}
