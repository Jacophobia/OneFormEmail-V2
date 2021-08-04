package oneForm.OneFormEmail_V2;

import com.google.gson.Gson;
import oneForm.OneFormEmail_V2.DepartmentTickets.*;
import td.api.Attachment;
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
import java.util.Map;
import java.util.concurrent.Semaphore;

import static oneForm.OneFormEmail_V2.RequestCollector.*;


public class ProcessRequest extends TDRunnable {
    // Constants
    private final int ONE_FORM_APPLICATION_ID = 48;
    private final int OFFICE_LIST_1_ATTRIBUTE_ID = 10329;
    private final String ACADEMIC_OFFICE_LIST_VAL = "31724";
    private final String ACCOUNTING_OFFICE_LIST_VAL = "31723";
    private final String ADMISSIONS_OFFICE_LIST_VAL = "31725";
    private final String ADVISING_OFFICE_LIST_VAL = "31727";
    private final String FACILITIES_OFFICE_LIST_VAL = "31728";
    private final String FINANCIAL_AID_OFFICE_LIST_VAL = "31729";
    private final String GENERAL_OFFICE_LIST_VAL = "31730";
    private final String IT_OFFICE_LIST_VAL = "31732";
    private final String SRR_OFFICE_LIST_VAL = "31733";
    private final String UNIVERSITY_STORE_OFFICE_LIST_VAL = "31734";
    private final String PATHWAY_OFFICE_LIST_VAL = "36695";
    private final int COUNT_TICKET_REPORT_ID = 18409;
    private final int OPERATIONS_APP_ID = 42;
    private final int ANDON_ATTRIBUTE_ID = 10376;
    private final int ONEFORM_ANDON_TICKET_ID = 12221;


    // Semaphores
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;

    // Main
    public static TeamDynamix pull;
    public static TeamDynamix push;
    RequestCollector.ACTION_REQUESTED ACTION;
    private OneformTicket oneformTicket;
    private DepartmentTicket departmentTicket;
    private CountTicket countTicket;
    private AndonTicket andonTicket;
    private LoggingSupervisor debug;
    private ArrayList<GeneralTicket> tickets = new ArrayList<>();

    // Other
    private int oneFormTicketID;


    public ProcessRequest(int ticketID, ACTION_REQUESTED ACTION) {
        super(
            new TDLoggingManager(Settings.debug),
            new History(ResourceType.TICKET,
            String.valueOf(ticketID))
        );
        debug = new LoggingSupervisor(this.history);
        oneFormTicketID = ticketID;
        this.ACTION = ACTION;

        String SB = Settings.sandbox ? "SB" : "";
        push = new TeamDynamix(
            System.getenv("TD_API_BASE_URL") + SB,
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
            debug.logError(
                oneformTicket.getId() + "\n" + exceptionMessage.createMessage()
            );
            debug.sendErrorReport();
            throw new TDException(exceptionMessage, debug.getHistory());
        }
    }

    private void processTicketRequest() throws TDException {
        retrieveOneFormTicket();
        debug.log("Oneform Ticket Id: " + oneformTicket.getId());
        if (ACTION != ACTION_REQUESTED.SPAM) {
            createDepartmentTicket();
        }
        manageCountData();
        if (oneformTicket.containsAttribute(ANDON_ATTRIBUTE_ID)) {
            debug.log("Contains Andon Cord attribute.");
            createAndonTicket();
        }
        sendCompletedTickets();
        makeFinalChanges();
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

    private void createDepartmentTicket() {
        assert oneformTicket.containsAttribute(OFFICE_LIST_1_ATTRIBUTE_ID) :
            "The oneform ticket does not contain the office list attribute";
        switch (oneformTicket.getCustomAttribute(OFFICE_LIST_1_ATTRIBUTE_ID)) {

            case PATHWAY_OFFICE_LIST_VAL:
                debug.log("Creating Pathway Ticket");
                departmentTicket = new PathwayTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACCOUNTING_OFFICE_LIST_VAL:
                debug.log("Creating Accounting Ticket");
                departmentTicket = new AccountingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADMISSIONS_OFFICE_LIST_VAL:
                debug.log("Creating Admissions Ticket");
                departmentTicket = new AdmissionsTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADVISING_OFFICE_LIST_VAL:
                debug.log("Creating Advising Ticket");
                departmentTicket = new AdvisingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case GENERAL_OFFICE_LIST_VAL:
                debug.log("Creating General Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACADEMIC_OFFICE_LIST_VAL:
                debug.log("Creating Academic Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FACILITIES_OFFICE_LIST_VAL:
                debug.log("Creating Facilities Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FINANCIAL_AID_OFFICE_LIST_VAL:
                debug.log("Creating Financial Aid Ticket");
                departmentTicket = new FinancialAidTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case IT_OFFICE_LIST_VAL:
                debug.log("Creating IT Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case SRR_OFFICE_LIST_VAL:
                debug.log("Creating SRR Ticket");
                departmentTicket = new SrrTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case UNIVERSITY_STORE_OFFICE_LIST_VAL:
                debug.log("Creating University Store Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            default:
                assert false : "The oneform ticket either has a new office " +
                    "list item which has not been added, or none is selected";
                debug.logError(
                    "Unable to find correct office. Using ByuiTicket as default"
                );
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
        }
        debug.log(
            departmentTicket.getClass().getSimpleName() + " added to tickets"
        );
        tickets.add(departmentTicket);
    }

    private void manageCountData() {
        boolean acquired = false;
        int attempts = 0;
        while (!acquired) {
            try {
                attempts++;
                countTicketSemaphore.acquire();
                acquired = true;
            }
            catch (InterruptedException exception) {
                if (attempts > 10) {
                    debug.logError("Unable to acquire count semaphore");
                    return;
                }
            }
        }
        try {
            ArrayList<Map<String, String>> countReportRows =
                pull.getReport(
                    COUNT_TICKET_REPORT_ID,
                    true,
                    ""
                ).getDataRows();
            boolean found = false;
            for (Map<String, String> row : countReportRows) {
                String requestorName = row.get("CustomerName");
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                if (requestorName.equals(oneformTicket.getAgentName()) &&
                    !found) {
                    debug.log("Agent name : " + requestorName);
                    debug.log("Count Ticket ID : " + ticketId);
                    retrieveCountTicket(ticketId);
                    found = true;
                }
            }
            if (!found) {
                countTicket = new CountTicket(history, oneformTicket, ACTION);
            }
            countTicket.incrementCount();
            countTicketSemaphore.release();
            if (found && !Settings.sandbox) {
                push.editTicket(false, countTicket);
            }
            else {
                tickets.add(countTicket);
            }
        }
        catch (TDException exception){
            debug.logError("Unable to modify count ticket.");
            countTicketSemaphore.release();
            return;
        }
    }

    private void retrieveCountTicket(int countTicketID) throws TDException {
        // TODO: Consolidate this and the retrieve oneform ticket method
        Gson gson = new Gson();
        String json = gson.toJson(
            pull.getTicket(OPERATIONS_APP_ID, countTicketID)
        );
        CountTicket retrievedCountTicket = gson.fromJson(
            json, CountTicket.class
        );
        retrievedCountTicket.initializeTicket(
            debug.getHistory(),
            oneformTicket,
            ACTION
        );
        this.countTicket = retrievedCountTicket;
    }

    private void createAndonTicket() {
        String isAndon = oneformTicket.getCustomAttribute(ANDON_ATTRIBUTE_ID);
        assert isAndon.equals("32086") :
            "The andon ticket value is included, but has an unrecognized value";
        if (oneformTicket.containsAttribute(ONEFORM_ANDON_TICKET_ID)) {
            debug.log("Andon Ticket already created.");
            return;
        }

        boolean acquired = false;
        int attempts = 0;
        while (!acquired) {
            try {
                attempts++;
                this.andonTicketSemaphore.acquire();
                acquired = true;
            } catch (InterruptedException exception) {
                if (attempts > 10) {
                    debug.logError("Unable to acquire Andon Semaphore.");
                    return;
                }
            }
        }

        andonTicket = new AndonTicket(debug.getHistory(), oneformTicket);
        tickets.add(andonTicket);
        this.andonTicketSemaphore.release();
    }

    private void sendCompletedTickets() throws TDException {
        for (GeneralTicket ticket  : tickets) {
            if (Settings.displayTicketBodies)
                debug.log("Uploading " + ticket.toString());
            debug.log(
                "Preparing " + ticket.getClass().getSimpleName() + " for upload"
            );
            ticket.prepareTicketUpload();
            Ticket uploadedTicket = push.createTicket(
                ticket.getAppId(),
                ticket
            );

            ticket.setCreatedTicket(uploadedTicket);
            
            debug.log(
                ticket.getClass().getSimpleName() + " uploaded. ID : " +
                ticket.getId()
            );
        }
        // Prepare and patch the oneform ticket to contain new data
        // about the department ticket.
        oneformTicket.prepareTicketUpload();
        pull.editTicket(false, oneformTicket);
    }

    private void makeFinalChanges() {
        this.addAttachments();
        if (andonTicket != null)
            oneformTicket.setAndonId(andonTicket.getId());
    }

    private void addAttachments() {
        assert departmentTicket.getId() != 0 :
            "The department ticket was not uploaded.";
        assert oneformTicket.getId() != 0 :
            "The oneform ticket is not valid.";
        ArrayList<Attachment> attachments = oneformTicket.getAttachments();
        for (Attachment attachment : attachments) {
            try {
                push.uploadAttachment(
                    departmentTicket.getId(),
                    departmentTicket.getAppId(),
                    attachment
                );
                debug.log("Attachment added.");
            }
            catch (TDException exception) {
                debug.logWarning("Failed to add attachment.");
            }
        }
    }
}
