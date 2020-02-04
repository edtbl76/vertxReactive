package Operators_9.Collectors_4;

import io.reactivex.Observable;

import java.util.HashSet;

public class CollectCollect_4 {


    public static void main(String[] args) {

        /*
            I call this the Kindergarten Cop operator.
                - "Who is your Daddy? and What does He Do?"

                In this case:
                - Your daddy is a HashSet
                - He Adds.
         */
        Observable.just(1, 2, 3, 4, 5, 6)
                .collect(HashSet::new, HashSet::add)
                .subscribe(System.out::println);

    }
}
