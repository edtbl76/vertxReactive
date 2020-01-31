package Observables_2;

import io.reactivex.Observable;

public class ErrorFactory_12 {

    public static void main(String[] args) {

        /*
            This is similar to empty and never, but it generates an error.
            - This would be used for testing purposes to ensure that errors are being generated.
         */
        Observable.error(new Exception("Danger, Will Robinson!"))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("I'm not going to be executed")
                );
    }
}
