package oneForm.OneFormEmail_V2;

import td.api.Exceptions.ExceptionMessages.FaultTDExceptionMessage;
import td.api.Exceptions.ExceptionMessages.TDExceptionMessageFactory;
import td.api.Exceptions.TDException;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.Logging.TDLoggingManager;
import td.api.MultiThreading.TDRunnable;

import java.util.concurrent.Semaphore;

public class ProcessRequest extends TDRunnable {
    public static TDLoggingManager log = new TDLoggingManager(true);
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;

    public ProcessRequest(int ticketID) {
        super(log, new History(ResourceType.TICKET, String.valueOf(ticketID)));
    }

    public void addCountTicketSemaphore(Semaphore countTicketSemaphore) {
        this.countTicketSemaphore = countTicketSemaphore;
    }

    public void addAndonTicketSemaphore(Semaphore andonTicketSemaphore) {
        this.andonTicketSemaphore = andonTicketSemaphore;
    }

    @Override
    public void executeTask() throws TDException {
        try {
            processTicketRequest();
        }
        catch (Exception exception) {
            this.countTicketSemaphore.release();
            this.andonTicketSemaphore.release();
            FaultTDExceptionMessage exceptionMessage = TDExceptionMessageFactory.getFaultTDExceptionMessage(exception);
            throw new TDException(exceptionMessage, this.history);
        }
    }

    public void processTicketRequest() {

    }
}
