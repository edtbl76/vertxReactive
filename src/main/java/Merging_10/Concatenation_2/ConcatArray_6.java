package Merging_10.Concatenation_2;

import io.reactivex.Observable;

public class ConcatArray_6 {

    public static void main(String[] args) {

        // Two run of the mill Observables
        Observable<String> beginning = Observable.just("once", "upon", "a", "time");
        Observable<String> end  = Observable.just("the", "end");

        /*
            I've build a generic array. If you don't suppress unchecked (due to varargs), it will squawk.
         */
        @SuppressWarnings("unchecked")
        Observable<String>[] fairyTale = new Observable[]{beginning, end};

        /*
            This concats a variable number of Obervables together.

            Why would I use this you ask if it is going to be such an ugly solution?
            Because, like merge(), concat() is limited to only 4 ObservableSource<T> parameters.

         */
        Observable.concatArray(fairyTale)
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        System.out::println
                );



    }

}
