package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

public abstract class DepartmentTicket extends GeneralTicket {
    protected OneformTicket oneformTicket;

    private final int STATUS_3_ID = 21;

    protected final int EMAIL_ACTIONS_ATTR = 11398;
    protected final String EMAIL_ACTIONS_CHOICE_ESCALATE = "35388";

    protected final int FINANCIAL_AID_APP_ID = 53;
    protected final int ACCOUNTING_APP_ID = 54;
    protected final int ADMISSIONS_APP_ID = 52;
    protected final int ADVISING_APP_ID = 56;
    protected final int BYUI_TICKETS_APP_ID = 40;
    protected final int SRR_APP_ID = 55;

    protected final int ONEFORM_TAG_ID = 10333;
    protected final int SUPPORT_CENTER_LOCATION_ID = 1;
    protected final int EMAIL_SOURCE_ID = 6;


    public DepartmentTicket(History history, OneformTicket oneformTicket) {
        super(history);

        assert oneformTicket != null :
            "The oneFormTicket from the parameters has not been initialized";
        this.oneformTicket = oneformTicket;
    }

    @Override
    public void prepareTicketUpload() throws TDException {
        super.prepareTicketUpload();
        setRequiredAttributes();
        setAdditionalAttributes();
        setDepartmentSpecificAttributes();
    }

    /**
     * Required Attributes:
     * These attributes need to be included in the upload or Teamdynamix
     * will reject our request to create the ticket.
     */
    private void setRequiredAttributes() {
        setTypeId(findTypeId()); // int
        setTitle(findTitle()); // String
        setFormId(findFormId()); // int
        setAccountId(findAccountId()); // int
        setStatusId(findStatusId()); // int
        setPriorityId(findPriorityId()); // int
        setRequestorUid(findRequestorUid()); // String
    }

    /**
     * Additional Attributes:
     * These attributes are not required to upload the ticket, but give
     * context to the ticket so that those who view them understand the
     * incident which was recorded in the OneForm ticket.
     */
    private void setAdditionalAttributes() {
        setDescription(findDescription());
        setSourceId(findSourceId());
        setLocationId(findLocationId());
    }

    //
    // Required Attributes:
    // These attributes need to be included in the upload or Teamdynamix
    // will reject our request to create the ticket.
    //
    abstract protected int findTypeId();
    abstract protected int findStatusId();
    abstract protected int findFormId();

    /**
     * The Title is always the same as the OneForm ticket's title, so we
     * don't need to add functionality for each of the individual
     * department tickets.
     * @return The title of the new ticket based on the title of the
     * OneForm ticket.
     */
    private String findTitle() {
        return oneformTicket.getTitle();
    }

    /**
     * The Account ID is always the same as the OneForm ticket's Account
     * ID, so we don't need to add functionality for each of the
     * individual department tickets.
     * @return The Account ID of the new ticket based on the Account ID
     * of the OneForm ticket.
     */
    private int findAccountId(){
        return oneformTicket.getAccountId();
    }

    /**
     * The Priority ID is always going to be the same for every
     * department ticket created.
     * @return The ID for a priority level of 3
     */
    private int findPriorityId() {
        return STATUS_3_ID;
    }

    /**
     * The Requester is always the same as the OneForm ticket's
     * Requester, so we don't need to add functionality for each of the
     * individual department tickets.
     * @return The Requester UID of the new ticket based on the
     * Requester UID of the OneForm ticket.
     */
    protected String findRequestorUid() {
        return oneformTicket.getRequestorUid();
    }

    //
    // Additional Attributes:
    // These attributes are not required to upload the ticket, but give
    // context to the ticket so that those who view them understand the
    // incident which was recorded in the OneForm ticket.
    //
    private String findDescription() {
        return oneformTicket.getDescription() +
            "|ONEFORM TICKET ID = " + oneformTicket.getId() + "|";
    }

    private int findLocationId() {
        return SUPPORT_CENTER_LOCATION_ID;
    }

    private int findSourceId() {
        return EMAIL_SOURCE_ID;
    }

    /**
     * Department Specific Attributes:
     * These attributes are not required to upload the ticket, but are
     * required by the department whose application they will be saved
     * in.
     */
    abstract protected void setDepartmentSpecificAttributes();

    /**
     * If you ever want to view the contents of a department ticket for
     * debugging purposes, you can either use
     * departmentTicket'sName.toString() or
     * System.out.println(departmentTicket'sName).
     *
     * This function overrides the built in toString function that every
     * class has so that department tickets display their contents when
     * printed rather than their location in memory.
     *
     * I have already built in functionality that does this, so if you
     * want to display the ticket bodies right before upload, go into
     * the settings class and set the DisplayTicketBodies value to true.
     * @return The contents of the Department Ticket
     */
    @Override
    public String toString() {
        return
            "\n      " + this.getClass() +
            "\n" +
            "\n      *  - Editable" +
            "\n      *  *  - Editable and Required" +
            "\n" +
            "\n      *  * TypeId : " + getTypeId() +
            "\n      *  * Title : " + getTitle() +
            "\n      *  * AccountId : " + getAccountId() +
            "\n      *  * StatusId : " + getStatusId() +
            "\n      *  * PriorityId : " + getPriorityId() +
            "\n      *  * RequesterUID : " + getRequestorUid() +
            "\n      * FormId : " + getFormId() +
            "\n      * Description : " + getDescription() +
            "\n      * SourceId : " + getSourceId() +
            "\n      * ImpactId : " + getImpactId() +
            "\n      * UrgencyId : " + getUrgencyId() +
            "\n      * GoesOffHoldDate : " + getGoesOffHoldDate() +
            "\n      * EstimatedMinutes : " + getEstimatedMinutes() +
            "\n      * StartDate : " + getStartDate() +
            "\n      * EndDate : " + getEndDate() +
            "\n      * ResponsibleUID : " + getResponsibleUid() +
            "\n      * ResponsibleGroupId : " + getResponsibleGroupId() +
            "\n      * TimeBudget : " + getTimeBudget() +
            "\n      * ExpensesBudget : " + getExpensesBudget() +
            "\n      * LocationId : " + getLocationId() +
            "\n      * LocationRoomId : " + getLocationRoomId() +
            "\n      * ServiceId : " + getServiceId() +
            "\n      * ServiceOfferingID : " +
            "\n      * ArticleId : " + getArticleId();
    }

}