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
    protected OneformTicket oneformTicket;
    protected int applicationID;

    public GeneralTicket(OneformTicket oneformTicket, History history) {
        super();
        initializeTicket(history, oneformTicket);
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void initializeTicket(History history, OneformTicket oneformTicket) {
        debug = new LoggingSupervisor(history, this.getClass());
        // Here I initialized another Teamdynamix object so that we can create
        // tickets in sandbox when sandbox mode is on, while still accessing
        // info from tickets in the live version of teamdynamix.
        debug.log("initializeTicket", "Beginning Ticket Initialization");
        String sandboxExtension = "";
        if (Settings.sandbox)
            sandboxExtension = "SB";
        TeamDynamix api = new TeamDynamix(
                System.getenv("TD_API_BASE_URL") + sandboxExtension,
                System.getenv("USERNAME"),
                System.getenv("PASSWORD"),
                debug.getHistory());
        assert oneformTicket != null : "The oneFormTicket from the parameters has not been initialized";
        this.oneformTicket = oneformTicket;

        assert history != null : "The history from the parameters has not been initialized";
        assert this.getClass() != null : "The class type cannot be retrieved";

        assert this.getAttributes() != null : "The custom attributes could not be retrieved";
        this.ticketAttributes = new HashMap<>();

        for (CustomAttribute attribute : this.getAttributes()) {
            assert attribute != null : "The attribute is empty";
            ticketAttributes.put(attribute.getId(), attribute.getValue());
        }
        debug.log("initializeTicket", "Ticket Initialized.");
    }

    abstract public void prepareTicketUpload() throws TDException;

    public String getAttribute(int attributeID){
        assert ticketAttributes != null : "ticketAttributes has not been initialized";
        assert ticketAttributes.containsKey(attributeID) : "Error attribute not found.";

        return ticketAttributes.get(attributeID);
    }

    public boolean containsAttribute(int attributeID) {
        assert ticketAttributes != null : "ticketAttributes has not been initialized";
        return ticketAttributes.containsKey(attributeID);
    }

    public String toString() {
        return  "\n *  - Editable\n *  *  - Editable and Required" +
                "\nTicketId= " + getId() +
                "\nParentId= " + getParentId() +
                "\nParentTitle= " + getParentTitle() +
                "\nParentClass= " + getParentClass() +
                "\n *  * TypeId= " + getTypeId() +
                "\nTypeName= " + getTypeName() +
                "\nTypeCategoryId= " + getTypeCategoryId() +
                "\nTypeCategoryName= " + getTypeCategoryName() +
                "\nClassification= " + getClassification() +
                "\nClassificationName= " + getClassificationName() +
                "\n * FormId= " + getFormId() +
                "\nFormName= " + getFormName() +
                "\n *  * Title= " + getTitle() +
                "\n * Description= " + getDescription() +
                "\nUri= " + getUri() +
                "\n *  * AccountId= " + getAccountId() +
                "\nAccountName= " + getAccountName() +
                "\n * SourceId= " + getSourceId() +
                "\nSourceName= " + getSourceName() +
                "\n *  * StatusId= " + getStatusId() +
                "\nStatusName= " + getStatusName() +
                "\nStatusClass= " + getStatusClass() +
                "\n * ImpactId= " + getImpactId() +
                "\nImpactName= " + getImpactName() +
                "\n * UrgencyId= " + getUrgencyId() +
                "\nUrgencyName= " + getUrgencyName() +
                "\n *  * PriorityId= " + getPriorityId() +
                "\nPriorityName= " + getPriorityName() +
                "\nPriorityOrder= " + getPriorityOrder() +
                "\nSlaId= " + getSlaId() +
                "\nSlaName= " + getSlaName() +
                "\nSlaViolated= " + isSlaViolated() +
                "\nSlaRespondByViolated= " + isSlaRespondByViolated() +
                "\nSlaResolvedByViolated= " + isSlaResolveByViolated() +
                "\nRespondedByDate= " + getRespondByDate() +
                "\nResolvedByDate= " + getResolveByDate() +
                "\nSlaBeginDate= " + getSlaBeginDate() +
                "\nOnHold= " + isOnHold() +
                "\nPLacedOnHoldDate= " + getPlacedOnHoldDate() +
                "\n * GoesOffHoldDate= " + getGoesOffHoldDate() +
                "\nCreatedDate= " + getCreatedDate() +
                "\nCreatedUID= " + getCreatedUid() +
                "\nCreatedFullName= " + getCreatedFullName() +
                "\nCreatedEmail= " + getCreatedEmail() +
                "\nModifiedDate= " + getModifiedDate() +
                "\nModifiedUID= " + getModifiedUid() +
                "\nModifiedFullName= " + getModifiedFullName() +
                "\nRequesterName= " + getRequestorName() +
                "\nRequesterFirstName= " + getRequestorFirstName() +
                "\nRequesterLastName= " + getRequestorLastName() +
                "\nRequesterEmail= " + getRequestorEmail() +
                "\nRequesterPhone= " + getRequestorPhone() +
                "\n *  * RequesterUID= " + getRequestorUid() +
                "\nActualMinutes= " + getActualMinutes() +
                "\n * EstimatedMinutes= " + getEstimatedMinutes() +
                "\nDaysOld= " + getDaysOld() +
                "\n * StartDate= " + getStartDate() +
                "\n * EndDate= " + getEndDate() +
                "\n * ResponsibleUID= " + getResponsibleUid() +
                "\nResponsibleFullName= " + getResponsibleFullName() +
                "\nResponsibleEmail= " + getResponsibleEmail() +
                "\n * ResponsibleGroupId= " + getResponsibleGroupId() +
                "\nResponsibleGroupName= " + getResponsibleGroupName() +
                "\nRespondedDate= " + getRespondedDate() +
                "\nRespondedUID= " + getRespondedUid() +
                "\nRespondedFullName= " + getRespondedFullName() +
                "\nCompletedDate= " + getCompletedDate() +
                "\nCompletedUID= " + getCompletedUid() +
                "\nCompletedFullName= " + getCompletedFullName() +
                "\nReviewerUID= " + getReviewerUid() +
                "\nReviewerFullName= " + getReviewerFullName() +
                "\nReviewerEmail= " + getReviewerEmail() +
                "\nReviewingGroupId= " + getReviewingGroupId() +
                "\nReviewingGroupName= " + getReviewingGroupName() +
                "\n * TimeBudget= " + getTimeBudget() +
                "\n * ExpensesBudget= " + getExpensesBudget() +
                "\nTimeBudgetUsed= " + getTimeBudgetUsed() +
                "\nExpensesBudgetUsed= " + getExpensesBudgetUsed() +
                "\nConvertedToTask= " + isConvertedToTask() +
                "\nConvertedToTaskDate= " + getConvertedToTaskDate() +
                "\nConvertedToTaskUid= " + getConvertedToTaskUid() +
                "\nConvertedToTaskFullName= " + getConvertedToTaskFullName() +
                "\nTaskProjectId= " + getTaskProjectId() +
                "\nTaskProjectName= " + getTaskProjectName() +
                "\nTaskPlanId= " + getTaskPlanId() +
                "\nTaskPlanName= " + getTaskPlanName() +
                "\nTaskId= " + getTaskId() +
                "\nTaskTitle= " + getTaskTitle() +
                "\nTaskStartDate= " + getTaskStartDate() +
                "\nTaskEndDate= " + getTaskEndDate() +
                "\nTaskPercentComplete= " + getTaskPercentComplete() +
                "\n * LocationId= " + getLocationId() +
                "\nLocationName= " + getLocationName() +
                "\n * LocationRoomId= " + getLocationRoomId() +
                "\nLocationRoomName= " + getLocationRoomName() +
                "\nRefCode= " + getRefCode() +
                "\n * ServiceId= " + getServiceId() +
                "\nServiceName= " + getServiceName() +
                "\n * ServiceOfferingID= " +
                "\nServiceOfferingName= " +
                "\nServiceCategoryId= " + getServiceCategoryId() +
                "\nServiceCategoryName= " + getServiceCategoryName() +
                "\n * ArticleId= " + getArticleId() +
                "\nArticleSubject= " + getArticleSubject() +
                "\nArticleStatus= " + getArticleStatus() +
                "\nArticleCategoryPathNames= " + getArticleCategoryPathNames() +
                "\nAppID= " + getAppId() +
                "\nAttributes= " + getAttributes() +
                "\nAttachments= " + getAttachments() +
                "\nTasks= " + getTasks() +
                "\nNotify= " + getNotify();
    }
}