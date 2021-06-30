package oneForm.OneFormEmail_V2;

import oneForm.OneFormEmail_V2.DepartmentTickets.OneformTicket;
import td.api.CustomAttribute;
import td.api.Logging.History;
import td.api.TeamDynamix;
import td.api.Ticket;

import java.util.HashMap;
import java.util.Map;

abstract class GeneralTicket extends Ticket {
    protected TeamDynamix api;
    protected LoggingSupervisor debug;
    protected Map<Integer, String> ticketAttributes = new HashMap<>();
    protected OneformTicket oneformTicket;

    public GeneralTicket(OneformTicket oneformTicket, LoggingSupervisor debug) {
        super();
        this.oneformTicket = oneformTicket;
        this.debug = debug;
        for (CustomAttribute attribute : this.getAttributes()) {
            ticketAttributes.put(attribute.getId(), attribute.getValue());
        }
    }

    abstract public void uploadTicket();

    public String getAttribute(int attributeID){
        assert ticketAttributes.containsKey(attributeID) : "Error attribute not found.";
        return ticketAttributes.get(attributeID);
    }
}