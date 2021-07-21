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
    protected void setDepartmentSpecificAttributes() {
        this.addCustomAttribute(TAG_ID, findTagValue());
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }


}