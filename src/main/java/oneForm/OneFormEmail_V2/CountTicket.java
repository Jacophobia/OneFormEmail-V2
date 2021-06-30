package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.TeamDynamix;
import td.api.Ticket;

class CountTicket extends GeneralTicket {

    public CountTicket(TeamDynamix api, LoggingSupervisor debug, OneformTicket oneformTicket) {
        super(oneformTicket, debug);
        this.api = api;
    }

    @Override
    public void uploadTicket() {

    }

    @Override
    public String toString() {
        return "CountTicket{ ticketAttributes= " +
                ticketAttributes +
                " TicketID= " +
                this.getId() +
                " TicketApplicationID= " +
                this.getAppId() +
                '}';
    }
}
