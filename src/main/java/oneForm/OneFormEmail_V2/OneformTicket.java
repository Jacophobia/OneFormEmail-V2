package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.ItemUpdate;
import td.api.Logging.History;

import java.util.ArrayList;

import static oneForm.OneFormEmail_V2.ProcessRequest.pull;
import static oneForm.OneFormEmail_V2.RequestCollector.ACTION_REQUESTED.*;

public class OneformTicket extends GeneralTicket {
    private final int ACTIONS_ATTR = 11398;
    private final String ACTIONS_CHOICE_ESCALATE = "35388";
    private final String ACTIONS_CHOICE_RESOLVED_KB = "35389";
    private final int ANDON_TICKET_ID = 12221;
    private final int OFFICE_ID = 10329;
    private final String OFFICE_SPAM = "36076";

    private String DEFAULT_UID = "e578bb85-0041-e511-80d1-005056ac5ec6";
    ArrayList<ItemUpdate> feed;
    private String agentName;
    private String agentUID;



    public OneformTicket(History history) {
        super(history);
    }

    public void initializeTicket(History history,
            RequestCollector.ACTION_REQUESTED ACTION) throws TDException {
        super.initializeTicket(history);

        if (ACTION == ESCALATED)
            this.addCustomAttribute(ACTIONS_ATTR, ACTIONS_CHOICE_ESCALATE);
        else
            this.addCustomAttribute(ACTIONS_ATTR, ACTIONS_CHOICE_RESOLVED_KB);

        if (ACTION == SPAM)
            this.addCustomAttribute(OFFICE_ID, OFFICE_SPAM);

        this.feed = pull.getTicketFeedEntries(this.getAppId(), this.getId());
        boolean found = false;
        String name;
        for (ItemUpdate itemUpdate : feed) {
            name = itemUpdate.getCreatedFullName();
            if (
                !name.equals("BSC Robot") &&
                !name.equals("System") &&
                !found
            ) {
                this.agentName = itemUpdate.getCreatedFullName();
                this.agentUID = itemUpdate.getCreatedUid();
                found = true;
            }
        }
        if (this.agentName == null || this.agentName.isEmpty()) {
            debug.logWarning(
                this.getClass(),
                "InitializeTicket",
                "No agent name found, using Albus Dumbledore"
            );
            this.agentName = "Albus Dumbledore";
        }
        if (this.agentUID == null || this.agentUID.isEmpty()) {
            debug.logWarning(
                this.getClass(),
                "InitializeTicket",
                "No agent UID found, using Albus Dumbledore"
            );
            this.agentUID = DEFAULT_UID;
        }
    }

    public ArrayList<ItemUpdate> getFeed() {
        return this.feed;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentUID() {
        return agentUID;
    }

    @Override
    public void prepareTicketUpload() throws TDException {
        super.prepareTicketUpload();
    }

    public void setAndonId(int andonId) {
        this.addCustomAttribute(ANDON_TICKET_ID, String.valueOf(andonId));
    }
}
