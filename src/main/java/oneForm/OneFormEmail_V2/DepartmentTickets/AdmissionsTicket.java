package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.LoggingSupervisor;
import td.api.TeamDynamix;

class AdmissionsTicket extends DepartmentTicket {

    public AdmissionsTicket(TeamDynamix api, LoggingSupervisor debug, OneformTicket oneformTicket) {
        super(api, debug, oneformTicket);
    }


}