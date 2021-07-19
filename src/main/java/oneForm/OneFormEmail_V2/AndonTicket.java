package oneForm.OneFormEmail_V2;

import td.api.Logging.History;

class AndonTicket extends GeneralTicket{
    OneformTicket oneformTicket;

    public AndonTicket(History history, OneformTicket oneformTicket) {
        super(history);
        this.oneformTicket = oneformTicket;
    }

    @Override
    public void prepareTicketUpload() {

    }
}
