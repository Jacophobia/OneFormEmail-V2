package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.LoggingSupervisor;
import td.api.TeamDynamix;

class AdvisingTicket extends DepartmentTicket {

    public AdvisingTicket(TeamDynamix api, LoggingSupervisor debug, OneformTicket oneformTicket) {
        super(api, debug, oneformTicket);
    }


}