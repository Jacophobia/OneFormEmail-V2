package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

class CountTicket extends GeneralTicket {
    OneformTicket oneformTicket;
    RequestCollector.ACTION_REQUESTED ACTION;
    private final int FORM_ID = 3343;
    private final int TYPE_ID = 632;
    private final int ACCOUNT_ID = 548;
    private final int STATUS_ID = 214;
    private final int PRIORITY_ID = 21;
    private final int DATA_DATE = 3057;
    private final int EMAIL_VOLUME = 11509;
    private final int SPAM_VOLUME = 11510;
    private final int REPLY_VOLUME = 11511;
    private final int ESCALATED_VOLUME = 11361;

    private boolean is_new_ticket = false;

    public CountTicket(History history, OneformTicket oneformTicket,
                       RequestCollector.ACTION_REQUESTED ACTION) {
        super(history);
        this.is_new_ticket = true;
        this.initializeTicket(history, oneformTicket, ACTION);
        this.addCustomAttribute(EMAIL_VOLUME, "0");
        this.addCustomAttribute(SPAM_VOLUME, "0");
        this.addCustomAttribute(REPLY_VOLUME, "0");
        this.addCustomAttribute(ESCALATED_VOLUME, "0");
    }

    public void initializeTicket(History history, OneformTicket oneformTicket,
                                 RequestCollector.ACTION_REQUESTED ACTION) {
        if (!is_new_ticket) {
            super.initializeTicket(history);
        }
        this.ACTION = ACTION;
        this.applicationID = 42;
        this.oneformTicket = oneformTicket;
        this.setFormId(FORM_ID);
        this.setTypeId(TYPE_ID);
        this.setTitle("Individual Agent Daily Data - Email");
        this.setAccountId(ACCOUNT_ID);
        this.setStatusId(STATUS_ID);
        this.setPriorityId(PRIORITY_ID);
        this.setRequestorUid(oneformTicket.getAgentUID());
        if (oneformTicket.getFeed().size() > 0)
            this.addCustomAttribute(
                DATA_DATE,
                oneformTicket.getFeed().get(0).getCreatedDate().toString()
            );
        else
            this.addCustomAttribute(
                DATA_DATE, oneformTicket.getCreatedDate().toString()
            );
    }
    public void incrementCount(){
        switch (ACTION) {
            case SPAM:
                this.incrementSpam();
                break;
            case RESOLVED:
                this.incrementResolved();
                break;
            case ESCALATED:
                this.incrementEscalated();
                break;
            default:
                debug.logWarning(
                    "There is a missing case in this switch statement."
                );
                return;
        }
        incrementOverall();
    }

    private void incrementSpam(){
        int spamVolume = (int) Float.parseFloat(
            this.getCustomAttribute(SPAM_VOLUME)
        );
        debug.logNote("Original SPAM count : " + spamVolume);
        spamVolume++;
        this.addCustomAttribute(SPAM_VOLUME, String.valueOf(spamVolume));
        debug.logNote("Updated SPAM count : " + spamVolume);
    }

    private void incrementResolved() {
        int resolvedVolume = (int) Float.parseFloat(
            this.getCustomAttribute(REPLY_VOLUME)
        );
        debug.logNote("Original RESOLVED count : " + resolvedVolume);
        resolvedVolume++;
        this.addCustomAttribute(REPLY_VOLUME, String.valueOf(resolvedVolume));
        debug.logNote("Updated RESOLVED count : " + resolvedVolume);
    }

    private void incrementEscalated() {
        int escalatedVolume = (int) Float.parseFloat(
            this.getCustomAttribute(ESCALATED_VOLUME)
        );
        debug.logNote("Original ESCALATED count : " + escalatedVolume);
        escalatedVolume++;
        this.addCustomAttribute(
            ESCALATED_VOLUME,
            String.valueOf(escalatedVolume)
        );
        debug.logNote("Updated ESCALATED count : " + escalatedVolume);
    }

    private void incrementOverall() {
        int totalVolume = (int) Float.parseFloat(
            this.getCustomAttribute(EMAIL_VOLUME)
        );
        debug.logNote("Original Overall count : " + totalVolume);
        totalVolume++;
        this.addCustomAttribute(EMAIL_VOLUME, String.valueOf(totalVolume));
        debug.logNote("Updated Overall count : " + totalVolume);

    }

    @Override
    public void prepareTicketUpload() throws TDException {
        this.incrementCount();
        super.prepareTicketUpload();
    }
}
