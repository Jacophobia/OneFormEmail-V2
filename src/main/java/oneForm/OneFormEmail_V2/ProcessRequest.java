package oneForm.OneFormEmail_V2;

import com.google.gson.Gson;
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
    public static final String ACADEMIC_OFFICE_LIST_VAL = "31724";
    public static final String ACCOUNTING_OFFICE_LIST_VAL = "31723";
    public static final String ADMISSIONS_OFFICE_LIST_VAL = "31725";
    public static final String ADVISING_OFFICE_LIST_VAL = "31727";
    public static final String FACILITIES_OFFICE_LIST_VAL = "31728";
    public static final String FINANCIAL_AID_OFFICE_LIST_VAL = "31729";
    public static final String GENERAL_OFFICE_LIST_VAL = "31730";
    public static final String IT_OFFICE_LIST_VAL = "31732";
    public static final String SRR_OFFICE_LIST_VAL = "31733";
    public static final String UNIVERSITY_STORE_OFFICE_LIST_VAL = "31734";
    public static final String PATHWAY_OFFICE_LIST_VAL = "36695";

    // Semaphores
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;

    // Main
    private TeamDynamix pull;
    private TeamDynamix push;
    RequestCollector.ACTION_REQUESTED ACTION;
    private OneformTicket oneformTicket;
    private DepartmentTicket departmentTicket;
    private CountTicket countTicket;
    private AndonTicket andonTicket;
    private LoggingSupervisor debug;
    private ArrayList<GeneralTicket> tickets = new ArrayList<>();

    // Other
    private int oneFormTicketID;


    public ProcessRequest(int ticketID, RequestCollector.ACTION_REQUESTED ACTION) {
        super(
            new TDLoggingManager(Settings.debug),
            new History(ResourceType.TICKET,
            String.valueOf(ticketID))
        );
        debug = new LoggingSupervisor(this.history);
        oneFormTicketID = ticketID;
        this.ACTION = ACTION;

        String sandboxExtension = "";
        if (Settings.sandbox)
            sandboxExtension = "SB";
        push = new TeamDynamix(
            System.getenv("TD_API_BASE_URL") + sandboxExtension,
            System.getenv("USERNAME"),
            System.getenv("PASSWORD"),
            debug.getHistory()
        );
        pull = new TeamDynamix(
            System.getenv("TD_API_BASE_URL"),
            System.getenv("USERNAME"),
            System.getenv("PASSWORD"),
            debug.getHistory()
        );
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
            FaultTDExceptionMessage exceptionMessage =
                TDExceptionMessageFactory.getFaultTDExceptionMessage(exception);
            throw new TDException(exceptionMessage, this.history);
        }
    }

    private void processTicketRequest() throws TDException {
        retrieveOneFormTicket();
        debug.log(
            this.getClass(),
            "ProcessTicketRequest",
            "Oneform Ticket Id: " + oneformTicket.getId()
        );
        createDepartmentTicket();
        sendCompletedTickets();
    }

    private void sendCompletedTickets() throws TDException {
        for (GeneralTicket ticket  : tickets) {
            if (Settings.displayTicketBodies)
                debug.log(
                    this.getClass(),
                    "ProcessTicketRequest",
                    "Uploading " + ticket.toString()
                );
            debug.log(
                this.getClass(),
                "sendCompletedTickets",
                "Preparing Ticket for upload"
            );
            ticket.prepareTicketUpload();
            Ticket uploadedTicket = push.createTicket(
                ticket.getApplicationID(),
                ticket
            );
            debug.log(
                this.getClass(),
                "sendCompletedTickets",
                "Ticket uploaded. ID : " + uploadedTicket.getId()
            );
        }

        // Prepare and patch the oneform ticket to contain new data
        // about the department ticket.
        oneformTicket.prepareTicketUpload();
        pull.editTicket(false, oneformTicket);
    }

    private void retrieveOneFormTicket() throws TDException {
        Gson gson = new Gson();
        String json = gson.toJson(
            pull.getTicket(ONE_FORM_APPLICATION_ID, oneFormTicketID)
        );
        OneformTicket retrievedOneFormTicket = gson.fromJson(
            json, OneformTicket.class
        );
        retrievedOneFormTicket.initializeTicket(debug.getHistory(), ACTION);
        this.oneformTicket = retrievedOneFormTicket;

    }

    private void createDepartmentTicket() throws TDException {
        assert oneformTicket.containsAttribute(OFFICE_LIST_1_ATTRIBUTE_ID) :
            "The oneform ticket does not contain the office list attribute";
        switch (oneformTicket.getCustomAttribute(OFFICE_LIST_1_ATTRIBUTE_ID)) {

            case PATHWAY_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Pathway Ticket"
                );
                departmentTicket = new PathwayTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACCOUNTING_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Accounting Ticket"
                );
                departmentTicket = new AccountingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADMISSIONS_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Admissions Ticket"
                );
                departmentTicket = new AdmissionsTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADVISING_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Advising Ticket"
                );
                departmentTicket = new AdvisingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case GENERAL_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating General Ticket"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACADEMIC_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Academic Ticket"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FACILITIES_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Facilities Ticket"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FINANCIAL_AID_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating Financial Aid Ticket"
                );
                departmentTicket = new FinancialAidTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case IT_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating IT Ticket"
                );

                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case SRR_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating SRR Ticket"
                );
                departmentTicket = new SrrTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case UNIVERSITY_STORE_OFFICE_LIST_VAL:
                debug.log(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Creating University Store Ticket"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            default:
                assert false : "The oneform ticket either has a new office " +
                    "list item which has not been added, or none is selected";
                debug.logError(
                    this.getClass(),
                    "createDepartmentTicket",
                    "Unable to find correct office. Using ByuiTicket as default"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
        }
        debug.log(
            this.getClass(),
            "createDepartmentTicket",
            "Department ticket added to tickets"
        );
        tickets.add(departmentTicket);
    }
}
