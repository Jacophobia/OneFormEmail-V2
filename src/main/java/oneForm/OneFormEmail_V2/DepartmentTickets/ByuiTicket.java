package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.LoggingSupervisor;
import td.api.Logging.History;
import td.api.TeamDynamix;

public class ByuiTicket extends DepartmentTicket {

    public ByuiTicket(TeamDynamix api, History history, OneformTicket oneformTicket) {
        super(api, history, oneformTicket);
    }


}