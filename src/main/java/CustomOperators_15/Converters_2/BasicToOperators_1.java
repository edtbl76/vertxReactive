package CustomOperators_15.Converters_2;

import io.reactivex.Observable;

public class BasicToOperators_1 {

    public static void main(String[] args) {

        /*
            Basic Observable w/ range() factory
         */
        Observable<Integer> observable = Observable.range(1, 10);

        /*
            This is an observable that is turned into a list.
         */
        observable
                .toList()
                .subscribe(integers -> System.out.println("List<Integer>: " + integers));

        /*
            This is an observable that is turned into a map (this is the simplest implementation of
            accomplishing this via a method reference that just generates a key as a String of the value)
         */
        observable
                .toMap(Object::toString)
                .subscribe(stringIntegerMap -> System.out.println("Map<String, Integer>: " + stringIntegerMap));





    }
}
