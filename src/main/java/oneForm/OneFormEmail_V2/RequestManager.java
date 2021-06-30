package oneForm.OneFormEmail_V2;

import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.MultiThreading.TDThreadManager;

import java.util.concurrent.*;
import java.util.logging.Level;

public class RequestManager extends TDThreadManager {
    Semaphore countTicketSemaphore = new Semaphore(1);
    Semaphore andonTicketSemaphore = new Semaphore(1);
    private static int threadsLaunched = 0;

    public RequestManager() {
        super(5, 10, 1, 50);
    }

    public void addServiceRequest(ProcessRequest request, History history, String threadName) throws InterruptedException {
        LoggingSupervisor debug = new LoggingSupervisor(history);
        boolean taskAdded = false;
        int waitTime = 5000;
        while (!taskAdded) {
            try {
                request.addCountTicketSemaphore(countTicketSemaphore);
                request.addAndonTicketSemaphore(andonTicketSemaphore);
                this.addTask(request, history, threadName);
                taskAdded = true;
            }
            catch (RejectedExecutionException exception) {
                history.addEvent(new LoggingEvent("Thread Queue is full. Waiting for " + waitTime / 1000 + " seconds", "addServiceRequest", RequestManager.class, Level.WARNING));
                Thread.sleep(waitTime);
                waitTime += 1000;
                taskAdded = false;
            }
        }
        if (threadsLaunched % 5 == 0) {
            debug.displayLog();
        }
    }
}
