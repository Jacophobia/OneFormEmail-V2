package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.TeamDynamix;

public abstract class DepartmentTicket extends GeneralTicket {

    public DepartmentTicket(TeamDynamix api, LoggingSupervisor debug, OneformTicket oneformTicket) {
        super(oneformTicket, debug);
        this.api = api;
    }

    @Override
    public String toString() {
        return "DepartmentTicket{ ticketAttributes= " +
                ticketAttributes +
                " TicketID= " +
                this.getId() +
                " TicketApplicationID= " +
                this.getAppId() +
                '}';
    }

    @Override
    public void uploadTicket() {

    }
}