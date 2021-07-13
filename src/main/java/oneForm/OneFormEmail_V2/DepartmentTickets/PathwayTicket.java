package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class PathwayTicket extends DepartmentTicket {

    public PathwayTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = 54;
    }

}