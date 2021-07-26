package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

import static oneForm.OneFormEmail_V2.RequestCollector.ACTION_REQUESTED.*;

public class OneformTicket extends GeneralTicket {
    protected final int EMAIL_ACTIONS_ATTR = 11398;
    protected final String EMAIL_ACTIONS_CHOICE_ESCALATE = "35388";
    protected final String EMAIL_ACTIONS_CHOICE_RESOLVED_KB = "35389";

    public OneformTicket(History history) {
        super(history);
    }

    public void initializeTicket(History history,
                                 RequestCollector.ACTION_REQUESTED ACTION) {
        super.initializeTicket(history);
        if (ACTION == ESCALATED) {
            this.addCustomAttribute(
                EMAIL_ACTIONS_ATTR,
                EMAIL_ACTIONS_CHOICE_ESCALATE
            );
        }
        else {
            this.addCustomAttribute(
                EMAIL_ACTIONS_ATTR,
                EMAIL_ACTIONS_CHOICE_RESOLVED_KB
            );
        }

        }

    @Override
    public void prepareTicketUpload() throws TDException {
    }
}
