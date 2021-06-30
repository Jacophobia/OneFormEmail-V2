package oneForm.OneFormEmail_V2;

import td.api.CustomAttribute;
import td.api.TeamDynamix;
import td.api.Ticket;

import java.util.HashMap;
import java.util.Map;

abstract class GeneralTicket extends Ticket {
    protected TeamDynamix api;
    protected Map<Integer, String> ticketAttributes = new HashMap<>();

    public GeneralTicket() {
        super();
        for (CustomAttribute attribute : this.getAttributes()) {
            ticketAttributes.put(attribute.getId(), attribute.getValue());
        }
    }

    abstract public void uploadTicket();

    public String getAttribute(int attributeID){
        assert ticketAttributes.containsKey(attributeID) : "Error attribute not found.";
        return ticketAttributes.get(attributeID);
    };
}