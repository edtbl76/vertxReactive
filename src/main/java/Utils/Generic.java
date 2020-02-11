package Utils;

import java.util.Random;

public class Generic {

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static Integer getRandom() {

        // This is the same as below, but we're defaulting to min, max of 0, 1000
        return getRandom(0, 1000);
    }

    public static Integer getRandom(int min, int max) {

        // Swap values if out of order.
        int temp = 0;
        if (min > max) {
            temp = max;
            max = min;
            min = temp;
        }

        return new Random().nextInt((max - min)  + 1) + min;
    }


}
