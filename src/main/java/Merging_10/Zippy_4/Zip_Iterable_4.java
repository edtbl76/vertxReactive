package Merging_10.Zippy_4;


import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Zip_Iterable_4 {

    public static void main(String[] args) {

        /*
            The first three lines should make sense.
         */
        Observable<String> words = Observable.just("one", "two", "three");
        Observable<String> numbers = Observable.just("1", "2", "3");

        List<Observable<String>> observables  = Arrays.asList(words, numbers);

        /*
            This is ugly. In order to make this work you have to take the object and then collect them to a list.

            This is because the internal Function has to be an Object (that you remap to the type you want).
            Other solutions are to override in place. It's not one of my favorite areas of Rx.
         */
        Observable.zip(observables, obj -> Stream.of(obj).map(o -> (String) o).collect(Collectors.toList()))
                .subscribe(System.out::println, Throwable::printStackTrace, System.out::println);

    }
}
