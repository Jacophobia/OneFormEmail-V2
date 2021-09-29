package oneForm.OneFormEmail_V2;

import td.api.CustomAttribute;
import td.api.Exceptions.TDException;
import td.api.ItemUpdate;
import td.api.Logging.History;
import td.api.Ticket;

import java.util.*;


public abstract class GeneralTicket extends Ticket {
    protected LoggingSupervisor debug;
    protected Ticket createdTicket = null;
    protected Map<Integer, String> ticketAttributes = new HashMap<>();
    protected Map<Integer, String> attributeChoiceText = new HashMap<>();
    protected int applicationID = 0;
    protected boolean retrieved = false;
    protected ArrayList<ItemUpdate> feed;

    public GeneralTicket(History history) {
        super();
        this.initializeTicket(history);
    }

    public boolean isRetrieved() {
        return retrieved;
    }

    public boolean wasCreated() {
        return createdTicket != null;
    }

    public void setRetrieved(boolean retrieved) {
        this.retrieved = retrieved;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    @Override
    public int getAppId() {
        if (super.getAppId() != 0) {
            return super.getAppId();
        }
        return applicationID;
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

    public ArrayList<ItemUpdate> getFeed() {
        return this.feed;
    }

    public void setCreatedTicket(Ticket createdTicket) {
        assert createdTicket.getAppId() == this.getAppId() :
            "The created ticket does not match the values of the ticket";
        this.createdTicket = createdTicket;
    }

    @Override
    public int getId() {
        if (this.createdTicket != null) {
            return createdTicket.getId();
        }
        return super.getId();
    }

    /**
     * If you ever want to view the contents of a general ticket for
     * debugging purposes, you can either use
     * ticket'sName.toString() or
     * System.out.println(ticket'sName).
     *
     * This function overrides the built-in toString function that every
     * class has so that department tickets display their contents when
     * printed rather than their location in memory.
     *
     * I have already built in functionality that does this, so if you
     * want to display the ticket bodies right before upload, go into
     * the settings class and set the DisplayTicketBodies value to true.
     * @return The contents of the ticket
     */
    public String toString() {
        String customAttributes = "{";
        for (CustomAttribute attribute : this.getAttributes()) {
            customAttributes +=
                "\n\t" + attribute.getId() + " : " + attribute.getValue() + ",";
        }
        customAttributes += "\n}";

        String nonEditableAttributes = "";
        if (this.isRetrieved())
            nonEditableAttributes = "\n\tTicketId : " + getId() +
            "\n\tParentId : " + getParentId() +
            "\n\tParentTitle : " + getParentTitle() +
            "\n\tParentClass : " + getParentClass() +
            "\n\tTypeName : " + getTypeName() +
            "\n\tTypeCategoryId : " + getTypeCategoryId() +
            "\n\tTypeCategoryName : " + getTypeCategoryName() +
            "\n\tClassification : " + getClassification() +
            "\n\tClassificationName : " + getClassificationName() +
            "\n\tFormName : " + getFormName() +
            "\n\tUri : " + getUri() +
            "\n\tAccountName : " + getAccountName() +
            "\n\tSourceName : " + getSourceName() +
            "\n\tStatusName : " + getStatusName() +
            "\n\tStatusClass : " + getStatusClass() +
            "\n\tImpactName : " + getImpactName() +
            "\n\tUrgencyName : " + getUrgencyName() +
            "\n\tPriorityName : " + getPriorityName() +
            "\n\tPriorityOrder : " + getPriorityOrder() +
            "\n\tSlaId : " + getSlaId() +
            "\n\tSlaName : " + getSlaName() +
            "\n\tSlaViolated : " + isSlaViolated() +
            "\n\tSlaRespondByViolated : " + isSlaRespondByViolated() +
            "\n\tSlaResolvedByViolated : " + isSlaResolveByViolated() +
            "\n\tRespondedByDate : " + getRespondByDate() +
            "\n\tResolvedByDate : " + getResolveByDate() +
            "\n\tSlaBeginDate : " + getSlaBeginDate() +
            "\n\tOnHold : " + isOnHold() +
            "\n\tPLacedOnHoldDate : " + getPlacedOnHoldDate() +
            "\n\tCreatedDate : " + getCreatedDate() +
            "\n\tCreatedUID : " + getCreatedUid() +
            "\n\tCreatedFullName : " + getCreatedFullName() +
            "\n\tCreatedEmail : " + getCreatedEmail() +
            "\n\tModifiedDate : " + getModifiedDate() +
            "\n\tModifiedUID : " + getModifiedUid() +
            "\n\tModifiedFullName : " + getModifiedFullName() +
            "\n\tRequesterName : " + getRequestorName() +
            "\n\tRequesterFirstName : " + getRequestorFirstName() +
            "\n\tRequesterLastName : " + getRequestorLastName() +
            "\n\tRequesterEmail : " + getRequestorEmail() +
            "\n\tRequesterPhone : " + getRequestorPhone() +
            "\n\tActualMinutes : " + getActualMinutes() +
            "\n\tDaysOld : " + getDaysOld() +
            "\n\tResponsibleFullName : " + getResponsibleFullName() +
            "\n\tResponsibleEmail : " + getResponsibleEmail() +
            "\n\tResponsibleGroupName : " + getResponsibleGroupName() +
            "\n\tRespondedDate : " + getRespondedDate() +
            "\n\tRespondedUID : " + getRespondedUid() +
            "\n\tRespondedFullName : " + getRespondedFullName() +
            "\n\tCompletedDate : " + getCompletedDate() +
            "\n\tCompletedUID : " + getCompletedUid() +
            "\n\tCompletedFullName : " + getCompletedFullName() +
            "\n\tReviewerUID : " + getReviewerUid() +
            "\n\tReviewerFullName : " + getReviewerFullName() +
            "\n\tReviewerEmail : " + getReviewerEmail() +
            "\n\tReviewingGroupId : " + getReviewingGroupId() +
            "\n\tReviewingGroupName : " + getReviewingGroupName() +
            "\n\tTimeBudgetUsed : " + getTimeBudgetUsed() +
            "\n\tExpensesBudgetUsed : " + getExpensesBudgetUsed() +
            "\n\tConvertedToTask : " + isConvertedToTask() +
            "\n\tConvertedToTaskDate : " + getConvertedToTaskDate() +
            "\n\tConvertedToTaskUid : " + getConvertedToTaskUid() +
            "\n\tConvertedToTaskFullName : " + getConvertedToTaskFullName() +
            "\n\tTaskProjectId : " + getTaskProjectId() +
            "\n\tTaskProjectName : " + getTaskProjectName() +
            "\n\tTaskPlanId : " + getTaskPlanId() +
            "\n\tTaskPlanName : " + getTaskPlanName() +
            "\n\tTaskId : " + getTaskId() +
            "\n\tTaskTitle : " + getTaskTitle() +
            "\n\tTaskStartDate : " + getTaskStartDate() +
            "\n\tTaskEndDate : " + getTaskEndDate() +
            "\n\tTaskPercentComplete : " + getTaskPercentComplete() +
            "\n\tLocationName : " + getLocationName() +
            "\n\tLocationRoomName : " + getLocationRoomName() +
            "\n\tRefCode : " + getRefCode() +
            "\n\tServiceName : " + getServiceName() +
            "\n\tServiceOfferingName : " +
            "\n\tServiceCategoryId : " + getServiceCategoryId() +
            "\n\tServiceCategoryName : " + getServiceCategoryName() +
            "\n\tArticleSubject : " + getArticleSubject() +
            "\n\tArticleStatus : " + getArticleStatus() +
            "\n\tArticleCategoryPathNames : " + getArticleCategoryPathNames() +
            "\n\tAppID : " + getAppId() +
            "\n\tAttributes : " + getAttributes() +
            "\n\tAttachments : " + getAttachments() +
            "\n\tTasks : " + getTasks() +
            "\n\tNotify : " + getNotify();
        return
            "\n      " + this.getClass().getSimpleName() +
            "\n" +
            "\n      *  - Editable" +
            "\n      *  *  - Editable and Required" +
            "\n" +
            "\n\t *  * TypeId : " + getTypeId() +
            "\n\t *  * Title : " + getTitle() +
            "\n\t *  * AccountId : " + getAccountId() +
            "\n\t *  * StatusId : " + getStatusId() +
            "\n\t *  * PriorityId : " + getPriorityId() +
            "\n\t *  * RequesterUID : " + getRequestorUid() +
            "\n\t * FormId : " + getFormId() +
            "\n\t * Description : " + getDescription() +
            "\n\t * SourceId : " + getSourceId() +
            "\n\t * ImpactId : " + getImpactId() +
            "\n\t * UrgencyId : " + getUrgencyId() +
            "\n\t * GoesOffHoldDate : " + getGoesOffHoldDate() +
            "\n\t * EstimatedMinutes : " + getEstimatedMinutes() +
            "\n\t * StartDate : " + getStartDate() +
            "\n\t * EndDate : " + getEndDate() +
            "\n\t * ResponsibleUID : " + getResponsibleUid() +
            "\n\t * ResponsibleGroupId : " + getResponsibleGroupId() +
            "\n\t * TimeBudget : " + getTimeBudget() +
            "\n\t * ExpensesBudget : " + getExpensesBudget() +
            "\n\t * LocationId : " + getLocationId() +
            "\n\t * LocationRoomId : " + getLocationRoomId() +
            "\n\t * ServiceId : " + getServiceId() +
            "\n\t * ServiceOfferingID : " +
            "\n\t * ArticleId : " + getArticleId() +
            nonEditableAttributes +
            "\n\tCustom Attributes\n" + customAttributes;
    }
}