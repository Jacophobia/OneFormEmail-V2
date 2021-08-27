package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class AccountingTicket extends DepartmentTicket {
    private final int    TYPE_ID = 568;
    private final int    FORM_ID = 1691;
    private final int    STATUS_CLOSED = 417;
    private final int    STATUS_NEW = 413;

    private final int    TAG_ID = 10944;

    private final int    ONE_FORM_TICKETID_TAG = 11013;

    private final int    BSC_AGENT_NAME = 5514;

    private final int    SENT_TO_LEVEL_2 = 3290;
    private final String SENT_TO_LEVEL_2_YES = "12813";
    private final String SENT_TO_LEVEL_2_NO = "12814";

    public AccountingTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = ACCOUNTING_APP_ID;
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
    protected void setDepartmentSpecificAttributes() {
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


}