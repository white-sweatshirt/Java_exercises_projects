package projekt;

import javafx.scene.layout.Pane;

import java.util.concurrent.Semaphore;

public class EventClass extends Thread {

    Pane basicPane;

    EventClass(Pane basicPane) {
        this.basicPane = basicPane;
    }

    @Override
    public void run() {
        while (true) {
            break;
        }
    }

}
