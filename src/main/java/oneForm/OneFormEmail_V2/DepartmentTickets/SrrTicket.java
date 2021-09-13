package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class SrrTicket extends DepartmentTicket {
    private final int TYPE_ID = 666;
    private final int FORM_ID = 1975;
    private final int STATUS_CLOSED = 424;
    private final int STATUS_NEW = 420;

    private final int TAG_ID = 10948;

    private final int    ONLINE_OR_CAMPUS_ID = 9829;
    private final String ON_CAMPUS_ID = "30087";
    private final String ONLINE_ID = "30088";

    private final int    ONEFORM_ONLINE_OR_CAMPUS_ID = 11044;
    private final String ONEFORM_ON_CAMPUS_ID = "34478";
    private final String ONEFORM_ONLINE_ID = "34479";

    private final int    ONLINE_FILLED_ID = 9851;
    private final String ONLINE_FILLED_YES_ID = "30163";
    private final String ONLINE_FILLED_NO_ID = "30164";

    private final int    ONEFORM_ONLINE_FILLED_ID = 11045;
    private final String ONEFORM_ONLINE_FILLED_YES_ID = "34480";
    private final String ONEFORM_ONLINE_FILLED_NO_ID = "34481";

    private final int ONE_FORM_TICKETID_TAG = 11017;

    private final int BSC_AGENT_NAME = 5533;

    private final int    SENT_TO_LEVEL_2 = 5467;
    private final String SENT_TO_LEVEL_2_YES = "15872";
    private final String SENT_TO_LEVEL_2_NO = "15873";

    public SrrTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = SRR_APP_ID;
    }

    @Override
    protected int findTypeId() {
        return TYPE_ID;
    }

    /**
     * Since Srr Does not work in Teamdynamix, their tickets will always
     * be closed. If this ever changes you can uncomment the code below
     * and delete the return statement at the beginning so that tickets
     * created in SRR are viewable in a work-list. Be sure to also
     * make this change in the PathwayTicket class.
     * @return the status of the SRR department ticket
     */
    @Override
    protected int findStatusId() {
        return STATUS_CLOSED;
        // String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        // if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
        //     return STATUS_NEW;
        // }
        // else {
        //     return STATUS_CLOSED;
        // }
    }

    @Override
    protected int findFormId() {
        return FORM_ID;
    }

    @Override
    protected void setDepartmentSpecificAttributes() {
        if (findOnlineOrCampus() != null) {
            this.addCustomAttribute(ONLINE_OR_CAMPUS_ID, findOnlineOrCampus());
            this.addCustomAttribute(ONLINE_FILLED_ID, findOnlineFilled());
        }
        this.addCustomAttribute(TAG_ID, findTagValue());
        this.addCustomAttribute(
            ONE_FORM_TICKETID_TAG,
            String.valueOf(oneformTicket.getId())
        );
        this.addCustomAttribute(
            BSC_AGENT_NAME,
            oneformTicket.getAgentName()
        );
        this.addCustomAttribute(SENT_TO_LEVEL_2, findEscalatedValue());
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }

    private String findEscalatedValue() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return SENT_TO_LEVEL_2_YES;
        }
        else {
            return SENT_TO_LEVEL_2_NO;
        }
    }

    private String findOnlineOrCampus() {
        if (!oneformTicket.containsAttribute(ONEFORM_ONLINE_OR_CAMPUS_ID))
            return null;
        switch (oneformTicket.getCustomAttribute(ONEFORM_ONLINE_OR_CAMPUS_ID)) {
            case ONEFORM_ON_CAMPUS_ID:
                return ON_CAMPUS_ID;
            case ONEFORM_ONLINE_ID:
                return ONLINE_ID;
            default:
                return null;
        }
    }

    private String findOnlineFilled() {
        switch (oneformTicket.getCustomAttribute(ONEFORM_ONLINE_FILLED_ID)) {
            case ONEFORM_ONLINE_FILLED_YES_ID:
                return ONLINE_FILLED_YES_ID;
            case ONEFORM_ONLINE_FILLED_NO_ID:
                return ONLINE_FILLED_NO_ID;
            default:
                assert false :
                    "There is an uncaught case in this switch statement";
                return ONLINE_FILLED_YES_ID;
        }
    }

    @Override
    protected int findTicketFeedID() {
        return 11518;
    }
}
