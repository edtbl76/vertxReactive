package Introduction_1;

import io.reactivex.Observable;

public class Launcher {
    public static void main(String[] args) {

        /*
            Observable is an emitter that spits out events/data. (In Reactive development you absolutely MUST
            come to terms with the fact that Events = Data)
         */
        Observable<Integer> numbers = Observable.just(1,2,3,4,5);

        /*
            Observer is the consumer of events/data.
            In this case, once we consume them, we are going to print them out with a closure/callback to System.out
         */
        numbers.subscribe(System.out::println);
    }
}

/*
    NOTES ON RESULTS:

    1. Observable PUSHES each piece of data as an EVENT.
    2. The ObservER is the callback (This is the actually System.out::println, which is just a closure written as a
    method expression. The variable is "anonymous", but it's there!) We don't HAVE to print it out, but this is
    an easy way to demonstrate the flow of reactive development.

    STREAMS vs. OBSERVABLES
    - Observables PUSH/EMIT events
    - Streams/Sequences PULL/POLL events.
 */
