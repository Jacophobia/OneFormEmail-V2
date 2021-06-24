package oneForm.OneFormEmail_V2;

import oneForm.Runnables.ServiceRequest;
import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.MultiThreading.TDThreadManager;

import java.util.concurrent.*;
import java.util.logging.Level;

public class RequestManager extends TDThreadManager {
    Semaphore countTicketSemaphore = new Semaphore(1);
    Semaphore andonTicketSemaphore = new Semaphore(1);

    public RequestManager() {
        super(5, 10, 1, 50);
    }

    public void addServiceRequest(ServiceRequest runnable, History history, String threadName) throws InterruptedException {
        boolean taskAdded = false;
        int waitTime = 5000;
        while (!taskAdded) {
            try {
                runnable.addCountTicketSemaphore(countTicketSemaphore);
                runnable.addAndonTicketSemaphore(andonTicketSemaphore);
                this.addTask(runnable, history, threadName);
                taskAdded = true;
            }
            catch (RejectedExecutionException exception) {
                history.addEvent(new LoggingEvent("Thread Queue is full. Waiting for " + waitTime + "milliseconds", "addServiceRequest", RequestManager.class, Level.WARNING));
                Thread.sleep(waitTime);
                waitTime += 1000;
                taskAdded = false;
            }
        }
    }
}
