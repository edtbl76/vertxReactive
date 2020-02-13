package HandlingMultipleObservers_11.Multicasting_1;

import Utils.Generic;
import io.reactivex.Observable;

public class ColdObservableWithOperator_3 {

    public static void main(String[] args) {

        /*
            As the wiki suggests, all of our hard work might be undone by inserting operators that split out chains
            after our publish() call.... like below...


            NOTE: This operator has a specific purpose.

            If you recall, the default process for multiple subscribers.. is cold. This means that we create a new
            stream for each subscriber.

            Let's say for a moment that our range() factory is generating a number of events based on input from a
            user that suggests they want X number Widgets (or random numbers in our case)

            Each time our initial Observable fires a number, our map() operator performs a get() operation.
            - in this particular example (and the ones that follow) we are just getting a random number. Let's
            pretend that this example is a getOrCreateSomeThing() related to our application. This means that our
            fabricated algorithm is to spit out a range of numbers, and get a piece of data for each one.

            If the user is making a distributed request that requires the same data sent out to different compute
            resources for various use cases, we are going to run into a problem.

            If a new stream is generated for each Observer, each stream is going to execute map() separately,
            resulting in DIFFERENT random numbers (widgets/data/whatever) being sent to each observer.

            NOTE: (A real world example might be concurrency mechanisms in ecommerce order management systems. If we
            are relying on event consistency in an event source design model, this is now effectively broken, because
            an Observer designed represent an Inventory/Warehouse management system and an Observable designed to
            represent the actual Order/CC Approval process are going to receive different pieces of data).

            (Yes, there are ways around this and entirely other designs that don't exhibit this fault
             but I'm just trying to relate the material.)



         */
        Observable<Integer> randomInts = Observable.range(1, 5)
                .map(integer -> Generic.getRandom());

        /*
            Down to 2 subscribers. Print is dead.
         */
        randomInts.subscribe(integer -> System.out.println("Observer 1: " + integer));
        randomInts.subscribe(integer -> System.out.println("Observer 2: " + integer));


        /*
            Look at the output. This looks like a run-of-the-mill Cold Observable.
            Unfortunately, the differences aren't immediately apparent if you don't know what is going on under the
            hood.

            1.) The Observable Factory kicks off a stream. We have 2 subscribe() methods, so we generate 2 streams.
            2.) Each stream has its own instance of the map() operator.

                (This is why each Observer gets DIFFERENT random numbers...)
            3.) Each Observer chews up its own personal stream.

                                Observable
                                /       \
                          Stream 1      Stream 2
                              |             |
                          Map 1         Map 2
                            |               |
                          Observer 1    Observer 2


                ya dig?
         */
    }
}
