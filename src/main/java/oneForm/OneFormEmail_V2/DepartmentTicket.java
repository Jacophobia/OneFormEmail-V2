package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

public abstract class DepartmentTicket extends GeneralTicket {
    protected OneformTicket oneformTicket;
    final int STATUS_3_ID = 21;
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

    abstract protected String findRequestorUid();

    @Override
    public String toString() {
        return
            "\n *     - Editable" +
            "\n *  *  - Editable and Required" +
            "\n" +
            "\n *  *    TypeId= " + getTypeId() +
            "\n *  *    Title= " + getTitle() +
            "\n *  *    AccountId= " + getAccountId() +
            "\n *  *    StatusId= " + getStatusId() +
            "\n *  *    PriorityId= " + getPriorityId() +
            "\n *  *    RequesterUID= " + getRequestorUid() +
            "\n *  *    FormId= " + getFormId() +
            "\n *       Description= " + getDescription() +
            "\n *       SourceId= " + getSourceId() +
            "\n *       ImpactId= " + getImpactId() +
            "\n *       UrgencyId= " + getUrgencyId() +
            "\n *       GoesOffHoldDate= " + getGoesOffHoldDate() +
            "\n *       EstimatedMinutes= " + getEstimatedMinutes() +
            "\n *       StartDate= " + getStartDate() +
            "\n *       EndDate= " + getEndDate() +
            "\n *       ResponsibleUID= " + getResponsibleUid() +
            "\n *       ResponsibleGroupId= " + getResponsibleGroupId() +
            "\n *       TimeBudget= " + getTimeBudget() +
            "\n *       ExpensesBudget= " + getExpensesBudget() +
            "\n *       LocationId= " + getLocationId() +
            "\n *       LocationRoomId= " + getLocationRoomId() +
            "\n *       ServiceId= " + getServiceId() +
            "\n *       ServiceOfferingID= " +
            "\n *       ArticleId= " + getArticleId();
    }

}