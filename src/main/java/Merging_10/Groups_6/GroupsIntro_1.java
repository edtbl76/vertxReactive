package Merging_10.Groups_6;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

public class GroupsIntro_1 {

    public static void main(String[] args) {

        // Pushin' some strings.
        Observable<String> srv = Observable.just("mary", "had", "a", "little", "lamb");

        // We've grouped the events based on their lengths.
        Observable<GroupedObservable<Integer, String>> stringLength =
                srv.groupBy(String::length);

        /*
            Remember when we said "only use Collections for logical groupings???"
            (hint.. it is waaaay back in basic operators, section 9)

            Groups = logical GROUPings.

            Just a suggestion. reduce() and collect() also work, but in this case, I felt it was probably
            prudent to remind y'all of some best practices.

            NOTES on output:
            - Since we are reducing this to a collection, we are losing the key.
            - since this was grouped by String length it has been ordered, so the output will be in order.
         */
        stringLength.flatMapSingle(Observable::toList)
                .subscribe(System.out::println);
    }
}
