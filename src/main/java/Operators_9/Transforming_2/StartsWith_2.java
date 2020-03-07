package Operators_9.Transforming_2;

import io.reactivex.Observable;

public class StartsWith_2 {

    public static void main(String[] args) {


        // Nothing special. Just an observable
        Observable<String> observable = Observable.just("square", "circle", "triangle", "trapezoid", "tetrahedron");

        // Demonstrates the insertion of a title header.
        observable
                .startWith("Shapes")
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("DONE")
                );

        /*
            If you noticed the output above.. it is kind of ugly when it prints, so lets fix that by using a
            variation of startWith()
         */
        System.out.println();
        observable
                .startWithArray("Shapes", "----------")
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("END OF LINE")
                );



    }
}
