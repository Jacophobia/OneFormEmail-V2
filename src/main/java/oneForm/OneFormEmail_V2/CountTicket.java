package oneForm.OneFormEmail_V2;

import td.api.Logging.History;

class CountTicket extends GeneralTicket {

    public CountTicket(History history, OneformTicket oneformTicket) {
        super(oneformTicket, history);
    }

    @Override
    public void prepareTicketUpload() {

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
