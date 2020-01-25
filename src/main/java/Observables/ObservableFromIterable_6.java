package Observables;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class ObservableFromIterable_6 {

    public static void main(String[] args) {

        /*
            Observable.fromIterable() is very powerful, because it allows a Collection to be
            "transformed" into a reactive stream.
         */
        List<String> legionOfBoom = Arrays.asList("Richard", "Earl", "Kam", "Brandon");
        Observable<String> observable = Observable.fromIterable(legionOfBoom);

        /*
            Observer... should look familiar
         */
        observable
                .map(String::length)
                .filter(integer -> integer < 4)
                .subscribe(System.out::println);
    }
}
