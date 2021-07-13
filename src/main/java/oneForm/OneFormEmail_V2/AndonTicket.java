package oneForm.OneFormEmail_V2;

import td.api.Logging.History;

class AndonTicket extends GeneralTicket{

    public AndonTicket(History history, OneformTicket oneformTicket) {
        super(history);
    }

    @Override
    public void prepareTicketUpload() {

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
