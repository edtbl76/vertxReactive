package Dispose_8;

import io.reactivex.Observable;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LongRunningObservables_5 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ToggleButton toggleButton = new ToggleButton("I'm a button.");
        Label label = new Label();

        Observable<Boolean> switchPositions = values(toggleButton.selectedProperty());

        switchPositions
                .map(position -> position ? "ON" : "OFF")
                .subscribe(label::setText);

        VBox vBox = new VBox(toggleButton, label);
        stage.setScene(new Scene(vBox));
        stage.show();
    }

    // Observable!
    private static<T>Observable<T> values(final ObservableValue<T> observableValue) {

        /*
            Observable Create is wrapped around a resource. In this case our resource is a ChangeListener.
         */
        return Observable.create(observableEmitter -> {

            // Initial push
            observableEmitter.onNext(observableValue.getValue());

            // Listener for pushing mutations
            final ChangeListener<T> listener = (observableValue1, previous, current) -> {
              observableEmitter.onNext(current);
            };
            observableValue.addListener(listener);

            /*
                This handles the disposal of our resource (the listener).
                setCancellable() and setDisposable() are the methods that can do this.

                setCancellable() tells the caller of this method (i.e. values())
                - what to do when dispose() is called.
             */
            observableEmitter.setCancellable(() -> observableValue.removeListener(listener));
        });

    }


}
