package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class ByuiTicket extends DepartmentTicket {
    private final int [] herpas = {548,564,563,565,537,546,542,547,538,545,540,544,710,727,712,391};
    private final int TYPE_ID = 370;
    private final int FORM_ID = 1969;
    private final int STATUS_CLOSED = 200;
    private final int STATUS_NEW = 196;

    private final int TAG_ID = 10945;


    public ByuiTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = BYUI_TICKETS_APP_ID;
    }

    @Override
    protected int findTypeId() {
        return TYPE_ID;
    }

    /**
     * At the moment, there are no departments using any of the
     * department tickets being housed in the BYUI-Tickets Application,
     * so the program will always close the tickets. If this changes, be
     * sure to uncomment the code below and search for the specific
     * condition where it will need to be given a status of new. If you
     * make any changes to the code below, be sure to make those changes
     * in the getByuiTicketStatus function in the PathwayTicket class.
     * @return The status of a ticket housed in the BYUI-Tickets
     * application.
     */
    @Override
    protected int findStatusId() {
        return STATUS_CLOSED;
        // String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        // if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
        //     return STATUS_NEW;
        // }
        // else {
        //     return STATUS_CLOSED;
        // }
    }

    @Override
    protected int findFormId() {
        return FORM_ID;
    }

    @Override
    protected String findRequestorUid() {
        boolean inHerpaList = false;
        for (int herpa : herpas) {
            if (herpa == this.oneformTicket.getAccountId()) {
                inHerpaList = true;
            }
        }
        if ((!inHerpaList) && (this.findTypeId() == TYPE_ID)) {
            return "d36a035f-50e7-e311-80cc-005056ac5ec6";
        }
        return this.oneformTicket.getRequestorUid();
    }

    @Override
    protected void setDepartmentSpecificAttributes() {
        this.addCustomAttribute(TAG_ID, findTagValue());
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }


}