package oneForm.OneFormEmail_V2;

import td.api.TeamDynamix;

public abstract class DepartmentTicket extends GeneralTicket {

    public DepartmentTicket(TeamDynamix api) {
        super();
        this.api = api;
    }

}