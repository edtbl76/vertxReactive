package Single;

import io.reactivex.Single;

public class SingleIntro_1 {

    public static void main(String[] args) {

        /*
            Very basic example of a Single.

            - we use the just() factory to create a string
            - we use map() operator to convert the string to its length
            - we pass a callback to onSuccess and another one to onError.

            This is pretty much the same as an Observable!
         */
        Single.just("Hello")
                .map(String::length)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace
                );
    }
}
