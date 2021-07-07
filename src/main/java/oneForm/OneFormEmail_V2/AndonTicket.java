package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.Logging.History;
import td.api.TeamDynamix;
import td.api.Ticket;

class AndonTicket extends GeneralTicket{

    public AndonTicket(TeamDynamix api, History history, OneformTicket oneformTicket) {
        super(oneformTicket, history);
        this.api = api;
    }

    @Override
    public void uploadTicket(Ticket ticket, int appId) {

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
