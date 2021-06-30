package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.Exceptions.ExceptionMessages.FaultTDExceptionMessage;
import td.api.Exceptions.ExceptionMessages.TDExceptionMessageFactory;
import td.api.Exceptions.TDException;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.Logging.TDLoggingManager;
import td.api.MultiThreading.TDRunnable;
import td.api.Ticket;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProcessRequest extends TDRunnable {
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;
    private LoggingSupervisor debug;
    private ArrayList<Ticket> tickets;
    private OneformTicket oneformTicket;
    private DepartmentTicket departmentTicket;

    public ProcessRequest(int ticketID) {
        super(new TDLoggingManager(Config.debug), new History(ResourceType.TICKET, String.valueOf(ticketID)));
        debug = new LoggingSupervisor(this.history);

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

    private void processTicketRequest() {

    }
}
