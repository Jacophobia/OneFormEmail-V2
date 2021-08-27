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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static oneForm.OneFormEmail_V2.RequestCollector.*;


public class ProcessRequest extends TDRunnable {
    // Constants
    private final int    ONE_FORM_APPLICATION_ID       = 48;
    private final int    OFFICE_LIST_ID                = 10329;
    private final String SPAM_OFFICE_ID                = "36076";
    private final int    ONEFORM_DEPT_TICKET_ID        = 11452;
    private final String ACADEMIC_OFFICE_LIST_VAL      = "31724";
    private final String ACCOUNTING_OFFICE_LIST_VAL    = "31723";
    private final String ADMISSIONS_OFFICE_LIST_VAL    = "31725";
    private final String ADVISING_OFFICE_LIST_VAL      = "31727";
    private final String FACILITIES_OFFICE_LIST_VAL    = "31728";
    private final String FINANCIAL_AID_OFFICE_LIST_VAL = "31729";
    private final String GENERAL_OFFICE_LIST_VAL       = "31730";
    private final String IT_OFFICE_LIST_VAL            = "31732";
    private final String SRR_OFFICE_LIST_VAL           = "31733";
    private final String UNI_STORE_OFFICE_LIST_VAL     = "31734";
    private final String PATHWAY_OFFICE_LIST_VAL       = "36695";
    private final int    COUNT_TICKET_REPORT_ID        = 18409;
    private final int    OPERATIONS_APP_ID             = 42;
    private final int    ANDON_ATTRIBUTE_ID            = 10376;
    private final int    ONEFORM_ANDON_TICKET_ID       = 12221;
    private final String ANDON_YES                     = "32086";


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

    /**
     * This initializes the ProcessRequest object. We need to know which
     * action was selected by the email agent, so we know how to handle
     * the ticket. If you want to change the program to sandbox mode so
     * that the tickets are created in sandbox, go to Settings and
     * change sandbox to true.
     * @param ticketID the ticket ID of the ticket we are requesting
     *                 processing for.
     * @param ACTION the action selected by the email agent.
     */
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

    /**
     * This method is used to set the countTicket semaphore. We do it
     * this way so that we don't unintentionally create new semaphores
     * in each thread.
     * @param countTicketSemaphore protects info from being accessed by
     *                             multiple threads at the same time.
     */
    public void addCountTicketSemaphore(Semaphore countTicketSemaphore) {
        this.countTicketSemaphore = countTicketSemaphore;
    }

    /**
     * This method is used to set the andonTicket semaphore. We do it
     * this way so that we don't unintentionally create new semaphores
     * in each thread.
     * @param andonTicketSemaphore protects info from being accessed by
     *                             multiple threads at the same time.
     */
    public void addAndonTicketSemaphore(Semaphore andonTicketSemaphore) {
        this.andonTicketSemaphore = andonTicketSemaphore;
    }

    /**
     * This method is the first method run when a request is received by
     * the program. This method will call the processTicketRequest
     * method and if there are any errors, this will make sure that the
     * errors are logged in the thread's history.
     * @throws TDException this method throws the customized TD
     * exception which shows common errors encountered in the TD
     * package.
     */
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

    /**
     * This method controls the flow of the program for each incoming
     * ticket request. When a request comes in, it will go through the
     * process contained in this method.
     * @throws TDException if an exception is thrown in one of the
     * methods called in processTicketRequest, we would like it to be
     * evaluated and recorded in the previous method.
     */
    private void processTicketRequest() throws TDException {
        retrieveOneFormTicket();

        if (
            ACTION != ACTION_REQUESTED.SPAM &&
            oneformTicket.containsAttribute(OFFICE_LIST_ID) &&
            !oneformTicket.getCustomAttribute(OFFICE_LIST_ID)
                .equals(SPAM_OFFICE_ID) &&
            oneformTicket.isValidRequest()) {
            if (!oneformTicket.containsAttribute(ANDON_ATTRIBUTE_ID)) {
                createAndonTicket();
            }
            createDepartmentTicket();
            if (oneformTicket.containsAttribute(ONEFORM_DEPT_TICKET_ID)) {
                assert oneformTicket.containsAttribute(ONEFORM_DEPT_TICKET_ID) :
                    "Oneform ticket must contain the Department Ticket ID " +
                    "attribute to retrieve a department ticket";
                assert departmentTicket != null :
                    "Department ticket was not initialized";
                debug.logNote(
                    "Department Ticket already created. ID: " +
                    oneformTicket.getCustomAttribute(ONEFORM_DEPT_TICKET_ID)
                );
                retrieveDepartmentTicket(
                    Integer.parseInt(
                        oneformTicket.getCustomAttribute(ONEFORM_DEPT_TICKET_ID)
                    ),
                    departmentTicket.getAppId(),
                    departmentTicket.getClass()
                );
            }
            tickets.add(departmentTicket);
            debug.logNote(
                departmentTicket.getClass().getSimpleName() + " added."
            );
        }
        manageCountData();
        sendCompletedTickets();
        makeFinalChanges();
    }

    /**
     * The Teamdynamix package returns an object of the ticket class
     * when you use the method getTicket, but we need the oneform ticket
     * to be an object of the OneformTicket class. This method retrieves
     * a ticket, deserializes it, and re-serializes it as a
     * OneformTicket.
     * @throws TDException this will throw an exception if the api call
     * fails.
     */
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
        oneformTicket.setRetrieved(true);
        this.tickets.add(oneformTicket);
        debug.log("Oneform Ticket Id: " + oneformTicket.getId());
    }

    /**
     * This method determines what type of department ticket we will be
     * working with throughout the rest of the program. It checks which
     * office list option was selected by the email agent and, based on
     * this, determines what type of ticket we need to create. If you
     * create a new ticket class, add the case to this switch statement
     * and create a new class which inherits from DepartmentTicket.
     */
    private void createDepartmentTicket() {
        assert oneformTicket.containsAttribute(OFFICE_LIST_ID) :
            "The oneform ticket does not contain the office list attribute";
        switch (oneformTicket.getCustomAttribute(OFFICE_LIST_ID)) {
            case PATHWAY_OFFICE_LIST_VAL:
                debug.logNote("Creating Pathway Ticket");
                departmentTicket = new PathwayTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACCOUNTING_OFFICE_LIST_VAL:
                debug.logNote("Creating Accounting Ticket");
                departmentTicket = new AccountingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADMISSIONS_OFFICE_LIST_VAL:
                debug.logNote("Creating Admissions Ticket");
                departmentTicket = new AdmissionsTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ADVISING_OFFICE_LIST_VAL:
                debug.logNote("Creating Advising Ticket");
                departmentTicket = new AdvisingTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case GENERAL_OFFICE_LIST_VAL:
                debug.logNote("Creating General Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case ACADEMIC_OFFICE_LIST_VAL:
                debug.logNote("Creating Academic Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FACILITIES_OFFICE_LIST_VAL:
                debug.logNote("Creating Facilities Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case FINANCIAL_AID_OFFICE_LIST_VAL:
                debug.logNote("Creating Financial Aid Ticket");
                departmentTicket = new FinancialAidTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case IT_OFFICE_LIST_VAL:
                debug.logNote("Creating IT Ticket");
                departmentTicket = new ByuiTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case SRR_OFFICE_LIST_VAL:
                debug.logNote("Creating SRR Ticket");
                departmentTicket = new SrrTicket(
                    debug.getHistory(),
                    oneformTicket
                );
                break;
            case UNI_STORE_OFFICE_LIST_VAL:
                debug.logNote("Creating University Store Ticket");
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
    }

    /**
     * This method is in charge of retrieving the count ticket for the
     * agent who handled the email ticket we are processing, and
     * updating their count data based on what type of contact was used.
     * We use the count semaphore here to protect the count data from
     * being accessed by multiple sources, which would ruin the
     * incrementation process.
     */
    private void manageCountData() {
        TeamDynamix api = Settings.sandbox ? push : pull;
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
                api.getReport(
                    COUNT_TICKET_REPORT_ID,
                    true,
                    ""
                ).getDataRows();
            boolean found = false;
            for (Map<String, String> row : countReportRows) {
                String requestorName = row.get("CustomerName");
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                if (requestorName != null) {
                    if (requestorName.equals(oneformTicket.getAgentName()) &&
                        !found) {
                        debug.log("Count ticket located.");
                        debug.log("Agent name : " + requestorName);
                        debug.log("Count Ticket ID : " + ticketId);
                        retrieveCountTicket(ticketId);
                        found = true;
                    }
                }
            }
            if (!found) {
                countTicket = new CountTicket(history, oneformTicket, ACTION);
                this.tickets.add(countTicket);
            }
            else {
                countTicket.prepareTicketUpload();
                api.editTicket(false, countTicket);
                countTicketSemaphore.release();
            }
        }
        catch (TDException exception){
            debug.logError("Unable to modify count ticket.");
            countTicketSemaphore.release();
            return;
        }
    }

    /**
     * This method is in charge of retrieving the department ticket,
     * deserializing it, then re-serializing it as an inherited object of
     * the DepartmentTicket class.
     * @param ticketId The ticket ID of the department ticket.
     * @param appId The Application ID of the department ticket.
     * @param ticketClass which class we want to make the department
     *                    ticket.
     * @throws TDException if the api is unsuccessful in retrieving the
     * department ticket, we throw a TDException.
     */
    private void retrieveDepartmentTicket(int ticketId, int appId,
            Class<?> ticketClass) throws TDException {
        Gson gson = new Gson();
        String json;
        if (!Settings.sandbox)
            json = gson.toJson(pull.getTicket(appId, ticketId));
        else
            json = gson.toJson(push.getTicket(appId, ticketId));
        DepartmentTicket ticket = gson.fromJson(json, (Type) ticketClass);
        ticket.setAttributes(new ArrayList<>());
        ticket.initializeTicket(debug.getHistory(), this.oneformTicket);
        this.departmentTicket = ticket;
        debug.logNote(
            "Department ticket retrieved, ID: " + departmentTicket.getId()
        );
        this.departmentTicket.setRetrieved(true);
    }

    /**
     * This method retrieves the count ticket, deserializes it, and
     * re-serializes it as a CountTicket object.
     * @param countTicketID The ticket ID of the count ticket.
     * @throws TDException if the api is unsuccessful in retrieving the
     * department ticket, we throw a TDException.
     */
    private void retrieveCountTicket(int countTicketID) throws TDException {
        // TODO: Consolidate this and the retrieve oneform ticket method
        TeamDynamix api = Settings.sandbox ? push : pull;
        Gson gson = new Gson();
        String json = gson.toJson(
            api.getTicket(OPERATIONS_APP_ID, countTicketID)
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
        this.countTicket.setRetrieved(true);
    }

    /**
     * This method creates an andon cord ticket. These tickets are used
     * by the Customer Experience Department to work on issues that are
     * spotted by our email agents. If the email agent selects the Andon
     * Cord checkbox on the email oneform, then we need to notify
     * Customer Experience by creating an AndonTicket.
     */
    private void createAndonTicket() {
        if (!oneformTicket.containsAttribute(ANDON_ATTRIBUTE_ID))
            return;
        String isAndon = oneformTicket.getCustomAttribute(ANDON_ATTRIBUTE_ID);
        assert isAndon.equals(ANDON_YES) :
            "The andon ticket value is included, but has an unrecognized value";
        if (oneformTicket.containsAttribute(ONEFORM_ANDON_TICKET_ID)) {
            debug.log("Andon Ticket already created.");
            return;
        }

        andonTicket = new AndonTicket(debug.getHistory(), oneformTicket);
        tickets.add(andonTicket);
    }

    /**
     * This method uploads all completed tickets to the Teamdynamix
     * servers. It sends all tickets contained in the tickets in the
     * tickets ArrayList.
     * @throws TDException if the api is unsuccessful in retrieving the
     * department ticket, we throw a TDException.
     */
    private void sendCompletedTickets() throws TDException {
        for (GeneralTicket ticket  : tickets) {
            assert ticket != null : "A ticket has not been initialized";
            if (Settings.displayTicketBodies)
                debug.log("Uploading " + ticket);
            debug.logNote(
                "Preparing " + ticket.getClass().getSimpleName() + " for upload"
            );

            uploadTicket(ticket);

            debug.log(
                ticket.getClass().getSimpleName() + " uploaded. ID : " +
                ticket.getId()
            );
        }
    }

    /**
     * Some tickets contain information that we only have access to
     * after they have been uploaded, so we use this method to work with
     * that information.
     * @throws TDException this is thrown if one of the methods fails
     * while trying to connect to the Teamdynamix server.
     */
    private void makeFinalChanges() throws TDException {
        if (departmentTicket != null && !departmentTicket.isRetrieved()) {
            this.addAttachments();
            oneformTicket.setDepartmentId(departmentTicket.getId());
        }
        if (andonTicket != null)
            oneformTicket.setAndonId(andonTicket.getId());
        uploadTicket(oneformTicket);

        if (!countTicket.isRetrieved())
            countTicketSemaphore.release();

    }

    /**
     * This method is responsible for uploading tickets and handling
     * preparatory processes that need to occur every time a ticket is
     * uploaded.
     * @param ticket an object that inherits from the GeneralTicket
     *               class.
     * @throws TDException if the ticket upload fails this will throw a
     * TDException.
     */
    private void uploadTicket(GeneralTicket ticket) throws TDException {
        ticket.prepareTicketUpload();

        Ticket uploadedTicket = null;
        try {
            if (!ticket.isRetrieved())
                uploadedTicket = push.createTicket(ticket.getAppId(), ticket);
            else {
                if (ticket.getClass() == oneformTicket.getClass())
                    uploadedTicket = pull.editTicket(false, ticket);
                else
                    uploadedTicket = push.editTicket(false, ticket);
            }
        }
        catch (TDException exception) {
            assert false : "Unable to upload the ticket";
            debug.logError(
                "Unable to upload " + ticket.getClass().getSimpleName() +
                ", the Office List may have been switched by the agent."
            );
        }
        assert uploadedTicket != null :
            "The uploaded ticket was never initialized.";

        ticket.setCreatedTicket(uploadedTicket);
    }

    /**
     * This method retrieves all attachments from the oneform ticket and
     * attaches them to the department ticket.
     */
    private void addAttachments() {
        assert oneformTicket != null :
            "The oneform ticket has not been initialized";
        assert departmentTicket != null :
            "This method is being called unnecessarily, the department " +
            "ticket has not been initialized.";
        assert departmentTicket.getId() != 0 :
            "The department ticket was not uploaded.";
        assert oneformTicket.getId() != 0 :
            "The oneform ticket is not valid.";
        ArrayList<Attachment> attachments = oneformTicket.getAttachments();
        for (Attachment attachment : attachments) {
            try {
                byte[] contents = pull.getAttachmentContents(attachment);
                push.uploadAttachment(
                    departmentTicket.getId(),
                    departmentTicket.getAppId(),
                    contents,
                    attachment.getName()
                );
                debug.logNote("Attachment added.");
            }
            catch (TDException exception) {
                debug.logWarning("Failed to add attachment.");
            }
        }
    }
}
