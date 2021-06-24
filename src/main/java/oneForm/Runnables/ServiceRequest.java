package oneForm.Runnables;

import oneForm.*;
import oneForm.IDs;
import oneForm.requiredInfoGets;
import td.api.*;
import td.api.Exceptions.*;
import td.api.Exceptions.ExceptionMessages.FaultTDExceptionMessage;
import td.api.Exceptions.ExceptionMessages.TDExceptionMessageFactory;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.*;
import td.api.MultiThreading.TDRunnable;

import java.util.logging.Level;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


import static java.lang.Integer.parseInt;

public class ServiceRequest extends TDRunnable {
    private Semaphore countTicketSemaphore;
    private Semaphore andonTicketSemaphore;
    public requiredInfoGets reqInfo; //Class that will get required ticket creation variables.
    public nonRequiredInfoGets nonReqInfo; //This will get the non-required ticket creation variables.
    public static final int ONE_FORM_APPLICATION_ID = 48;
    public static final int ONE_FORM_OFFICE_LIST_1_ATTRIBUTE_ID = 10329;
    public static final String ALBUS_DUMBLEDORE_UID = "e578bb85-0041-e511-80d1-005056ac5ec6";
    public final int ticketID;
    public static TDLoggingManager log = new TDLoggingManager(false);

    public enum EmailActionStep {
        SPAM, ESCALATE, RESOLVED_BY_KB
    }


    public ServiceRequest(int ticketID) {
        super(log, new History(ResourceType.TICKET, String.valueOf(ticketID)));
        this.ticketID = ticketID;
        nonReqInfo = new nonRequiredInfoGets(history);
        reqInfo = new requiredInfoGets(history);

    }

    @Override
    public void executeTask() throws TDException {
        try {
            program();
        }
        catch (Exception exception) {
            this.countTicketSemaphore.release();
            this.andonTicketSemaphore.release();
            FaultTDExceptionMessage exceptionMessage = TDExceptionMessageFactory.getFaultTDExceptionMessage(exception);
            throw new TDException(exceptionMessage, this.history);
        }
    }

    public void program() throws Exception {

        try {
            TeamDynamix api = new TeamDynamix(System.getenv("TD_API_BASE_URL"),
                    System.getenv("USERNAME"),
                    System.getenv("PASSWORD"),
                    history);

            int departmentTicketAppID = getAppId(ticketID); //The application that the new ticket will be created in.
            history.addEvent(new LoggingEvent("Department Ticket APP ID: " + departmentTicketAppID, "executeTask", ServiceRequest.class, Level.INFO));

            history.addEvent(new LoggingEvent("Retrieving OneForm Ticket for: " + ticketID, "executeTask" , ServiceRequest.class, Level.INFO));
            Ticket oneFormTicket = api.getTicket(ONE_FORM_APPLICATION_ID, ticketID);
            history.addEvent(new LoggingEvent("OneForm ticket ID is " + oneFormTicket.getId(), "executeTask", ServiceRequest.class, Level.INFO));

            ArrayList<CustomAttribute> attributes = oneFormTicket.getAttributes();

            //Get the action that the agent took from the oneForm...
            String action = "";
            for (CustomAttribute customAttribute : attributes) {

                if (customAttribute.getId() == oneForm.IDs.EMAIL_ACTIONS_ATTR) {
                    action = customAttribute.getValue();
                }
            }


            //Now we will create the department ticket if not found, or just update it
            boolean hasDeptID = false;
            String deptTicketID = "";
            //Check if Dept ID is already there
            for (CustomAttribute attribute : attributes) {
                if (attribute.getId() == oneForm.IDs.ONE_FORM_DEPT_TICK_ID) {
                    hasDeptID = true;
                    deptTicketID = attribute.getValue();
                }
            }

            //Get the name of the agent who most recently modified the ticket
            //Search for the first feed that isn't created by BSC robot

            ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId());
            String agentName = "";
            String agentUID = "";
            for (ItemUpdate itemUpdate : feed) {
                if (!itemUpdate.getCreatedFullName().equals("BSC Robot") && !itemUpdate.getCreatedFullName().equals("System")) {
                    agentName = itemUpdate.getCreatedFullName();
                    agentUID = itemUpdate.getCreatedUid();
                    break;
                }
            }

            // Default to Albus Dumbledore if no agent is found
            if (agentName.isEmpty()) {
                history.addEvent(new LoggingEvent("No agent name was found. Defaulting to Albus Dumbledore", "program", ServiceRequest.class, Level.WARNING));
                agentName = "Albus Dumbledore";
            }
            if (agentUID.isEmpty()) {
                history.addEvent(new LoggingEvent("No agent UID was found. Defaulting to Albus Dumbledore", "program", ServiceRequest.class, Level.WARNING));
                agentUID = ALBUS_DUMBLEDORE_UID;
            }

            //Check for each of the 3 cases, escalate, reply, spam...

            //Is Spam?
            history.addEvent(new LoggingEvent("One Form App ID: " + oneFormTicket.getAppId(), "executeTask", ServiceRequest.class, Level.INFO));
            if (departmentTicketAppID == 0) {
                history.addEvent(new LoggingEvent("Action Taken marked as SPAM", "executeTask", ServiceRequest.class, Level.INFO));
                this.manageCountTicket(api, agentUID, departmentTicketAppID, oneFormTicket, agentName, EmailActionStep.SPAM); // 3
            }

            //Replied?
            else if (action.equals(oneForm.IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB)) {
                history.addEvent(new LoggingEvent("Action Taken marked as RESOLVED BY KB", "executeTask", ServiceRequest.class, Level.INFO));
                //First increment the Count ticket...

                this.manageCountTicket(api ,agentUID, departmentTicketAppID, oneFormTicket, agentName, EmailActionStep.RESOLVED_BY_KB); // 1

                // Now we update/create the Dep ticket....
                // If there is a dept ticket already....
                if (hasDeptID) {
                    history.addEvent(new LoggingEvent("Department ticket already created.", "executeTask", ServiceRequest.class, Level.INFO));
                    Ticket departmentTicket = api.getTicket(departmentTicketAppID, Integer.parseInt(deptTicketID));


                    Ticket ticket = updateTicketFeed(oneFormTicket, departmentTicket, api, feed);
                    CustomAttribute sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(departmentTicketAppID), nonReqInfo.getSentToLevel2Value(departmentTicketAppID, attributes));
                    ticket.getAttributes().add(sentToLevel2);

                    this.manageAndonCordTicket(ticket, departmentTicketAppID, api);

                    api.editTicket(false, ticket);

                    api.editTicket(false, oneFormTicket);
                }
                //If there is no dept ticket already...
                if (!hasDeptID) {
                    history.addEvent(new LoggingEvent("Department ticket not yet created.", "executeTask", ServiceRequest.class, Level.INFO));
                    Ticket departmentTicket = createNewTicket(oneFormTicket, attributes, departmentTicketAppID, api);
                    this.uploadNewTickets(
                            departmentTicketAppID,
                            departmentTicket,
                            oneFormTicket,
                            buildAndonTicket(oneFormTicket, departmentTicketAppID, attributes));
                    this.manageAndonCordTicket(oneFormTicket, departmentTicketAppID, api);
                    // this.addAttachments(oneFormTicket, departmentTicket, api);
                }
            }

            //Escalated?
            else if (action.equals(oneForm.IDs.EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                history.addEvent(new LoggingEvent("Actions Taken marked as ESCALATED TICKET", "executeTask", ServiceRequest.class, Level.INFO));
                //First increment the Count ticket...
                this.manageCountTicket(api, agentUID, departmentTicketAppID, oneFormTicket, agentName, EmailActionStep.ESCALATE); // 2

                //Now we update/create the Dep ticket....
                //If there is a dept ticket already....
                if (hasDeptID) {
                    history.addEvent(new LoggingEvent("Department ticket already created.", "executeTask", ServiceRequest.class, Level.INFO));
                    Ticket departmentTicket = api.getTicket(departmentTicketAppID, Integer.parseInt(deptTicketID));


                    Ticket ticket = updateTicketFeed(oneFormTicket, departmentTicket, api, feed);
                    CustomAttribute sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(departmentTicketAppID), nonReqInfo.getSentToLevel2Value(departmentTicketAppID, attributes));
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("STATUS ID ID: " + ticket.getStatusId(), "executeTask", ServiceRequest.class, Level.INFO));
                    this.manageAndonCordTicket(oneFormTicket, departmentTicketAppID, api);

                    api.editTicket(false, ticket);
                    api.editTicket(false, oneFormTicket);
                }
                //If there is no dept ticket already...
                if (!hasDeptID) {
                    history.addEvent(new LoggingEvent("Department ticket not yet created.", "executeTask", ServiceRequest.class, Level.INFO));
                    Ticket deptTicket = createNewTicket(oneFormTicket, attributes, departmentTicketAppID, api);
                    uploadNewTickets(
                            departmentTicketAppID,
                            deptTicket,
                            oneFormTicket,
                            buildAndonTicket(oneFormTicket, departmentTicketAppID, attributes));
                    // this.addAttachments(oneFormTicket, deptTicket, api);
                }
            }
        }
        catch (InterruptedException exception) {
            FaultTDExceptionMessage exceptionMessage = TDExceptionMessageFactory.getFaultTDExceptionMessage(exception);
            throw new TDException(exceptionMessage, this.history);
        }
        history.addEvent(new LoggingEvent("Ticket ID: " + ticketID + " FINISHED", "executeTask", ServiceRequest.class, Level.INFO));
    }


    private void addAttachments(Ticket oneFormTicket, Ticket departmentTicket, TeamDynamix api) {
        //Upload attachment
        history.addEvent(new LoggingEvent("oneFormTicket Id is " + oneFormTicket.getId(), "addAttachments", ServiceRequest.class, Level.INFO));
        history.addEvent(new LoggingEvent("departmentTicket Id is " + departmentTicket.getId(), "addAttachments", ServiceRequest.class, Level.INFO));

        List<Attachment> attachments = oneFormTicket.getAttachments();
        history.addEvent(new LoggingEvent("Scanning for attachments", "addAttachments", ServiceRequest.class, Level.INFO));
        for (Attachment attachment : attachments) {
            try {
                history.addEvent(new LoggingEvent("Attachment found.", "addAttachments", ServiceRequest.class, Level.INFO));
                api.uploadAttachment(departmentTicket.getId(), departmentTicket.getAppId(), attachment);
                history.addEvent(new LoggingEvent("Attachment added.", "addAttachments", ServiceRequest.class, Level.INFO));
            }
            catch (TDException exception) {
                history.addEvent(new LoggingEvent("Failed to add attachments.", "addAttachments", ServiceRequest.class, Level.INFO));
            }
        }
    }

    private void manageCountTicket(TeamDynamix api, String agentUID, int appID, Ticket oneFormTicket, String agentName, EmailActionStep choice) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "manageCountTicket", ServiceRequest.class, Level.INFO));
        //CHOICE 1 IS notify, 2 is escalate, 3 is spam
        history.addEvent(new LoggingEvent("Attempting to acquire countTicket Semaphore.", "manageCountTicket", ServiceRequest.class, Level.INFO));
        boolean acquired = false;
        int attempts = 0;
        while (!acquired) {
            try {
                attempts++;
                countTicketSemaphore.acquire(); // problem child here
                history.addEvent(new LoggingEvent("CountTicket Semaphore acquired.", "manageCountTicket", ServiceRequest.class, Level.INFO));
                acquired = true;
            } catch (InterruptedException exception) {
                if (attempts > 10) {
                    history.addEvent(new LoggingEvent("Unable to acquire countTicket semaphore after " + attempts + " attempts. Aborting count ticket process.", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                    return;
                }
                history.addEvent(new LoggingEvent("Unable to acquire countTicket semaphore. Will reattempt.", "manageCountTicket", ServiceRequest.class, Level.WARNING));
            }
        }
        Report report = api.getReport(18409, true, "");

        ArrayList<Map<String, String>> rows = report.getDataRows();
        boolean found = false;
            for (Map<String, String> row : rows) {
                String requestorName = row.get("CustomerName");
                int countID = Math.round(Float.parseFloat(row.get("TicketID"))); //Id of the count ticket if it exists.
                if (requestorName.equals(agentName)) {
                    history.addEvent(new LoggingEvent("NEED TO INCREMENT COUNT TICKET DATA.ROWPARSER", "manageCountTicket", ServiceRequest.class, Level.INFO));

                    Ticket countTicket = api.getTicket(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID, countID);
                    if (choice == EmailActionStep.RESOLVED_BY_KB) {
                        try {
                            incrementNotify(countTicket, api);
                            incrementOverall(countTicket, api);
                        }
                        catch (TDException exception) {
                            history.addEvent(new LoggingEvent("RESOLVED_BY_KB - Error Incrementing count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                        }
                    } else if (choice == EmailActionStep.ESCALATE) {
                        try {
                            incrementEscalate(countTicket, api);
                            incrementOverall(countTicket, api);
                        }
                        catch (TDException exception) {
                            history.addEvent(new LoggingEvent("ESCALATE - Error Incrementing count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                        }

                    } else if (choice == EmailActionStep.SPAM) {

                        try {
                            incrementSpam(countTicket, api);
                            incrementOverall(countTicket, api);
                        }
                        catch (TDException exception) {
                            history.addEvent(new LoggingEvent("SPAM - Error Incrementing count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                        }
                    }

                    found = true;
                    break;
                }
            }

        if (!found) {
            Ticket countTicket = createCountTicket(oneFormTicket, api, agentUID);
            Ticket createdCountTicket = api.getTicket(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID, countTicket.getId());
            if (choice == EmailActionStep.RESOLVED_BY_KB) {
                try {
                    incrementNotify(createdCountTicket, api);
                    incrementOverall(createdCountTicket, api);
                }
                catch (TDException exception) {
                    history.addEvent(new LoggingEvent("RESOLVED_BY_KB - Error Incrementing new count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                }
            }
            else if (choice == EmailActionStep.ESCALATE) {
                try {
                    incrementEscalate(createdCountTicket, api);
                    incrementOverall(createdCountTicket, api);
                }
                catch (TDException exception) {
                    history.addEvent(new LoggingEvent("ESCALATE - Error Incrementing new count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                }
            }
            else if (choice == EmailActionStep.SPAM) {
                try {
                    incrementSpam(createdCountTicket, api);
                    incrementOverall(createdCountTicket, api);
                }
                catch (TDException exception) {
                    history.addEvent(new LoggingEvent("SPAM - Error Incrementing new count ticket", "manageCountTicket", ServiceRequest.class, Level.SEVERE));
                }
            }
        }
        countTicketSemaphore.release();
        history.addEvent(new LoggingEvent("Count ticket semaphore released.", "manageCountTicket", ServiceRequest.class, Level.INFO));
    }

    /**
     * manageAndonCordTicket
     * This method creates an andon cord ticket if the option was selected.
     * @param oneFormTicket - the oneForm ticket object containing all data in the OneForm
     * @param appID - The app id of the department ticket
     * @param api - a copy of the tdApi
     * @throws TDException
     * @throws InterruptedException
     */
    private void manageAndonCordTicket(Ticket oneFormTicket, int appID, TeamDynamix api) throws TDException {
        // history.addEvent(new LoggingEvent("", "manageAndonCordTicket", ServiceRequest.class, Level.INFO));
        boolean acquired = false;
        int attempts = 0;
        while (!acquired) {
            try {
                attempts++;
                this.andonTicketSemaphore.acquire();
                acquired = true;
            } catch (InterruptedException exception) {
                if (attempts > 10) {
                    history.addEvent(new LoggingEvent("Unable to acquire andonTicket semaphore after " + attempts + " attempts. Aborting andon ticket process.", "manageAndonCordTicket", ServiceRequest.class, Level.SEVERE));
                    return;
                }
                history.addEvent(new LoggingEvent("Unable to acquire andonTicket semaphore. Will reattempt.", "manageAndonCordTicket", ServiceRequest.class, Level.WARNING));

            }
        }
        //Quick Andon Cord check before we finish the program
        ArrayList<CustomAttribute> attributes = oneFormTicket.getAttributes();
        for (int i = 0; i < oneFormTicket.getAttributes().size(); i++) {
            if (oneFormTicket.getAttributes().get(i).getValue().equals(oneForm.IDs.BSC_OPERATIONS_ANDON_CORD_NEEDED_YES)) {
                history.addEvent(new LoggingEvent("WE NEED TO CREATE AN ANDON CORD", "manageAndonCordTicket", ServiceRequest.class, Level.INFO));
                //Create the AndonCord Ticket
                Ticket andonCordTicket;
                int andonCordTicketID = -1;
                try {
                    Ticket andonTicket = buildAndonTicket(oneFormTicket, appID, attributes);
                    andonCordTicket = api.createTicket(andonTicket.getAppId(), andonTicket);
                    andonCordTicketID = andonCordTicket.getId();
                }
                catch (TDException exception) {
                    history.addEvent(new LoggingEvent("Error creating andon cord ticket.", "manageAndonCordTicket", ServiceRequest.class, Level.SEVERE));
                }
                history.addEvent(new LoggingEvent("ANDON CORD TICKET ID: " + andonCordTicketID, "manageAndonCordTicket", ServiceRequest.class, Level.INFO));
                this.andonTicketSemaphore.release();
                return;
            }
        }

        this.andonTicketSemaphore.release();
    }

    /**
     * getPathwayId
     * If the pathway radio option was selected, this will determine
     * which application the ticket should be created in. Pathway
     * incidents are handled by a number of departments so it is often
     * different.
     * @param attributes - contains a list of all custom attributes.
     * @return The application ID that the ticket should be created in.
     */
    private int getPathwayAppId(ArrayList<CustomAttribute> attributes) {
            for (CustomAttribute attribute : attributes) {
                if (attribute.getId() == oneForm.IDs.ONE_FORM_TAG_ID) {
                    switch (parseInt(attribute.getValue())) {
                        case oneForm.IDs.BSC_TAG_1_ADVISING:
                            return oneForm.IDs.ADVISING_APPLICATION_ID;
                        case oneForm.IDs.BSC_TAG_1_APPLICATION_QUESTIONS:
                            return oneForm.IDs.ADMISSIONS_APPLICATION_ID;
                        case oneForm.IDs.BSC_TAG_1_CHARGES_PAYMENTS:
                            return oneForm.IDs.ACCOUNTING_APPLICATION_ID;
                        case oneForm.IDs.BSC_TAG_1_FINANCIAL_AID:
                            return oneForm.IDs.FINANCIAL_AID_APPLICATION_ID;
                        case oneForm.IDs.BSC_TAG_1_REGISTRATION_ISSUES:
                            return oneForm.IDs.SRR_APPLICATION_ID;
                        default:
                            return oneForm.IDs.BYUI_TICKETS_APPLICATION_ID;
                    }
                }
            }
            return oneForm.IDs.BYUI_TICKETS_APPLICATION_ID;
        }

     /**
     * getAppId:
     * Here we will get the ID of the application the ticket needs to be sent to.
     *
     * @param ticketID,     the ID of the One Form ticket we will get information from.
     * @return the application ID of the issue.
     */
    int getAppId ( int ticketID ) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "getAppId", ServiceRequest.class, Level.));
        int appID = 0;

        TeamDynamix api = new TeamDynamix(System.getenv("TD_API_BASE_URL"),
                                          System.getenv("USERNAME"),
                                          System.getenv("PASSWORD"),
                                          history);

        Ticket oneFormTicket = api.getTicket(ONE_FORM_APPLICATION_ID, ticketID);

        requiredInfoGets info = new requiredInfoGets(history);
        ArrayList<CustomAttribute> attributes = oneFormTicket.getAttributes();

        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == oneForm.IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {    //<---If the attribute ID of the Office list 1 attribute is found
                switch (parseInt(attribute.getValue())) {
                    case oneForm.IDs.ACADEMIC_OFFICE_LIST_VAL:
                    case oneForm.IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                    case oneForm.IDs.IT_OFFICE_LIST_VAL:
                    case oneForm.IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                    case oneForm.IDs.GENERAL_OFFICE_LIST_VAL:
                    case oneForm.IDs.FACILITIES_OFFICE_LIST_VAL:
                        return oneForm.IDs.BYUI_TICKETS_APPLICATION_ID;
                    case oneForm.IDs.ACCOUNTING_OFFICE_LIST_VAL:
                        return oneForm.IDs.ACCOUNTING_APPLICATION_ID;
                    case oneForm.IDs.ADMISSIONS_OFFICE_LIST_VAL:
                        return oneForm.IDs.ADMISSIONS_APPLICATION_ID;
                    case oneForm.IDs.ADVISING_OFFICE_LIST_VAL:
                        return oneForm.IDs.ADVISING_APPLICATION_ID;
                    case oneForm.IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                        return oneForm.IDs.FINANCIAL_AID_APPLICATION_ID;
                    case oneForm.IDs.SRR_OFFICE_LIST_VAL:
                        return oneForm.IDs.SRR_APPLICATION_ID;
                    case oneForm.IDs.PATHWAY_OFFICE_LIST_VAL:
                        return getPathwayAppId(attributes);
                }
            }
        }
        return appID;
    }


    /**
     * buildAndonTicket:
     * This will create the Andon cord ticket based on information provided from the oneForm ticket
     *
     * @param oneFormTicket, the OneForm ticket that we are getting information from
     * @param appID,         application ID
     * @param attributes,    attributes of the oneForm
     */
    Ticket buildAndonTicket(Ticket oneFormTicket, int appID, ArrayList<CustomAttribute > attributes){
        // history.addEvent(new LoggingEvent("", "buildAndonTicket", ServiceRequest.class, Level.));
        Ticket andonTicket = new Ticket();

        //REQUIRED ONES
        andonTicket.setFormId(oneForm.IDs.BSC_OPERATIONS_ANDON_FORM_ID);
        andonTicket.setTypeId(oneForm.IDs.BSC_OPERATIONS_TYPE_ID);
        andonTicket.setTitle("Andon Cord CX Follow-Up");
        andonTicket.setAccountId(oneFormTicket.getAccountId());
        andonTicket.setStatusId(oneForm.IDs.BSC_OPERATIONS_ANDON_FORM_STATUS_NEW);
        andonTicket.setPriorityId(reqInfo.getPriorityID());
        andonTicket.setRequestorUid(oneFormTicket.getRequestorUid());
        andonTicket.setAppID(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID);

        //OTHERS THAT AREN'T REQUIRED BY TD
        //ONEFORM TICKET ID
        CustomAttribute originalIncidentID = new CustomAttribute(oneForm.IDs.BSC_OPERATIONS_INCIDENT_ATTRIBUTE_ID, Integer.toString(oneFormTicket.getId()));
        andonTicket.getAttributes().add(originalIncidentID);

        //ANDON NOTES FROM ONE FORM
        CustomAttribute andonCordNotes = new CustomAttribute(oneForm.IDs.BSC_OPERATIONS_ANDON_CORD_NOTES_ATTRIBUTE_ID, nonReqInfo.getAndonCordNotes(oneFormTicket.getAttributes()));
        andonTicket.getAttributes().add(andonCordNotes);


        //ADD OFFICE TAG
        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {

                CustomAttribute andonOffice = new CustomAttribute(IDs.BSC_OPERATIONS_OFFICE_ATTRIBUTE_ID, attribute.getChoicesText());
                andonTicket.getAttributes().add(andonOffice);
                break;

            }
        }

        //ADD THE ISSUE TAG
        CustomAttribute andonIssue = new CustomAttribute(oneForm.IDs.BSC_OPERATIONS_ONEFORM_ISSUE_ATTRIBUTE_ID, nonReqInfo.getChoiceTextString(attributes));
        andonTicket.getAttributes().add(andonIssue);

        return andonTicket;

    }


    /**
     * createNewTicket:
     * This will create the new form based on information provided from the oneForm ticket
     *
     * @param oneFormTicket, the OneForm ticket that we are getting information from
     * @param appID,         application ID
     * @param attributes,    attributes of the oneForm
     */
    Ticket createNewTicket (Ticket oneFormTicket, ArrayList < CustomAttribute > attributes, int appID,
                            TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "createNewTicket", ServiceRequest.class, Level.));

        Ticket ticket = new Ticket();

        String officeListID = ""; //This is to find which office has been selected (For use in other functions)
        for (CustomAttribute customAttribute : attributes) {
            if (customAttribute.getId() == oneForm.IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {
                officeListID = customAttribute.getValue();
            }
        }


        int typeID = reqInfo.getTypeID(Integer.parseInt(officeListID));

        //THE REQUIRED ONES
        ticket.setTypeId(reqInfo.getTypeID(Integer.parseInt(officeListID)));
        ticket.setFormId(reqInfo.getFormID(Integer.parseInt(officeListID)));
        ticket.setTitle(oneFormTicket.getTitle());
        ticket.setAccountId(reqInfo.getAccountID(oneFormTicket));
        ticket.setStatusId(reqInfo.getStatusID(attributes));
        ticket.setPriorityId(reqInfo.getPriorityID());
        ticket.setRequestorUid(reqInfo.getRequestorUID(oneFormTicket, attributes, appID, typeID));
        ticket.setSourceId(oneForm.IDs.SOURCE_ID_EMAIL);


        //THE EXTRA attributes that aren't required by TD
        ticket.setDescription(oneFormTicket.getDescription());
        ticket.setAppID(appID);

        ticket.setLocationId(oneForm.IDs.LOCATION_ID);

        CustomAttribute sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(appID), nonReqInfo.getSentToLevel2Value(appID, attributes));
        ticket.getAttributes().add(sentToLevel2);

        //If the ticket is a FA ticket, it will need the first initial of the last name
        if (appID == oneForm.IDs.FINANCIAL_AID_APPLICATION_ID) {
            CustomAttribute lastInitial = new CustomAttribute(oneForm.IDs.LAST_NAME_INITIAL, nonReqInfo.getLastNameInitialID(history, oneFormTicket));
            ticket.getAttributes().add(lastInitial);
        }

        //If the ticket is admissions then we need to check for override email
        if (appID == oneForm.IDs.ADMISSIONS_APPLICATION_ID) {
            CustomAttribute override = new CustomAttribute(oneForm.IDs.ADMISSIONS_OVERIDE_ID, nonReqInfo.getOverrideEmailID(attributes));
            ticket.getAttributes().add(override);
        }

        //If ticket is SRR we need to check for "can't add course"
        if (appID == oneForm.IDs.SRR_APPLICATION_ID) {
            String onlineOrCampus = nonReqInfo.getCampusOrOnline(attributes);
            String onlineFilled = nonReqInfo.getOnlineFormFilled(attributes);
            if (!onlineOrCampus.equals("0")) {
                CustomAttribute onlnOrCampus = new CustomAttribute(oneForm.IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ID, onlineOrCampus);
                ticket.getAttributes().add(onlnOrCampus);
                CustomAttribute onlineFill = new CustomAttribute(oneForm.IDs.SRR_ONLINE_FILLED_ATTRIBUTE_ID, onlineFilled);
                ticket.getAttributes().add(onlineFill);
            }
        }


        //Need to add the string of the tag name to the dept ticket
        CustomAttribute tagName = new CustomAttribute(nonReqInfo.getChoiceTextID(appID), nonReqInfo.getChoiceTextString(attributes));
        ticket.getAttributes().add(tagName);

        //Add the oneForm ticket feed to the department ticket feed
        ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId());

        StringBuilder feedBuild = new StringBuilder();
        for (ItemUpdate itemUpdate : feed) {
            if (!itemUpdate.getCreatedFullName().equals("BSC Robot") && !itemUpdate.getCreatedFullName().equals("System")) {
                String uid = itemUpdate.getCreatedUid();

                User user = api.getUser(uid);

                if (!user.getGroupIDs().toString().contains("80") || itemUpdate.getNotifiedList() != null) {
                    feedBuild.append(itemUpdate.getBody()).append("\n");
                }
            }
            if (itemUpdate.getReplies().size() > 0) {
                for (int j = 0; j < itemUpdate.getReplies().size(); j++) {
                    feedBuild.append(itemUpdate.getReplies().get(j).getBody()).append("\n");
                }
            }
        }

        CustomAttribute attribute = new CustomAttribute(nonReqInfo.getTicketFeedUpdateAttr(appID), feedBuild.toString());
        ticket.getAttributes().add(attribute);

        //Get the agent's name
        String agentName1 = "";
        for (ItemUpdate itemUpdate : feed) {
            if (!itemUpdate.getCreatedFullName().equals("BSC Robot") && !itemUpdate.getCreatedFullName().equals("System")) {
                agentName1 = itemUpdate.getCreatedFullName();
                break;
            }
        }
        //Add the agent name
        CustomAttribute agentName = new CustomAttribute(nonReqInfo.getAgentNameAttrID(appID), agentName1);
        ticket.getAttributes().add(agentName);


        //Add the oneFormTicket ID to the Department Ticket
        CustomAttribute oneFormTicketID = new CustomAttribute(nonReqInfo.getOneFormTicketIDID(appID), Integer.toString(oneFormTicket.getId()));
        ticket.getAttributes().add(oneFormTicketID);



        return ticket;
    }

    /**
     * clearTicketFeedAttr:
     * At the time of first ticket creation the attribute will not show its text in the ticket feed
     *  unless something else modifies it. This will take the newly created ticket and set the
     *  ticket feed attribute to 0, just to get the current info put in the feed.
     *  @param oneFormTicket, the current oneForm that was created
     * @param ticket,        the newly created department ticket
     */
    private void clearTicketFeedAttr (Ticket oneFormTicket, Ticket ticket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "clearTicketFeedAttr", ServiceRequest.class, Level.));
        ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId());

        int appId = ticket.getAppId();
        ArrayList<CustomAttribute> attributes = ticket.getAttributes();
        for (int i = 0; i < attributes.size() - 1; i++) {
            if (attributes.get(i).getId() == nonReqInfo.getTicketFeedUpdateAttr(appId)) {
                history.addEvent(new LoggingEvent("WE GOT A MATCHING ATTRIBUTE", "clearTicketFeedAttr", ServiceRequest.class, Level.INFO));
                if (!attributes.get(i).getChoicesText().equals(feed.get(feed.size() - 1).getBody())) {
                    history.addEvent(new LoggingEvent("THE FEEDS DONT MATCH, ADD THE NEW FEED TO DEPT TICKET", "clearTicketFeedAttr", ServiceRequest.class, Level.INFO));
                    ticket.getAttributes().remove(i);
                    CustomAttribute ticketFeed = new CustomAttribute(nonReqInfo.getTicketFeedUpdateAttr(appId), "0");
                    ticket.getAttributes().add(ticketFeed);
                    return;
                }
            }
        }
    }

    /**
     * updateTicketFeed:
     * Updates the dept ticket feed with the newest entry from the oneForm feed. This is done
     *  by adding the new oneform entry to the Ticket Feed Update attribute in the dept ticket.
     *  This attribute is hidden so no one may modify it, but it should properly update the dept
     *  ticket feed. Also will add any missing attachments to the dept ticket from the oneForm ticket.
     *
     * @param oneFormTicket, the current oneForm that was created
     * @param departmentTicket,        the newly created department ticket
     */
    Ticket updateTicketFeed (Ticket oneFormTicket, Ticket departmentTicket, TeamDynamix api, ArrayList<ItemUpdate> feed) throws
    TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "updateTicketFeed", ServiceRequest.class, Level.));
        int appId = departmentTicket.getAppId();


        //Logic behind filtering the feed
        // Is not BSC Robot
        // Is not System
        // Is not part of BYUI Support Center group "Id 80" unless they are notifying.
        //
        //
        history.addEvent(new LoggingEvent("WE NEED TO MAKE A BODY", "updateTicketFeed", ServiceRequest.class, Level.INFO));
        StringBuilder feedBuild = new StringBuilder();
        int count = 0;
        for (ItemUpdate itemUpdate : feed) {
            if (itemUpdate.getReplies().size() > 0) {
                for (int j = 0; j < itemUpdate.getReplies().size(); j++) {

                    if (!itemUpdate.getReplies().get(j).getCreatedFullName().equals("BSC Robot") && !itemUpdate.getReplies().get(j).getCreatedFullName().equals("System")) {

                        feedBuild.append(itemUpdate.getReplies().get(j).getCreatedFullName()).append(": ").append(itemUpdate.getReplies().get(j).getBody()).append("\n\n");

                    }
                }
            }

            if (!itemUpdate.getCreatedFullName().equals("BSC Robot") && !itemUpdate.getCreatedFullName().equals("System")) {
                String uid = itemUpdate.getCreatedUid();

                User user = api.getUser(uid);

                if (!user.getGroupIDs().toString().contains("80") || itemUpdate.getNotifiedList() != null) {
                    if (count == 0) {
                        feedBuild.append("-> \n\n" + itemUpdate.getCreatedFullName() + ": " + itemUpdate.getBody()).append("\n\n");
                        count++;
                    } else {
                        feedBuild.append(itemUpdate.getCreatedFullName() + ": " + itemUpdate.getBody()).append("\n\n");
                    }
                }
            }
        }
        //Make the string look prettier...
        String finalFeed = feedBuild.toString();
        finalFeed = finalFeed.replaceAll("<br />", "\n");
        finalFeed = finalFeed.replaceAll("<br /><br />", "\n");
        finalFeed = finalFeed.replaceAll("br/>", "\n");
        finalFeed = finalFeed.replaceAll("Changed Status from &quot;<b>Closed</b>&quot; to &quot;<b>New</b>&quot;.<\n" +
                "<", " ");


        //Clear the ticket feed
        CustomAttribute ticketFeedClear = new CustomAttribute(nonReqInfo.getTicketFeedUpdateAttr(appId), "0");
        departmentTicket.getAttributes().add(ticketFeedClear);

        history.addEvent(new LoggingEvent("Editing ticket: " + departmentTicket.getId(), "updateTicketFeed", ServiceRequest.class, Level.INFO));
        api.editTicket(false, departmentTicket);

        //upload the string
        CustomAttribute ticketFeed = new CustomAttribute(nonReqInfo.getTicketFeedUpdateAttr(appId), finalFeed);
        // history.addEvent(new LoggingEvent("TICKET FEED ATTRIBUTE BODY: " + ticketFeed.getValue(), "updateTicketFeed", ServiceRequest.class, Level.INFO));
        departmentTicket.getAttributes().add(ticketFeed);

        history.addEvent(new LoggingEvent("Updating oneFormTicket " + oneFormTicket.getId(), "updateTicketFeed", ServiceRequest.class, Level.INFO));
        api.editTicket(false, oneFormTicket);

        //We will now update the attachments as well.
        //Test tickets 48, 4450070, and 54, 4450080

        history.addEvent(new LoggingEvent("Adding attachments to department ticket.", "updateTicketFeed", ServiceRequest.class, Level.INFO));

        //Upload attachment
        List<Attachment> attachments = oneFormTicket.getAttachments();

        for (Attachment attachment : attachments) {
            api.uploadAttachment(departmentTicket.getId(), departmentTicket.getAppId(), attachment);
            history.addEvent(new LoggingEvent("Attachment added.", "updateTicketFeed", ServiceRequest.class, Level.INFO));
        }
        return departmentTicket;
    }


    /**
     * finalOneFormUpdates:
     * Updates the oneForm ticket with any changes we need at the end of the program (e.g add dept ticket ID)
     *
     * @param oneFormTicket, the current oneForm that was created
     * @param ticket,        the newly created department ticket
     */
    void finalOneFormUpdates (Ticket oneFormTicket, Ticket ticket) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "finalOneFormUpdates", ServiceRequest.class, Level.));
        TeamDynamix api = new TeamDynamix(System.getenv("TD_API_BASE_URL"), System.getenv("USERNAME"), System.getenv("PASSWORD"), history);

        //Add the Dept Ticket ID to the OneForm description
        String newOneFormDescription = oneFormTicket.getDescription() + " |DEPARTMENT TICKET ID = " + ticket.getId() + "|";

        //The ticket used to update the oneForm Ticket
        Ticket newOneFormTicket = api.getTicket(oneForm.IDs.ONE_FORM_APPLICATION_ID, oneFormTicket.getId());

        //Set the new description
        newOneFormTicket.setDescription(newOneFormDescription);

        //Add the dep ticket ID attribute
        CustomAttribute departmentTicketID = new CustomAttribute(oneForm.IDs.ONE_FORM_DEPT_TICK_ID, Integer.toString(ticket.getId()));
        newOneFormTicket.getAttributes().add(departmentTicketID);

        //Set the requester to be the same as the created by
        //newOneFormTicket.setResponsibleUid(newOneFormTicket.getCreatedUid());


        //Update the oneForm ticket
        api.editTicket(false, newOneFormTicket);
    }

    /**
     * setTicketToClosed:
     * Will set a ticket to closed regardless of the application it is in.
     *
     * @param ticket, the ticket we want to set to closed
     */
    void setTicketToClosed (Ticket ticket) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "setTicketToClosed", ServiceRequest.class, Level.));
        int statusID = 0;

        switch (ticket.getAppId()) {
            case oneForm.IDs.ACCOUNTING_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.ACCOUNTING_STATUS_CLOSED);
                break;
            case oneForm.IDs.BYUI_TICKETS_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.ACADEMIC_STATUS_CLOSED);
                break;
            case oneForm.IDs.ADMISSIONS_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.ADMISSIONS_STATUS_CLOSED);
                break;
            case oneForm.IDs.FINANCIAL_AID_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.FINANCIAL_AID_STATUS_CLOSED);
                break;
            case oneForm.IDs.SRR_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.SRR_STATUS_CLOSED);
                break;
            case oneForm.IDs.ONE_FORM_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.ONE_FORM_CLOSED);
                break;
            case oneForm.IDs.ADVISING_APPLICATION_ID:
                ticket.setStatusId(oneForm.IDs.ADVISING_STATUS_CLOSED);
                break;

        }
    }

    /**
     * sentToLevel2StatusUpdates:
     * If the oneForm was marked as sent to level 2 or escalated then we need to reflect those changes in the dep ticket as well.
     *
     * @param ticket, the dep ticket need to modify.
     * @param feed, the feed entry to check if it was escalated or not.
     * @param api, the TD api we will use throughout the program.
     */
    Ticket sentToLevel2StatusUpdates (Ticket ticket, ArrayList < ItemUpdate > feed, TeamDynamix api) throws
    TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.));

        CustomAttribute sentToLevel2;
        if (feed.get(0).getBody().contains("to \"Escalated the") || feed.get(0).getBody().contains("to \"Resolved by calling")) {
            switch (ticket.getAppId()) {
                case oneForm.IDs.ACCOUNTING_APPLICATION_ID:
                    ticket.setStatusId(oneForm.IDs.ACCOUNTING_STATUS_NEW);
                    sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(ticket.getAppId()), oneForm.IDs.ACCOUNTING_SENT_TO_LEVEL_2_YES);
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("BONK1", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
                    break;
                case oneForm.IDs.BYUI_TICKETS_APPLICATION_ID:
                    ticket.setStatusId(oneForm.IDs.ACADEMIC_STATUS_NEW);
                    sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(ticket.getAppId()), oneForm.IDs.ACADEMIC_SENT_TO_LEVEL_2_YES);
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("BONK2", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
                    break;
                case oneForm.IDs.ADMISSIONS_APPLICATION_ID:
                    ticket.setStatusId(oneForm.IDs.ADMISSIONS_STATUS_NEW);
                    sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(ticket.getAppId()), oneForm.IDs.ADMISSIONS_SENT_TO_LEVEL_2_YES);
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("BONK3", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
                    break;
                case oneForm.IDs.FINANCIAL_AID_APPLICATION_ID:
                    ticket.setStatusId(oneForm.IDs.FINANCIAL_AID_STATUS_NEW);
                    sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(ticket.getAppId()), oneForm.IDs.FINANCIAL_AID_SENT_TO_LEVEL_2_YES);
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("BONK4", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
                    break;
                case oneForm.IDs.SRR_APPLICATION_ID:
                    ticket.setStatusId(oneForm.IDs.SRR_STATUS_NEW);
                    sentToLevel2 = new CustomAttribute(nonReqInfo.getSentToLevel2ID(ticket.getAppId()), oneForm.IDs.SRR_SENT_TO_LEVEL_2_YES);
                    ticket.getAttributes().add(sentToLevel2);
                    history.addEvent(new LoggingEvent("BONK5", "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
                    break;

            }

        }
        history.addEvent(new LoggingEvent("STATUS OF FUNCTION: " + ticket.getStatusName(), "sentToLevel2StatusUpdates", ServiceRequest.class, Level.INFO));
        return ticket;

    }

    /**
     * uploadNewTicket:
     * This will take the new ticket that has been formed, and create it in the
     * designated application of TD
     *
     * @param appID,         the ticket that we are going to use to update the existing one
     * @param newTicket,     the ticket that will be put into TD
     * @param oneFormTicket, the oneForm ticket
     * @param andonTicket,   the andon cord ticket
     */
    void uploadNewTickets ( int appID, Ticket newTicket, Ticket oneFormTicket, Ticket andonTicket) throws
    TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "uploadNewTickets", ServiceRequest.class, Level.));
        TeamDynamix api = new TeamDynamix(System.getenv("TD_API_BASE_URL"), System.getenv("USERNAME"), System.getenv("PASSWORD"), history);
        //Create the department ticket
        Ticket ticket = api.createTicket(appID, newTicket);

        api.editTicket(false, oneFormTicket);

        //adjust the tickets feed
        clearTicketFeedAttr(oneFormTicket, ticket, api);

        api.editTicket(false, ticket);







        //If a Andon Cord ticket is needed.
        for (int i = 0; i < oneFormTicket.getAttributes().size(); i++) {
            if (oneFormTicket.getAttributes().get(i).getValue().equals(IDs.BSC_OPERATIONS_ANDON_CORD_NEEDED_YES)) {
                //Create the AndonCord Ticket
                Ticket andonCordTicket = api.createTicket(andonTicket.getAppId(), andonTicket);
                history.addEvent(new LoggingEvent("ANDON CORD TICKET ID: " + andonCordTicket.getId(), "uploadNewTickets", ServiceRequest.class, Level.INFO));
                break;
            }
        }

        finalOneFormUpdates(oneFormTicket, ticket);
        this.addAttachments(oneFormTicket, ticket, api);

        //FOR TESTING
        history.addEvent(new LoggingEvent("THE CREATED TICKET's ID: " + ticket.getId() + "| AND APPID: " + ticket.getAppId(), "uploadNewTickets", ServiceRequest.class, Level.INFO));
    }


    /**
     * checkForNotified:
     * If the last ticket feed entry was notified, return true, otherwise return false.
     *
     * @param oneFormTicket: the ticket whose entry needs to be checked.
     * @param api: the TeamDynamix object we are using.
     */
    boolean checkForNotified (Ticket oneFormTicket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "checkForNotified", ServiceRequest.class, Level.));
        ArrayList<CustomAttribute> attributes = oneFormTicket.getAttributes();

        ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId()); //Read the entries into an array

        //In the if statement we check only the most recent entry, no others.
        if (feed.get(0).getNotifiedList() != null) {
            history.addEvent(new LoggingEvent("THIS WAS NOTIFIED", "checkForNotified", ServiceRequest.class, Level.INFO));
            return true; //Return true so we know we need to create a count ticket
        }
        return false; //False means to not increment the count ticket.
    }

    /**
     * createCountTicket:
     * Creates the count ticket and uploads it to TD. Sets all of the counters to 0 to begin with.
     *
     * @param oneFormTicket: the ticket whose information will be passed
     * @param api: the TeamDynamix object we are using.
     */
    Ticket createCountTicket (Ticket oneFormTicket, TeamDynamix api, String agentNameUID) throws
            TDException {
        // history.addEvent(new LoggingEvent("", "createCountTicket", ServiceRequest.class, Level.));
        Ticket countTicket = new Ticket();
        ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId()); //Read the entries into an array

        //REQUIRED ATTRIBUTES
        countTicket.setFormId(oneForm.IDs.OPERATIONS_COUNT_FORM_ID);
        countTicket.setTypeId(oneForm.IDs.BSC_OPERATIONS_TYPE_ID);
        countTicket.setTitle("Email Count Form");
        countTicket.setAccountId(548); //The acct/dept of OnTrackStudent
        countTicket.setStatusId(oneForm.IDs.BSC_OPERATIONS_ANDON_FORM_STATUS_CLOSED);
        countTicket.setPriorityId(reqInfo.getPriorityID());

        countTicket.setRequestorUid(agentNameUID);
        countTicket.setAppID(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID);

        //THE EXTRA attributes that aren't required by TD
        countTicket.setAppID(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID);

        //Add the extra custom attributes

        CustomAttribute dateData = new CustomAttribute(oneForm.IDs.OPERATIONS_COUNT_DATA_DATE, feed.get(0).getCreatedDate().toString());
        countTicket.getAttributes().add(dateData);

        CustomAttribute emailVol = new CustomAttribute(oneForm.IDs.OPERATIONS_COUNT_EMAIL_VOLUME, "0");
        countTicket.getAttributes().add(emailVol);

        CustomAttribute spamVol = new CustomAttribute(oneForm.IDs.OPERATIONS_COUNT_SPAM_VOLUME, "0");
        countTicket.getAttributes().add(spamVol);

        CustomAttribute replyVol = new CustomAttribute(oneForm.IDs.OPERATIONS_COUNT_REPLY_VOLUME, "0");
        countTicket.getAttributes().add(replyVol);

        CustomAttribute escalateVol = new CustomAttribute(oneForm.IDs.OPERATIONS_COUNT_EXCALATED_VOLUME, "0");
        countTicket.getAttributes().add(escalateVol);

        Ticket ticket = api.createTicket(oneForm.IDs.BSC_OPERATIONS_APPLICATION_ID, countTicket); //Upload the ticket to TD

        //output the COUNT ticket details for debugging...
        history.addEvent(new LoggingEvent("COUNT TICKET ID: " + ticket.getId(), "createCountTicket", ServiceRequest.class, Level.INFO));

        return ticket;
    }

    /**
     * shouldCreateCount:
     * Determines whether a count ticket should be created, a count ticket will be created under the following
     *  circumstances....
     *  1. Tag is Spam
     *  2. The agent notified when emailing
     *  3. The ticket was marked to escalate
     *
     * @param oneFormTicket, the current oneForm that was created
     * @param api,           TD api
     */
    int shouldCreateCount (Ticket oneFormTicket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "shouldCreateCount", ServiceRequest.class, Level.));
        ArrayList<ItemUpdate> feed = api.getTicketFeedEntries(oneFormTicket.getAppId(), oneFormTicket.getId());

        //This checks if the feed is notify
        if (isSpam(feed)) {
            return 1;
        } else if (checkForNotified(oneFormTicket, api)) {
            return 2;
        } else if (isEscalate(feed)) {
            return 3;
        } else
            return 0;
    }

    /**
     * isNotify:
     * Determines whether the ticket feed changed to escalate the ticket
     *
     * @param feed, the feed of the oneForm ticket
     */
    boolean isEscalate (ArrayList < ItemUpdate > feed) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "isEscalate", ServiceRequest.class, Level.));
        String body = feed.get(0).getBody();

        if (body.contains("Resolved by calling") || body.contains("Escalated the ticket")) {
            history.addEvent(new LoggingEvent("THIS WAS ESCALATED", "isEscalate", ServiceRequest.class, Level.INFO));
            return true;
        }

        return false;
    }

    /**
     * isSpam:
     * Determines whether the ticket feed changed the tag to spam or not
     *
     * @param feed, the feed of the oneForm ticket
     */
    boolean isSpam (ArrayList < ItemUpdate > feed) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "isSpam", ServiceRequest.class, Level.));
        String body = feed.get(0).getBody();

        if (body.contains("to Spam")) {
            history.addEvent(new LoggingEvent("THIS WAS CHANGED TO SPAM", "isSpam", ServiceRequest.class, Level.INFO));
            return true;
        }

        return false;
    }

    /**
     * incrementNotify:
     * Increments the Email reply volume by one
     *
     * @param ticket, the count ticket that needs to be incremented
     */
    void incrementNotify (Ticket ticket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "incrementNotify", ServiceRequest.class, Level.));
        ArrayList<CustomAttribute> attributes = ticket.getAttributes();

        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == IDs.OPERATIONS_COUNT_REPLY_VOLUME) {
                String num4 = attribute.getValueText();

                int newNum4 = parseInt(num4); //cast to int
                newNum4 = newNum4 + 1; //increment

                num4 = String.valueOf(newNum4); //reassign the string to the new value.

                CustomAttribute newReplyVol = new CustomAttribute(IDs.OPERATIONS_COUNT_REPLY_VOLUME, num4);//make the new custom attribute
                ticket.getAttributes().add(newReplyVol); //add the new count

                api.editTicket(false, ticket);

                return;
            }
        }
    }

    /**
     * incrementSpam:
     * Increments the Email spam volume by one
     *
     * @param ticket, the ticket to be implemented
     */
    void incrementSpam (Ticket ticket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "incrementSpam", ServiceRequest.class, Level.));
        ArrayList<CustomAttribute> attributes = ticket.getAttributes();

        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == IDs.OPERATIONS_COUNT_SPAM_VOLUME) {
                String num3 = attribute.getValueText();

                int newNum3 = parseInt(num3); //cast to int
                newNum3 = newNum3 + 1; //increment

                num3 = String.valueOf(newNum3); //reassign the string to the new value.

                CustomAttribute newReplyVol = new CustomAttribute(IDs.OPERATIONS_COUNT_SPAM_VOLUME, num3);//make the new custom attribute
                ticket.getAttributes().add(newReplyVol); //add the new count

                api.editTicket(false, ticket);

                return;
            }
        }
    }

    /**
     * incrementEscalate:
     * Increments the Escalated Tickets volume by one
     *
     * @param ticket, the ticket to be incremented
     */
    void incrementEscalate (Ticket ticket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "incrementEscalate", ServiceRequest.class, Level.));
        ArrayList<CustomAttribute> attributes = ticket.getAttributes();

        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == IDs.OPERATIONS_COUNT_EXCALATED_VOLUME) {
                String num2 = attribute.getValueText();

                int newNum2 = parseInt(num2); //cast to int
                newNum2 = newNum2 + 1; //increment

                num2 = String.valueOf(newNum2); //reassign the string to the new value.

                CustomAttribute newReplyVol = new CustomAttribute(IDs.OPERATIONS_COUNT_EXCALATED_VOLUME, num2);//make the new custom attribute
                ticket.getAttributes().add(newReplyVol); //add the new count

                api.editTicket(false, ticket);

                return;
            }
        }
    }

    /**
     * incrementOverall:
     *  Increments the Email total volume by one
     *
     * @param ticket, the ticket to be incremented
     */
    void incrementOverall (Ticket ticket, TeamDynamix api) throws TDException, InterruptedException {
        // history.addEvent(new LoggingEvent("", "incrementOverall", ServiceRequest.class, Level.));
        ArrayList<CustomAttribute> attributes = ticket.getAttributes();

        for (CustomAttribute attribute : attributes) {
            if (attribute.getId() == IDs.OPERATIONS_COUNT_EMAIL_VOLUME) {
                String num1 = attribute.getValueText();

                int newNum1 = parseInt(num1); //cast to int
                newNum1 = newNum1 + 1; //increment

                num1 = String.valueOf(newNum1); //reassign the string to the new value.

                CustomAttribute newReplyVol = new CustomAttribute(IDs.OPERATIONS_COUNT_EMAIL_VOLUME, num1);//make the new custom attribute
                ticket.getAttributes().add(newReplyVol); //add the new count

                api.editTicket(false, ticket); //update the ticket in TD

                return;
            }
        }
    }


    public void addCountTicketSemaphore (Semaphore semaphore){
        this.countTicketSemaphore = semaphore;
    }

    public void addAndonTicketSemaphore(Semaphore semaphore) {
        this.andonTicketSemaphore = semaphore;
    }
}
