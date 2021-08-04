package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;

class AndonTicket extends GeneralTicket{
    OneformTicket oneformTicket;
    private final int FORM_ID = 3068;
    private final int TYPE_ID = 632;
    private final int STATUS_NEW = 210;
    private final int PRIORITY_MEDIUM = 21;
    private final int APP_ID = 42;
    private final int ANDON_NOTES = 10379;
    private final int ONEFORM_ID = 7901;
    private final int NOTES_ID = 9510;
    private final int OFFICE_ID = 11042;
    private final int ONEFORM_OFFICE_ID = 10329;
    private final int TAG_ID = 11041;
    private final int ONEFORM_TAG_ID = 10333;



    public AndonTicket(History history, OneformTicket oneformTicket) {
        super(history);
        this.oneformTicket = oneformTicket;
        this.setFormId(FORM_ID);
        this.setTypeId(TYPE_ID);
        this.setTitle("Andon Cord CX Follow-Up");
        this.setAccountId(oneformTicket.getAccountId());
        this.setStatusId(STATUS_NEW);
        this.setPriorityId(PRIORITY_MEDIUM);
        this.setRequestorUid(oneformTicket.getRequestorUid());
        this.setApplicationID(APP_ID);
        this.addCustomAttribute(
            ONEFORM_ID,
            String.valueOf(oneformTicket.getId())
        );
        this.addCustomAttribute(
            NOTES_ID,
            oneformTicket.getCustomAttribute(ANDON_NOTES)
        );
        this.addCustomAttribute(
            OFFICE_ID,
            oneformTicket.getAttributeText(ONEFORM_OFFICE_ID)
        );
        this.addCustomAttribute(
            TAG_ID,
            oneformTicket.getAttributeText(ONEFORM_TAG_ID)
        );
    }

    @Override
    public void prepareTicketUpload() throws TDException {
        super.prepareTicketUpload();
    }
}
