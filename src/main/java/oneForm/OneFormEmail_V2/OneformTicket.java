package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

public class OneformTicket extends GeneralTicket {

    public OneformTicket(History history, OneformTicket oneformTicket) {
        super(oneformTicket, history);
    }

    @Override
    public void prepareTicketUpload() throws TDException {
    }
}
