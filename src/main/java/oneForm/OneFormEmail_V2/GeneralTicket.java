package oneForm.OneFormEmail_V2;

import td.api.CustomAttribute;
import td.api.Exceptions.TDException;
import td.api.Logging.History;
import td.api.TeamDynamix;
import td.api.Ticket;

import java.util.HashMap;
import java.util.Map;

public abstract class GeneralTicket extends Ticket {
    protected LoggingSupervisor debug;
    protected Map<Integer, String> ticketAttributes = new HashMap<>();
    protected int applicationID;

    public GeneralTicket(History history) {
        super();
        initializeTicket(history);
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void initializeTicket(History history) {
        debug = new LoggingSupervisor(history);

        debug.log(
            this.getClass(),
            "initializeTicket",
            "Beginning " + this.getClass().getSimpleName() + " Initialization");

        assert history != null :
            "The history from the parameters has not been initialized";
        assert this.getAttributes() != null :
            "The custom attributes could not be retrieved";

        this.ticketAttributes = new HashMap<>();

        for (CustomAttribute attribute : this.getAttributes()) {
            assert attribute != null : "The attribute is empty";
            ticketAttributes.put(attribute.getId(), attribute.getValue());
        }
        debug.log(this.getClass(), "Ticket Initialized.");
    }

    abstract public void prepareTicketUpload() throws TDException;

    public String getAttribute(int attributeID){
        assert ticketAttributes != null :
            "ticketAttributes has not been initialized";
        assert ticketAttributes.containsKey(attributeID) :
            "Error attribute not found.";

        return ticketAttributes.get(attributeID);
    }

    public boolean containsAttribute(int attributeID) {
        assert ticketAttributes != null :
            "ticketAttributes has not been initialized";
        return ticketAttributes.containsKey(attributeID);
    }

    public String toString() {
        return
            "\n      *  - Editable" +
            "\n      *  *  - Editable and Required" +
            "\n" +
            "\n     TicketId : " + getId() +
            "\n     ParentId : " + getParentId() +
            "\n     ParentTitle : " + getParentTitle() +
            "\n     ParentClass : " + getParentClass() +
            "\n      *  * TypeId : " + getTypeId() +
            "\n     TypeName : " + getTypeName() +
            "\n     TypeCategoryId : " + getTypeCategoryId() +
            "\n     TypeCategoryName : " + getTypeCategoryName() +
            "\n     Classification : " + getClassification() +
            "\n     ClassificationName : " + getClassificationName() +
            "\n      * FormId : " + getFormId() +
            "\n     FormName : " + getFormName() +
            "\n      *  * Title : " + getTitle() +
            "\n      * Description : " + getDescription() +
            "\n     Uri : " + getUri() +
            "\n      *  * AccountId : " + getAccountId() +
            "\n     AccountName : " + getAccountName() +
            "\n      * SourceId : " + getSourceId() +
            "\n     SourceName : " + getSourceName() +
            "\n      *  * StatusId : " + getStatusId() +
            "\n     StatusName : " + getStatusName() +
            "\n     StatusClass : " + getStatusClass() +
            "\n      * ImpactId : " + getImpactId() +
            "\n     ImpactName : " + getImpactName() +
            "\n      * UrgencyId : " + getUrgencyId() +
            "\n     UrgencyName : " + getUrgencyName() +
            "\n      *  * PriorityId : " + getPriorityId() +
            "\n     PriorityName : " + getPriorityName() +
            "\n     PriorityOrder : " + getPriorityOrder() +
            "\n     SlaId : " + getSlaId() +
            "\n     SlaName : " + getSlaName() +
            "\n     SlaViolated : " + isSlaViolated() +
            "\n     SlaRespondByViolated : " + isSlaRespondByViolated() +
            "\n     SlaResolvedByViolated : " + isSlaResolveByViolated() +
            "\n     RespondedByDate : " + getRespondByDate() +
            "\n     ResolvedByDate : " + getResolveByDate() +
            "\n     SlaBeginDate : " + getSlaBeginDate() +
            "\n     OnHold : " + isOnHold() +
            "\n     PLacedOnHoldDate : " + getPlacedOnHoldDate() +
            "\n      * GoesOffHoldDate : " + getGoesOffHoldDate() +
            "\n     CreatedDate : " + getCreatedDate() +
            "\n     CreatedUID : " + getCreatedUid() +
            "\n     CreatedFullName : " + getCreatedFullName() +
            "\n     CreatedEmail : " + getCreatedEmail() +
            "\n     ModifiedDate : " + getModifiedDate() +
            "\n     ModifiedUID : " + getModifiedUid() +
            "\n     ModifiedFullName : " + getModifiedFullName() +
            "\n     RequesterName : " + getRequestorName() +
            "\n     RequesterFirstName : " + getRequestorFirstName() +
            "\n     RequesterLastName : " + getRequestorLastName() +
            "\n     RequesterEmail : " + getRequestorEmail() +
            "\n     RequesterPhone : " + getRequestorPhone() +
            "\n      *  * RequesterUID : " + getRequestorUid() +
            "\n     ActualMinutes : " + getActualMinutes() +
            "\n      * EstimatedMinutes : " + getEstimatedMinutes() +
            "\n     DaysOld : " + getDaysOld() +
            "\n      * StartDate : " + getStartDate() +
            "\n      * EndDate : " + getEndDate() +
            "\n      * ResponsibleUID : " + getResponsibleUid() +
            "\n     ResponsibleFullName : " + getResponsibleFullName() +
            "\n     ResponsibleEmail : " + getResponsibleEmail() +
            "\n      * ResponsibleGroupId : " + getResponsibleGroupId() +
            "\n     ResponsibleGroupName : " + getResponsibleGroupName() +
            "\n     RespondedDate : " + getRespondedDate() +
            "\n     RespondedUID : " + getRespondedUid() +
            "\n     RespondedFullName : " + getRespondedFullName() +
            "\n     CompletedDate : " + getCompletedDate() +
            "\n     CompletedUID : " + getCompletedUid() +
            "\n     CompletedFullName : " + getCompletedFullName() +
            "\n     ReviewerUID : " + getReviewerUid() +
            "\n     ReviewerFullName : " + getReviewerFullName() +
            "\n     ReviewerEmail : " + getReviewerEmail() +
            "\n     ReviewingGroupId : " + getReviewingGroupId() +
            "\n     ReviewingGroupName : " + getReviewingGroupName() +
            "\n      * TimeBudget : " + getTimeBudget() +
            "\n      * ExpensesBudget : " + getExpensesBudget() +
            "\n     TimeBudgetUsed : " + getTimeBudgetUsed() +
            "\n     ExpensesBudgetUsed : " + getExpensesBudgetUsed() +
            "\n     ConvertedToTask : " + isConvertedToTask() +
            "\n     ConvertedToTaskDate : " + getConvertedToTaskDate() +
            "\n     ConvertedToTaskUid : " + getConvertedToTaskUid() +
            "\n     ConvertedToTaskFullName : " + getConvertedToTaskFullName() +
            "\n     TaskProjectId : " + getTaskProjectId() +
            "\n     TaskProjectName : " + getTaskProjectName() +
            "\n     TaskPlanId : " + getTaskPlanId() +
            "\n     TaskPlanName : " + getTaskPlanName() +
            "\n     TaskId : " + getTaskId() +
            "\n     TaskTitle : " + getTaskTitle() +
            "\n     TaskStartDate : " + getTaskStartDate() +
            "\n     TaskEndDate : " + getTaskEndDate() +
            "\n     TaskPercentComplete : " + getTaskPercentComplete() +
            "\n      * LocationId : " + getLocationId() +
            "\n     LocationName : " + getLocationName() +
            "\n      * LocationRoomId : " + getLocationRoomId() +
            "\n     LocationRoomName : " + getLocationRoomName() +
            "\n     RefCode : " + getRefCode() +
            "\n      * ServiceId : " + getServiceId() +
            "\n     ServiceName : " + getServiceName() +
            "\n      * ServiceOfferingID : " +
            "\n     ServiceOfferingName : " +
            "\n     ServiceCategoryId : " + getServiceCategoryId() +
            "\n     ServiceCategoryName : " + getServiceCategoryName() +
            "\n      * ArticleId : " + getArticleId() +
            "\n     ArticleSubject : " + getArticleSubject() +
            "\n     ArticleStatus : " + getArticleStatus() +
            "\n     ArticleCategoryPathNames : " +getArticleCategoryPathNames()+
            "\n     AppID : " + getAppId() +
            "\n     Attributes : " + getAttributes() +
            "\n     Attachments : " + getAttachments() +
            "\n     Tasks : " + getTasks() +
            "\n     Notify : " + getNotify();
    }
}