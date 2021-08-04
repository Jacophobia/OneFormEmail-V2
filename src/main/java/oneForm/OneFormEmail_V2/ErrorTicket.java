package oneForm.OneFormEmail_V2;

import td.api.CustomAttribute;
import td.api.Ticket;

public class ErrorTicket extends Ticket {
    public ErrorTicket(String ticketID) {
        this.setFormId(3442);
        this.setTypeId(902);
        this.setTitle("OneForm Error Ticket");
        this.setAccountId(531);
        this.setStatusId(210);
        this.setPriorityId(21);
        this.setRequestorUid("f6c1bbd5-c4a2-eb11-85aa-0050f2eef487");
        this.setDescription("Errors\n");
        this.getAttributes().add(new CustomAttribute(
            12204,
            ticketID)
        );
    }

    public ErrorTicket(String ticketID, String error) {
        this(ticketID);
        this.setDescription("Errors\n" + error);
    }

    public void logError(String error) {
        String errorLog = this.getDescription();
        this.setDescription("\n" + errorLog);
    }
}
