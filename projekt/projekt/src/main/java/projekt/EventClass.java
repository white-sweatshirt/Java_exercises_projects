package projekt;

import javafx.scene.layout.Pane;

import java.util.concurrent.Semaphore;

public class EventClass extends Thread
{
    Semaphore maxPeople = new Semaphore(10);

    Pane basicPane;
    EventClass(Pane basicPane)
    {
        this.basicPane = basicPane;
    }
    // TIME FOR ACTION

    @Override
    public void run()
    {
        while (true)
        {
            break;
        }
    }

}
