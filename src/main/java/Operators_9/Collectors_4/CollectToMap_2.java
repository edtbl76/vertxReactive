package Operators_9.Collectors_4;

import io.reactivex.Observable;

import java.util.concurrent.ConcurrentHashMap;

public class CollectToMap_2 {

    public static void main(String[] args) {

        // Just an observable
        Observable<String> cameras = Observable.just("camera1", "camera2", "camera3");

        /*
            collecting to a map is trickier, because we have to fabricate the keys.
            - This particular example isn't very good unless you know you are going to end up
            with unique calues.

         */
        System.out.print("Example 1: ");
        cameras
                .toMap(s -> s.charAt(s.length() - 1))
                .subscribe(System.out::println);

        /*
            Why this breaks...
            - When we use String::length as a key, the length is equal for every event.
            - this means that the key is going to replaced every time the values are equal.
            - the end result is that the only "value" represented by the resulting map will be
            the LAST value collected with the particular key computation.
         */
        System.out.print("Example 2: ");
        cameras
                .toMap(String::length)
                .subscribe(System.out::println);

        /*
            This is an example of providing a SECOND ARGUMENT
            - in this case the first argument is the key
            - the second argument is the value.
         */
        System.out.print("Example 3: ");
        cameras
                .toMap(s -> s.charAt(s.length() - 1 ), String::length)
                .subscribe(System.out::println);

        /*
            Default implementation is HashMap, but you can override this via the
            third argument/lambda/closure/callback :)
         */
        System.out.print("Example 4: ");
        cameras
                .toMap(s -> s.charAt(s.length() - 1), String::toCharArray, ConcurrentHashMap::new)
                .subscribe(System.out::println);

        /*
            Whoa whoa whoa... Ed, how do I solve the case of Example 2?
            What if I want all of those events to map to a single key? There has to be a way to do that right?

            Yes! There is! this is a MultiMap. MultiMap stuffs all of the events w/ the same key into a Collection.
         */
        System.out.print("Example 5: ");
        cameras
                .toMultimap(String::length)
                .subscribe(System.out::println);

    }
}
