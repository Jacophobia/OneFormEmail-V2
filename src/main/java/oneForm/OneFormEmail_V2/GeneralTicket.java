package oneForm.OneFormEmail_V2;

import td.api.CustomAttribute;
import td.api.Exceptions.TDException;
import td.api.Logging.History;
import td.api.Ticket;

import java.util.*;

public abstract class GeneralTicket extends Ticket {
    protected LoggingSupervisor debug;
    protected Ticket createdTicket;
    protected Map<Integer, String> ticketAttributes = new HashMap<>();
    protected Map<Integer, String> attributeChoiceText = new HashMap<>();
    protected int applicationID = 0;
    protected boolean retrieved = false;

    public GeneralTicket(History history) {
        super();
        this.initializeTicket(history);
    }

    public boolean isRetrieved() {
        return retrieved;
    }

    public void setRetrieved(boolean retrieved) {
        this.retrieved = retrieved;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    @Override
    public int getAppId() {
        if (this.applicationID == 0) {
            return super.getAppId();
        }
        return applicationID;
    }

    @Override
    public int getId() {
        if (this.createdTicket != null) {
            return createdTicket.getId();
        }
        return super.getId();
    }

    public void initializeTicket(History history) {
        debug = new LoggingSupervisor(history);
        debug.logNote(
            this.getClass(),
            "initializeTicket",
            "Beginning " + this.getClass().getSimpleName() + " Initialization"
        );

        assert history != null :
            "The history from the parameters has not been initialized";
        assert this.getAttributes() != null :
            "The custom attributes could not be retrieved";

        this.ticketAttributes = new HashMap<>();
        this.attributeChoiceText = new HashMap<>();

        for (CustomAttribute attribute : this.getAttributes()) {
            assert attribute != null : "The attribute is empty";
            ticketAttributes.put(attribute.getId(), attribute.getValue());
            attributeChoiceText.put(
                attribute.getId(),
                attribute.getChoicesText()
            );
        }
        debug.logNote(this.getClass().getSimpleName() + " Initialized.");
        this.setGoesOffHoldDate(new Date(
            System.currentTimeMillis() + 1000000000000L
        ));
    }

    public void prepareTicketUpload() throws TDException {
        ArrayList<CustomAttribute> customAttributes = new ArrayList<>();
        Set<Integer> keys = this.ticketAttributes.keySet();
        for (int key : keys) {
            customAttributes.add(new CustomAttribute(
                key,
                this.ticketAttributes.get(key))
            );
        }
        this.setAttributes(customAttributes);
    }

    public String getCustomAttribute(int attributeID) {
        assert ticketAttributes != null :
            "ticketAttributes has not been initialized";
        assert ticketAttributes.containsKey(attributeID) :
            "Error attribute not found.";

        return ticketAttributes.get(attributeID);
    }

    public String getAttributeText(int attributeID) {
        assert ticketAttributes != null :
            "ticketAttributes has not been initialized";
        assert ticketAttributes.containsKey(attributeID) :
            "Error attribute not found.";

        return attributeChoiceText.get(attributeID);
    }

    public void addCustomAttribute(int key, String value) {
        CustomAttribute newAttribute = new CustomAttribute(key, value);
        ticketAttributes.put(key, newAttribute.getValue());
        attributeChoiceText.put(key, newAttribute.getChoicesText());
    }

    public boolean containsAttribute(int attributeID) {
        assert ticketAttributes != null :
            "ticketAttributes has not been initialized";
        return ticketAttributes.containsKey(attributeID);
    }

    public void setCreatedTicket(Ticket createdTicket) {
        this.createdTicket = createdTicket;
    }

    public String toString() {
        return
            "\n      " + this.getClass().getSimpleName() +
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
            "\n      * ArticleId : " + getArticleId() +
            "\n     TicketId : " + getId() +
            "\n     ParentId : " + getParentId() +
            "\n     ParentTitle : " + getParentTitle() +
            "\n     ParentClass : " + getParentClass() +
            "\n     TypeName : " + getTypeName() +
            "\n     TypeCategoryId : " + getTypeCategoryId() +
            "\n     TypeCategoryName : " + getTypeCategoryName() +
            "\n     Classification : " + getClassification() +
            "\n     ClassificationName : " + getClassificationName() +
            "\n     FormName : " + getFormName() +
            "\n     Uri : " + getUri() +
            "\n     AccountName : " + getAccountName() +
            "\n     SourceName : " + getSourceName() +
            "\n     StatusName : " + getStatusName() +
            "\n     StatusClass : " + getStatusClass() +
            "\n     ImpactName : " + getImpactName() +
            "\n     UrgencyName : " + getUrgencyName() +
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
            "\n     ActualMinutes : " + getActualMinutes() +
            "\n     DaysOld : " + getDaysOld() +
            "\n     ResponsibleFullName : " + getResponsibleFullName() +
            "\n     ResponsibleEmail : " + getResponsibleEmail() +
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
            "\n     LocationName : " + getLocationName() +
            "\n     LocationRoomName : " + getLocationRoomName() +
            "\n     RefCode : " + getRefCode() +
            "\n     ServiceName : " + getServiceName() +
            "\n     ServiceOfferingName : " +
            "\n     ServiceCategoryId : " + getServiceCategoryId() +
            "\n     ServiceCategoryName : " + getServiceCategoryName() +
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