package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class SrrTicket extends DepartmentTicket {
    private final int TYPE_ID = 666;
    private final int FORM_ID = 1975;
    private final int STATUS_CLOSED = 424;
    private final int STATUS_NEW = 420;

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


}