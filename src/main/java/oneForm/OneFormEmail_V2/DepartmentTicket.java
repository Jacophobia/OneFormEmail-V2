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


    public DepartmentTicket(History history, OneformTicket oneformTicket) {
        super(history);

        assert oneformTicket != null :
            "The oneFormTicket from the parameters has not been initialized";
        this.oneformTicket = oneformTicket;
    }

    @Override
    public void prepareTicketUpload() throws TDException {
        setRequiredAttributes();
    }

    protected void setRequiredAttributes() {
        setTypeId(findTypeId()); // int
        setTitle(findTitle()); // String
        setFormId(findFormId()); // int
        setAccountId(findAccountId()); // int
        setStatusId(findStatusId()); // int
        setPriorityId(findPriorityId()); // int
        setRequestorUid(findRequestorUid()); // String

    }

    abstract protected int findTypeId();

    abstract protected int findStatusId();

    abstract protected int findFormId();

    private String findTitle() {
        return oneformTicket.getTitle();
    }

    private int findAccountId(){
        return oneformTicket.getAccountId();
    }

    private int findPriorityId() {
        return STATUS_3_ID;
    }

    protected String findRequestorUid() {
        // TODO: Add functionality for when it is a Health Center
        //  Contact (see line 350 - 360 requiredInfoGets)
        return oneformTicket.getRequestorUid();
    }

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