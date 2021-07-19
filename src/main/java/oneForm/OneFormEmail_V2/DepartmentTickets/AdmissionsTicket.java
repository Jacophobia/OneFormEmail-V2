package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class AdmissionsTicket extends DepartmentTicket {
    private final int TYPE_ID = 654;
    private final int FORM_ID = 1970;
    private final int STATUS_CLOSED = 403;
    private final int STATUS_NEW = 399;

    public AdmissionsTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = ADMISSIONS_APP_ID;
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


}