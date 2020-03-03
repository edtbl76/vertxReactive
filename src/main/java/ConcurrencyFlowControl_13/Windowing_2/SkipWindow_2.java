package ConcurrencyFlowControl_13.Windowing_2;

import io.reactivex.Observable;

public class SkipWindow_2 {

    public static void main(String[] args) {

        /*
            This demonstrates a skip = to the count, so the results are the same as if the skip value hadn't been
            provided.

            1 + 2 + 3 + 4       = 10
            5 + 6 + 7 + 8       = 26
            9 + 10 + 11 + 12    = 42
            13 + 14 + 15 + 16   = 58
            17 + 18 + 19 + 20   = 74
         */
        System.out.println("count == skip");
        Observable.range(1,20)
                .window(4, 4)
                .flatMapSingle(integerObservable ->
                        integerObservable.reduce(0, Integer::sum))
                .subscribe(System.out::println);

        /*
            This demonstrates count < skip.
            This means values are skipped.

            1 + 2 + 3 + 4       = 10
            7 + 8 + 9 + 10      = 34
            13 + 14 + 15 + 16   = 58
            19 + 20             = 39
         */
        System.out.println("\ncount < skip");
        Observable.range(1,20)
                .window(4, 6)
                .flatMapSingle(integerObservable ->
                        integerObservable.reduce(0, Integer::sum))
                .subscribe(System.out::println);

        /*
            This one is more fun, because it overlaps

            (The first few create a pattern. we start with 15. The next one loses 1, but adds 6 - net gain of 5.
            The next one loses 2, adds 7 - net gain of 5. This pattern remains until we start running out of
            numbers)
            1 + 2 + 3 + 4 + 5       = 15
            2 + 3 + 4 + 5 + 6       = 20
            3 + 4 + 5 + 6 + 7       = 25
            4 + 5 + 6 + 7 + 8       = 30
            5 + 6 + 7 + 8 + 9       = 35
            6 + 7 + 8 + 9 + 10      = 40

            Now the pattern breaks down and subtracts each number
            7 + 8 + 9 + 10          = 34
            8 + 9 + 10              = 27
            9 + 10                  = 19
            10                      = 10

         */
        System.out.println("\ncount > skip");
        Observable.range(1,10)
                .window(5, 1)
                .flatMapSingle(integerObservable ->
                        integerObservable.reduce(0, Integer::sum))
                .subscribe(System.out::println);
    }
}
