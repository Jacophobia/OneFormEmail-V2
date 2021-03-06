package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class AdvisingTicket extends DepartmentTicket {
    private final int TYPE_ID = 880;
    private final int FORM_ID = 3355;
    private final int STATUS_CLOSED = 431;
    private final int STATUS_NEW = 427;

    private final int TAG_ID = 11602;

    private final int ONE_FORM_TICKETID_TAG = 11601;

    private final int BSC_AGENT_NAME = 11603;

    private final int    SENT_TO_LEVEL_2 = 11600;
    private final String SENT_TO_LEVEL_2_YES = "36274";
    private final String SENT_TO_LEVEL_2_NO = "36275";

    public AdvisingTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = ADVISING_APP_ID;
    }

    @Override
    protected int findTypeId() {
        return TYPE_ID;
    }

    @Override
    protected int findStatusId() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return STATUS_NEW;
        }
        else {
            return STATUS_CLOSED;
        }
    }

    @Override
    protected int findFormId() {
        return FORM_ID;
    }

    @Override
    protected void setDepartmentSpecificAttributes() {}
    protected String findEscalatedValue() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return SENT_TO_LEVEL_2_YES;
        }
        else {
            return SENT_TO_LEVEL_2_NO;
        }
    }

    @Override
    protected int findTicketFeedID() {
        return 11647;
    }

    @Override
    protected int findOneformTagId() {
        return TAG_ID;
    }

    @Override
    protected int findOneformTicketIdId() {
        return ONE_FORM_TICKETID_TAG;
    }

    @Override
    protected int findBSCAgentNameId() {
        return BSC_AGENT_NAME;
    }

    @Override
    protected int findSentToLevel2Id() {
        return SENT_TO_LEVEL_2;
    }
}