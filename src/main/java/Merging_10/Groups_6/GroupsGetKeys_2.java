package Merging_10.Groups_6;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

public class GroupsGetKeys_2 {

    public static void main(String[] args) {

        /*
            Similar setup as previous example.
            - We've got a stream of strings and a groupBy length.
         */
        Observable<String> observable = Observable.just("its", "fleece", "was", "white", "as", "snow");
        Observable<GroupedObservable<Integer, String>> groupedObservable = observable.groupBy(String::length);

        /*
            Our previous output as ok. It certainly got the job done, but it leaves too much to the imagination. It
            "appears" they have been ordered by string length, but it isn't entirely clear that is the case. What if
            we had a circumstance where the keys were ambiguous based on the result set, and we absolutely needed to
            understand the keys for analysis?

            This is a way to achieve these results:

         */

        groupedObservable.flatMapSingle(
                /*
                    This is a GREAT example of using reduce(). (we're doing this INSIDE our flatMapSingle, as
                    the wiki suggests...

                    - reduce "tallies" all of the events and emits the final total. In the case of strings,
                    it concatenates them together. This can be used to our advantage when displaying the results of
                    grouped emissions (i.e. a formatted list can be displayed to a user)

                    - this is a also a great example of the flexibility of using map() and the reactive paradigm.
                        We are taking the callback variable inside the map operator and using it to interact with
                        the (outer) callback variable from the next hierarchical layer up.
                        (This is all very functional in style. If you haven't had experience with the concept of
                        closures, check that out... it's beyond the scope of this tutorial, but it will certainly
                        help you relate to the material.)

                        At any rate, this functionality allows you to fold the result of one closure (the outer callback)
                        into the result of another (the inner callback), that reflects a composite result set.)

                        This is a very simple way of doing something that might otherwise be quite complex.

                 */
                integerStringGroupedObservable -> integerStringGroupedObservable
                        .reduce("", (current, next) -> current.equals("") ? next : current + "," + next)
                        .map(s -> integerStringGroupedObservable.getKey() + " [" + s + "]")

        ).subscribe(System.out::println);
    }
}
