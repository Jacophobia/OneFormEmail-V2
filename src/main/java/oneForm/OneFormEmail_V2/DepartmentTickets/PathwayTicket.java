package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class PathwayTicket extends DepartmentTicket {
    // Attributes for each of the different form types depending on what
    // type of ticket it is.
    private final int ACCOUNTING_TYPE_ID = 568;
    private final int ACCOUNTING_FORM_ID = 1691;
    private final int ACCOUNTING_STATUS_CLOSED = 417;
    private final int ACCOUNTING_STATUS_NEW = 413;

    private final int ADMISSIONS_TYPE_ID = 654;
    private final int ADMISSIONS_FORM_ID = 1970;
    private final int ADMISSIONS_STATUS_CLOSED = 403;
    private final int ADMISSIONS_STATUS_NEW = 399;

    private final int ADVISING_TYPE_ID = 880;
    private final int ADVISING_FORM_ID = 3355;
    private final int ADVISING_STATUS_CLOSED = 431;
    private final int ADVISING_STATUS_NEW = 427;

    private final int BYUI_TICKETS_TYPE_ID = 370;
    private final int BYUI_TICKETS_FORM_ID = 1969;
    private final int BYUI_TICKETS_STATUS_CLOSED = 200;
    private final int BYUI_TICKETS_STATUS_NEW = 196;

    private final int FINANCIAL_AID_TYPE_ID = 554;
    private final int FINANCIAL_AID_FORM_ID = 1681;
    private final int FINANCIAL_AID_STATUS_CLOSED = 410;
    private final int FINANCIAL_AID_STATUS_NEW = 406;

    private final int SRR_TYPE_ID = 666;
    private final int SRR_FORM_ID = 1975;
    private final int SRR_STATUS_CLOSED = 424;
    private final int SRR_STATUS_NEW = 420;

    private final int ACCOUNTING_TAG_ID = 10944;
    private final int BYUI_TICKETS_TAG_ID = 10945;
    private final int ADMISSIONS_TAG_ID = 10946;
    private final int FINANCIAL_AID_TAG_ID = 10947;
    private final int SRR_TAG_ID = 10948;
    private final int ADVISING_TAG_ID = 11602;

    private final String TAG_ADVISING = "36696";
    private final String TAG_APPLICATION_QUESTIONS = "36697";
    private final String TAG_COMPLAINTS_GRIEVANCES = "36698";
    private final String TAG_CHARGES_PAYMENTS = "36699";
    private final String TAG_FINANCIAL_AID = "36700";
    private final String TAG_TECHNICAL_ISSUES = "36701";
    private final String TAG_REGISTRATION_ISSUES = "36702";
    private final String TAG_ALL_OTHER_QUESTIONS = "36703";

    public PathwayTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        // If you need to add a tag to the list of available pathway
        // tickets, be sure to add it to this list. You won't need to
        // worry about changing much of the code, because this will
        // determine everything that happens after it. If the process
        // needs to be adjusted you should only need to change it here.
        switch (this.oneformTicket.getCustomAttribute(ONEFORM_TAG_ID)) {
            case TAG_ADVISING:
                applicationID = ADVISING_APP_ID;
                break;
            case TAG_APPLICATION_QUESTIONS:
                applicationID = ADMISSIONS_APP_ID;
                break;
            case TAG_COMPLAINTS_GRIEVANCES:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            case TAG_CHARGES_PAYMENTS:
                applicationID = ACCOUNTING_APP_ID;
                break;
            case TAG_FINANCIAL_AID:
                applicationID = FINANCIAL_AID_APP_ID;
                break;
            case TAG_TECHNICAL_ISSUES:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            case TAG_REGISTRATION_ISSUES:
                applicationID = SRR_APP_ID;
                break;
            case TAG_ALL_OTHER_QUESTIONS:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            default:
                assert false :
                    "There is an uncaught case in this switch statement. " +
                    "Attribute: " +
                    this.oneformTicket.getCustomAttribute(ONEFORM_TAG_ID);
                applicationID = BYUI_TICKETS_APP_ID;
        }

    }

    @Override
    protected int findTypeId() {
        assert this.applicationID != 0 :
            "The applicationID attribute has not been initialized";
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_TYPE_ID;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_TYPE_ID;
            case ADVISING_APP_ID:
                return ADVISING_TYPE_ID;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_TYPE_ID;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_TYPE_ID;
            case SRR_APP_ID:
                return SRR_TYPE_ID;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_TYPE_ID;
        }
    }

    @Override
    protected int findStatusId() {
        assert this.applicationID != 0 :
            "The applicationID attribute has not been initialized";
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return getAccountingStatusID();
            case ADMISSIONS_APP_ID:
                return getAdmissionsStatusID();
            case ADVISING_APP_ID:
                return getAdvisingStatusID();
            case BYUI_TICKETS_APP_ID:
                return getByuiTicketsStatusID();
            case FINANCIAL_AID_APP_ID:
                return getFinancialAidStatusID();
            case SRR_APP_ID:
                return getSrrStatusID();
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return getByuiTicketsStatusID();
        }
    }

    /**
     * Since Srr Does not work in Teamdynamix, their tickets will always
     * be closed. If this ever changes you can uncomment the code below
     * and delete the return statement at the beginning so that tickets
     * created in SRR are viewable in a work-list. Be sure to also
     * make this change in the SrrTicket class.
     * @return the status of the pathway department ticket created in
     * the SRR application.
     */
    private int getSrrStatusID() {
        return SRR_STATUS_CLOSED;
        // String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        // if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
        //     return SRR_STATUS_NEW;
        // }
        // else {
        //     return SRR_STATUS_CLOSED;
        // }
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
    private int getByuiTicketsStatusID() {
        return BYUI_TICKETS_STATUS_CLOSED;
        // String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        // if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
        //     return BYUI_TICKETS_STATUS_NEW;
        // }
        // else {
        //     return BYUI_TICKETS_STATUS_CLOSED;
        // }
    }

    private int getFinancialAidStatusID() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return FINANCIAL_AID_STATUS_NEW;
        }
        else {
            return FINANCIAL_AID_STATUS_CLOSED;
        }
    }

    private int getAdvisingStatusID() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return ADVISING_STATUS_NEW;
        }
        else {
            return ADVISING_STATUS_CLOSED;
        }
    }

    private int getAdmissionsStatusID() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return ADMISSIONS_STATUS_NEW;
        }
        else {
            return ADMISSIONS_STATUS_CLOSED;
        }
    }

    private int getAccountingStatusID() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return ACCOUNTING_STATUS_NEW;
        }
        else {
            return ACCOUNTING_STATUS_CLOSED;
        }
    }

    @Override
    protected int findFormId() {
        assert this.applicationID != 0 :
            "The applicationID attribute has not been initialized";
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_FORM_ID;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_FORM_ID;
            case ADVISING_APP_ID:
                return ADVISING_FORM_ID;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_FORM_ID;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_FORM_ID;
            case SRR_APP_ID:
                return SRR_FORM_ID;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_FORM_ID;
        }
    }

    @Override
    protected void setDepartmentSpecificAttributes() {

    }

}