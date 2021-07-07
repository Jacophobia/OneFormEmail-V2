package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import td.api.Logging.History;
import td.api.TeamDynamix;

public class OneformTicket extends DepartmentTicket {

    public OneformTicket(TeamDynamix api, History history, OneformTicket oneformTicket) {
        super(api, history, oneformTicket);
    }

}
