package oneForm.OneFormEmail_V2;

import com.google.gson.Gson;
import td.api.Exceptions.TDException;
import td.api.Logging.History;
import td.api.Ticket;

public abstract class DepartmentTicket extends GeneralTicket {
    public DepartmentTicket(History history, OneformTicket oneformTicket) {
        super(oneformTicket, history);
    }

    @Override
    public void prepareTicketUpload() throws TDException {

    }

    @Override
    public String toString() {
        return  "\n *     - Editable" +
                "\n *  *  - Editable and Required" +
                "\n *  *    TypeId= " + getTypeId() +
                "\n *       FormId= " + getFormId() +
                "\n *  *    Title= " + getTitle() +
                "\n *       Description= " + getDescription() +
                "\n *  *    AccountId= " + getAccountId() +
                "\n *       SourceId= " + getSourceId() +
                "\n *  *    StatusId= " + getStatusId() +
                "\n *       ImpactId= " + getImpactId() +
                "\n *       UrgencyId= " + getUrgencyId() +
                "\n *  *    PriorityId= " + getPriorityId() +
                "\n *       GoesOffHoldDate= " + getGoesOffHoldDate() +
                "\n *  *    RequesterUID= " + getRequestorUid() +
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
                "\n *       ArticleId= " + getArticleId() +
                "\n         AppID= " + getAppId() +
                "\n         Attributes= " + getAttributes() +
                "\n         Attachments= " + getAttachments() +
                "\n         Tasks= " + getTasks() +
                "\n         Notify= " + getNotify();
    }

}