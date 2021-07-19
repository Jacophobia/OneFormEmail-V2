package oneForm.OneFormEmail_V2;

import td.api.Logging.History;

class CountTicket extends GeneralTicket {
    OneformTicket oneformTicket;

    public CountTicket(History history, OneformTicket oneformTicket) {
        super(history);
        this.oneformTicket = oneformTicket;
    }

    @Override
    public void prepareTicketUpload() {

    }
}
