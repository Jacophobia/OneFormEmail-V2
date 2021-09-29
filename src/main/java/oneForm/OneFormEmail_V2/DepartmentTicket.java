package oneForm.OneFormEmail_V2;

import td.api.*;
import td.api.Exceptions.TDException;
import td.api.Logging.History;

import static oneForm.OneFormEmail_V2.ProcessRequest.pull;
import static oneForm.OneFormEmail_V2.ProcessRequest.push;

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
    private boolean feedCleared = false;


    public DepartmentTicket(History history, OneformTicket oneformTicket) {
        super(history);

        assert oneformTicket != null :
            "The oneFormTicket from the parameters has not been initialized";
        this.oneformTicket = oneformTicket;
    }

    public void initializeTicket(History history, OneformTicket oneformTicket) {
        super.initializeTicket(history);
        this.oneformTicket = oneformTicket;
        if (this.isRetrieved()) {
            TeamDynamix api = Settings.sandbox ? push : pull;
            try {
                if (this.getAppId() != 0 && this.getId() != 0)
                    this.feed = api.getTicketFeedEntries(
                        this.getAppId(), this.getId()
                    );
            } catch (TDException exception) {
                debug.logWarning("Unable to retrieve the ticket feed");
            }
        }
    }

    @Override
    public int getId() {
        if (this.createdTicket != null)
            return createdTicket.getId();
        return super.getId();
    }

    public boolean isRetrieved() {
        return retrieved;
    }

    @Override
    public void prepareTicketUpload() throws TDException {
        setRequiredAttributes();
        setAdditionalAttributes();
        setDepartmentSpecificAttributes();
        super.prepareTicketUpload();
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
        // ticket feed updater
        this.addCustomAttribute(findTicketFeedID(), findTicketFeedContent());
        this.addCustomAttribute(
            findOneformTagId(),
            oneformTicket.getAttributeText(ONEFORM_TAG_ID)
        );
        this.addCustomAttribute(
            findOneformTicketIdId(),
            String.valueOf(oneformTicket.getId())
        );
        this.addCustomAttribute(
            findBSCAgentNameId(),
            oneformTicket.getAgentName()
        );
        this.addCustomAttribute(findSentToLevel2Id(), findEscalatedValue());
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
    abstract protected int findTicketFeedID();

    private String findTicketFeedContent() {
        if (!feedCleared) {
            feedCleared = true;
            return "\n";
        }
        String name;
        String feedContent;
        String feed = "\n";
        String feedName;
        String feedBody;
        String feedTime;
        int counter = 0;
        for (ItemUpdate feedItem : oneformTicket.getFeed()) {
            assert feedItem != null : "Feed Item has no value";
            name = feedItem.getCreatedFullName();

            if (
                !name.equals("BSC Robot")        &&
                !name.equals("System")           &&
                !name.equals("Ipaas Automation") &&
                !name.equals("Automation Robot") &&
                !name.equals("BYUI Ticketing")
            ) {
                feedContent = "";
                String feedItemTitle = "";
                if (feedItem.getBody().contains("<br /><br />") ||
                    (!feedItem.getBody().contains("Changed ") &&
                        !feedItem.getBody().contains("Approved") &&
                        !feedItem.getBody().contains("Rejected this") &&
                        !feedItem.getBody().contains("Skipped the") &&
                        !feedItem.getBody().contains("Removed the") &&
                        !feedItem.getBody().contains("Edited this"))
                ) {
                    feedTime = " " + feedItem.getCreatedDate().toString() + ":";
                    feedName = feedItem.getCreatedFullName() + feedTime + "\n";
                    feedBody = feedItem.getBody();
//                    feedBody = feedBody.replaceAll("^(.|\n)*<br /><br />", "");
                    feedContent += feedName + feedBody + "\n\n";
                    for (ItemUpdateReply reply : feedItem.getReplies()) {
                        feedTime = " " + reply.getCreatedDate().toString() +":";
                        feedName = reply.getCreatedFullName() + feedTime + "\n";
                        feedBody = reply.getBody();
                        feedContent += feedName + feedBody + "\n\n";
                    }
                    feedItemTitle = feedName + feedTime;
                }

                if (!feedItemTitle.equals("")) {
                    boolean isCopy = false;
                    if (!feedContent.equals("") && getFeed() != null) {
                        assert this.getFeed() != null : "There is no feed";
                        for (ItemUpdate ticketFeed : this.getFeed()) {
                            if (ticketFeed.getBody().contains(feedItemTitle)) {
                                isCopy = true;
                                counter++;
                            }
                        }
                    }
                    if (!isCopy)
                        feed += feedContent;
                }
            }
        }
        feed = feed.replaceAll("<br/>", "\n");
        feed = feed.replaceAll("<br />", "\n");
        feed = feed.replaceAll("&quot;", "\"");
        feed = feed.replaceAll("&#39;", "'");
        feed = feed.replaceAll("<b>", "");
        feed = feed.replaceAll("</b>", "");
        debug.logNote(counter + " emails already located in feed.");
        return feed;
    }


    private String findDescription() {
        return oneformTicket.getDescription();
    }

    private int findLocationId() {
        return SUPPORT_CENTER_LOCATION_ID;
    }

    private int findSourceId() {
        return EMAIL_SOURCE_ID;
    }

    abstract protected int findOneformTagId();
    abstract protected int findOneformTicketIdId();
    abstract protected int findBSCAgentNameId();
    abstract protected int findSentToLevel2Id();
    abstract protected String findEscalatedValue();

    /**
     * Department Specific Attributes:
     * These attributes are not required to upload the ticket, but are
     * required by the department whose application they will be saved
     * in.
     */
    abstract protected void setDepartmentSpecificAttributes();
    // TODO: Add all of the common custom attributes to the additional
    //  attributes method and use abstract find methods

    @Override
    public void setCreatedTicket(Ticket createdTicket) {
        assert createdTicket.getAppId() == this.getAppId() :
            "The created ticket does not match the values of the ticket";
        this.createdTicket = createdTicket;
        this.setTypeId(createdTicket.getTypeId());
        this.setTitle(createdTicket.getTitle());
        this.setAccountId(createdTicket.getAccountId());
        this.setStatusId(createdTicket.getStatusId());
        this.setPriorityId(createdTicket.getPriorityId());
        this.setRequestorUid(createdTicket.getRequestorUid());
        this.setFormId(createdTicket.getFormId());
        this.setDescription(createdTicket.getDescription());
        this.setSourceId(createdTicket.getSourceId());
        this.setImpactId(createdTicket.getImpactId());
        this.setUrgencyId(createdTicket.getUrgencyId());
        this.setEstimatedMinutes(createdTicket.getEstimatedMinutes());
        this.setStartDate(createdTicket.getStartDate());
        this.setEndDate(createdTicket.getEndDate());
        this.setResponsibleUid(createdTicket.getResponsibleUid());
        this.setResponsibleGroupId(createdTicket.getResponsibleGroupId());
        this.setTimeBudget(createdTicket.getTimeBudget());
        this.setExpensesBudget(createdTicket.getExpensesBudget());
        this.setLocationId(createdTicket.getLocationId());
        this.setLocationRoomId(createdTicket.getLocationRoomId());
        this.setServiceId(createdTicket.getServiceId());
        this.setArticleId(createdTicket.getArticleId());
        debug.logNote(
            "Updated the " + this.getClass().getSimpleName() + " with info " +
                "from the retrieved ticket."
        );
    }
}