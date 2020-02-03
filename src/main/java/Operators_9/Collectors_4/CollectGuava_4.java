package Operators_9.Collectors_4;

import com.google.common.collect.ImmutableList;
import io.reactivex.Observable;

public class CollectGuava_4 {

    public static void main(String[] args) {

        /*
            Observable with a list of companies.
         */
        Observable<String> companies = Observable.just("Google", "Amazon", "Facebook", "HubSpot");

        /*
            Here's the sitch. For whatever reason, our slave driving development manager has said
            I want you to use Google Guava's Immutable List because I SAID SO!

            (And because it helps me demonstrate the use of a 3rd party dependency in a collector that allows me to
            leverage external implementations)

            For those of you who haven't worked with ImmutableList it works like this:
                - call builder() (factory) to create ImmutableList.Builder<T>
                - call add() to stuff the items into the structure
                - call build() this "seals" the list so that there can be no further modifications.

            This is a good example, because sometimes we want to create a collection that we don't want to be modified,
            however we might not start w/ all of the elements.

            An easier way to say this is that we want a data structure to start mutable before it reaches some
            state of "stability" at which point we want it to become immutable.

         */
        companies
                /*
                    Your daddy is an ImmutableList.Builder<T> (we create this by calling builder())
                    And he adds.

                    This is the Kindergarten Cop Operator.
                 */
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                /*
                    This is the call to build().
                    This is a very reactive way of accomplishing the task I mentioned in the comment above.

                 */
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);

    }

}
