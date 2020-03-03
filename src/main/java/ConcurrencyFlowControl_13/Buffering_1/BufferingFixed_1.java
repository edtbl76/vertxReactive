package ConcurrencyFlowControl_13.Buffering_1;

import io.reactivex.Observable;

public class BufferingFixed_1 {

    public static void main(String[] args) {


        int cores = Runtime.getRuntime().availableProcessors();
        /*
            My chain starts w/ a range that is going to create 97  events.

            Our buffer() operator is going to batch up the results based on the provided argument. I'm choosing
            the number of cores. (Which in my case is 12)

            Then we are printing it out.

            I chose 97 events so that you can see what happens when the number doesn't divide evenly.
            The last "batch" is going to be smaller.

         */
        Observable.range(1, 97)
                .buffer(cores)
                .subscribe(System.out::println);
    }
}
