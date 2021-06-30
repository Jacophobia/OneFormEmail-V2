package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.Exceptions.ExceptionMessages.FaultTDExceptionMessage;
import td.api.Exceptions.ExceptionMessages.TDExceptionMessageFactory;
import td.api.Exceptions.TDException;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.Logging.TDLoggingManager;
import td.api.MultiThreading.TDRunnable;
import td.api.TeamDynamix;
import td.api.Ticket;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProcessRequest extends TDRunnable {
    // Constants
    public static final int ONE_FORM_APPLICATION_ID = 48;

    // Semaphores
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;

    // Main
    private TeamDynamix api;
    private OneformTicket oneformTicket;
    private DepartmentTicket departmentTicket;
    private LoggingSupervisor debug;
    private ArrayList<Ticket> tickets;

    // Other
    private int oneformTicketID;

    public ProcessRequest(int ticketID) {
        super(new TDLoggingManager(Settings.debug), new History(ResourceType.TICKET, String.valueOf(ticketID)));
        debug = new LoggingSupervisor(this.history);
        oneformTicketID = ticketID;
        if (Settings.sandbox)
            api = new TeamDynamix(System.getenv("TD_API_BASE_URL") + "SB",
                    System.getenv("USERNAME"),
                    System.getenv("PASSWORD"),
                    debug.getHistory());
        else {
            api = new TeamDynamix(System.getenv("TD_API_BASE_URL"),
                    System.getenv("USERNAME"),
                    System.getenv("PASSWORD"),
                    debug.getHistory());
        }

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

    private void processTicketRequest() throws TDException {
        oneformTicket = (OneformTicket) api.getTicket(ONE_FORM_APPLICATION_ID, oneformTicketID);
        debug.log("Oneform Ticket Id: " + oneformTicket.getId());
    }

    private DepartmentTicket getDepartmentTicket() {
        return null;
    }
}
