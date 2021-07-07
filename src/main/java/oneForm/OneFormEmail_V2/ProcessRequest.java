package oneForm.OneFormEmail_V2;

import com.google.gson.Gson;
import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import oneForm.OneFormEmail_V2.DepartmentTickets.*;
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

import static java.lang.Integer.parseInt;


public class ProcessRequest extends TDRunnable {
    // Constants
    public static final int ONE_FORM_APPLICATION_ID = 48;
    public static final int OFFICE_LIST_1_ATTRIBUTE_ID = 10329;
    public static final int ACADEMIC_OFFICE_LIST_VAL = 31724;
    public static final int ACCOUNTING_OFFICE_LIST_VAL = 31723;
    public static final int ADMISSIONS_OFFICE_LIST_VAL = 31725;
    public static final int ADVISING_OFFICE_LIST_VAL = 31727;
    public static final int FACILITIES_OFFICE_LIST_VAL = 31728;
    public static final int FINANCIAL_AID_OFFICE_LIST_VAL = 31729;
    public static final int GENERAL_OFFICE_LIST_VAL = 31730;
    public static final int IT_OFFICE_LIST_VAL = 31732;
    public static final int SRR_OFFICE_LIST_VAL = 31733;
    public static final int UNIVERSITY_STORE_OFFICE_LIST_VAL = 31734;
    public static final int PATHWAY_OFFICE_LIST_VAL = 36695;

    // Semaphores
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;

    // Main
    private TeamDynamix api;
    private OneformTicket oneformTicket;
    private DepartmentTicket departmentTicket;
    private CountTicket countTicekt;
    private AndonTicket andonTicket;
    private LoggingSupervisor debug;
    private ArrayList<GeneralTicket> tickets = new ArrayList<>();

    // Other
    private int oneformTicketID;


    public ProcessRequest(int ticketID) {
        super(new TDLoggingManager(Settings.debug), new History(ResourceType.TICKET, String.valueOf(ticketID)));
        debug = new LoggingSupervisor(this.history, ProcessRequest.class);
        oneformTicketID = ticketID;

        String sandboxExtension = "";
        if (Settings.sandbox)
            sandboxExtension = "SB";
        api = new TeamDynamix(
                System.getenv("TD_API_BASE_URL") + sandboxExtension,
                        System.getenv("USERNAME"),
                        System.getenv("PASSWORD"),
                        debug.getHistory());
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
        oneformTicket = retrieveOneFormTicket();
        debug.log("ProcessTicketRequest", "Oneform Ticket Id: " + oneformTicket.getId());
        tickets.add(oneformTicket);

        for (GeneralTicket ticket  : tickets) {
            if (Settings.displayTicketBodies)
                debug.log("ProcessTicketRequest", "Uploading " + ticket.toString());
            ticket.uploadTicket(ticket, ticket.getApplicationID());
        }
    }

    private OneformTicket retrieveOneFormTicket() throws TDException {
        Gson gson = new Gson();
        String json = gson.toJson(api.getTicket(ONE_FORM_APPLICATION_ID, oneformTicketID));
        OneformTicket retrievedOneFormTicket = gson.fromJson(json, OneformTicket.class);
        retrievedOneFormTicket.initializeTicket(api, debug.getHistory(), retrievedOneFormTicket);
        return retrievedOneFormTicket;
    }

    private void createDepartmentTicket() throws TDException {
        assert oneformTicket.containsAttribute(OFFICE_LIST_1_ATTRIBUTE_ID) : "The oneform ticket does not contain the office list attribute";
        switch (parseInt(oneformTicket.getAttribute(OFFICE_LIST_1_ATTRIBUTE_ID))) {
            case PATHWAY_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Pathway Ticket");
                departmentTicket = new PathwayTicket(api, debug.getHistory(), oneformTicket);
            }
            case ACCOUNTING_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Accounting Ticket");
                departmentTicket = new AccountingTicket(api, debug.getHistory(), oneformTicket);
            }
            case ADMISSIONS_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Admissions Ticket");
                departmentTicket = new AdmissionsTicket(api, debug.getHistory(), oneformTicket);
            }
            case ADVISING_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Advising Ticket");
                departmentTicket = new AdvisingTicket(api, debug.getHistory(), oneformTicket);
            }
            case GENERAL_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating General Ticket");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
            case ACADEMIC_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Academic Ticket");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
            case FACILITIES_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Facilities Ticket");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
            case FINANCIAL_AID_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating Financial Aid Ticket");
                departmentTicket = new FinancialAidTicket(api, debug.getHistory(), oneformTicket);
            }
            case IT_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating IT Ticket");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
            case SRR_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating SRR Ticket");
                departmentTicket = new SrrTicket(api, debug.getHistory(), oneformTicket);
            }
            case UNIVERSITY_STORE_OFFICE_LIST_VAL: {
                debug.log("createDepartmentTicket", "Creating University Store Ticket");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
            default: {
                assert false : "The oneform ticket either has a new office list item which has not been added, or none is selected";
                debug.logError("createDepartmentTicket", "Unable to find correct office. Using ByuiTicket as default");
                departmentTicket = new ByuiTicket(api, debug.getHistory(), oneformTicket);
            }
        }
    }

    /**
     * This function will take a General ticket's child class and return it as a
     * ticket object which can interact with the TD api.
     * @return a ticket object which can be sent to the api.
     */
    private Ticket convertToTicket(GeneralTicket ticket) {
        Gson gson = new Gson();
        String json = gson.toJson(ticket);
        return gson.fromJson(json, Ticket.class);
    }
}
