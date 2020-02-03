package Operators_9.Collectors_4;

import io.reactivex.Observable;

import java.util.concurrent.CopyOnWriteArrayList;

public class CollectToList_1 {

    public static void main(String[] args) {

        /*
            Think about this for a moment before reading the code.
            When we dump this to a list, we are going to get ALL OF THE EVENTS AT ONCE.
            - However, this also means we have to wait for all of them to be collected before
            proceeding.

                - YES... this tends to violate the principles of reactive programming.
         */
        Observable.just("Stanley", "Lombardi", "WhateverTheNBAHas", "OctoberClassic")
                .toList()
                .subscribe(System.out::println);

        /*
            This demonstrates the use of a capacityHint to "warm" the ArrayList (default)
         */
        Observable.range(1, 10)
                .toList(10)
                .subscribe(System.out::println);

        /*
            We can also provide a different type of List.. not that the output really differs.
         */
        Observable.just("shopping list", "check list", "Santa's list")
                .toList(CopyOnWriteArrayList::new)
                .subscribe(System.out::println);

    }
}
