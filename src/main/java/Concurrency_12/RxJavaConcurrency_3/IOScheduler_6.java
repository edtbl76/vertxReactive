package Concurrency_12.RxJavaConcurrency_3;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOScheduler_6 {

    public static void main(String[] args)  {

        String file = "/Users/emangini/IdeaProjects/vertxReactive/pom.xml";

        /*
            This is a fairly silly example of an IO work load.
            I've started out w/ a try-with-resources block that reads in my pom file.
         */
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {


            /*
                Here is an example of an Observable that is reading in data from a file system, one line at a time.
                This is more "finite", because I have a single file, but this could easily be tailing a file or
                stream that is constantly being written to.

                NOTE: IF this were an input stream, I'd recommend replacing the Observable w/ a Flowable so that
                we can prevent overrunning the final observer. (we'll talk about these later.)

                our subscribeOn is going to split this up into asynchronous IO tasks (dynamically created and managed)

                we are using blockingSubscribe() to provide for our keepalive, which just prints out the line
                we read.
             */
            Observable.fromIterable(bufferedReader.lines()::iterator)
                    .subscribeOn(Schedulers.io())
                    .map(String::toUpperCase)  // For Sticks and Giggles...
                    .blockingSubscribe(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
