package ColdAndHot;

import io.reactivex.Observable;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SomeLikeItHot_3 extends Application {

    @Override
    public void start(Stage stage)  {
        ToggleButton toggleButton = new ToggleButton("TOGGLE");
        Label label = new Label();

        Observable<Boolean> switchStates = values(toggleButton.selectedProperty());

        switchStates.map(b -> b ? "OFF" : "ON").subscribe(label::setText);

        VBox vBox = new VBox(toggleButton, label);

        stage.setScene(new Scene(vBox));
        stage.show();
    }

    private static <T>Observable<T> values(final ObservableValue<T> observableValue) {
        return Observable.create(observableEmitter -> {
            // Initial state
            observableEmitter.onNext(observableValue.getValue());

           // Changed State
           final ChangeListener<T> listener = (value, previous, current) -> observableEmitter.onNext(current);
           observableValue.addListener(listener);
        });
    }
}
