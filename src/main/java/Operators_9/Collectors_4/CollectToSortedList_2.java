package Operators_9.Collectors_4;

import io.reactivex.Observable;

import java.util.Comparator;

public class CollectToSortedList_2 {

    public static void main(String[] args) {

        Observable<Integer> numbers = Observable.just(6,9,7,3,5,4,1,8,2);

        // No introduction needed, we've already met.
        System.out.print("Input Order (Unsorted): ");
        numbers
                .toList()
                .subscribe(System.out::println);

        // Demonstrates sorting based on natural order.
        System.out.print("Sorted Order          : " );
        numbers
                .toSortedList()
                .subscribe(System.out::println);

        // Reverse Sorting using provided Comparator
        System.out.print("Reverse Sorted Order  : ");
        numbers
                .toSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }
}
