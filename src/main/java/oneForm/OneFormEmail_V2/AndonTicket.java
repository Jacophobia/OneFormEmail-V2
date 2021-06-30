package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.TeamDynamix;

class AndonTicket extends GeneralTicket{

    public AndonTicket(TeamDynamix api, LoggingSupervisor debug, OneformTicket oneformTicket) {
        super(oneformTicket, debug);
        this.api = api;
    }

    @Override
    public void uploadTicket() {

    }
    @Override
    public String toString() {
        return "AndonTicket{ ticketAttributes= " +
                ticketAttributes +
                " TicketID= " +
                this.getId() +
                " TicketApplicationID= " +
                this.getAppId() +
                '}';
    }
}
