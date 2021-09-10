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
    private final String TAG_COURSE_MATERIALS = "36926";

    private final int ACCOUNTING_ONE_FORM_TICKETID_TAG = 11013;
    private final int BYUI_TICKETS_ONE_FORM_TICKETID_TAG = 11015;
    private final int ADMISSIONS_ONE_FORM_TICKETID_TAG = 11014;
    private final int FINANCIAL_AID_ONE_FORM_TICKETID_TAG = 11016;
    private final int SRR_ONE_FORM_TICKETID_TAG = 11017;
    private final int ADVISING_ONE_FORM_TICKETID_TAG = 11601;

    private final int ACCOUNTING_BSC_AGENT_NAME = 5514;
    private final int BYUI_TICKETS_BSC_AGENT_NAME = 5520;
    private final int ADMISSIONS_BSC_AGENT_NAME = 5871;
    private final int FINANCIAL_AID_BSC_AGENT_NAME = 5523;
    private final int SRR_BSC_AGENT_NAME = 5533;
    private final int ADVISING_BSC_AGENT_NAME = 11603;

    private final int ACCOUNTING_SENT_TO_LEVEL_2 = 3290;
    private final String ACCOUNTING_SENT_TO_LEVEL_2_YES = "12813";
    private final String ACCOUNTING_SENT_TO_LEVEL_2_NO = "12814";

    private final int ADMISSIONS_SENT_TO_LEVEL_2 = 5183;
    private final String ADMISSIONS_SENT_TO_LEVEL_2_YES = "15672";
    private final String ADMISSIONS_SENT_TO_LEVEL_2_NO = "15673";

    private final int ADVISING_SENT_TO_LEVEL_2 = 11600;
    private final String ADVISING_SENT_TO_LEVEL_2_YES = "36274";
    private final String ADVISING_SENT_TO_LEVEL_2_NO = "36275";

    private final int BYUI_TICKETS_SENT_TO_LEVEL_2 = 2281;
    private final String BYUI_TICKETS_SENT_TO_LEVEL_2_YES = "5919";
    private final String BYUI_TICKETS_SENT_TO_LEVEL_2_NO = "5920";

    private final int FINANCIAL_AID_SENT_TO_LEVEL_2 = 3275;
    private final String FINANCIAL_AID_SENT_TO_LEVEL_2_YES = "12507";
    private final String FINANCIAL_AID_SENT_TO_LEVEL_2_NO = "12508";

    private final int SRR_SENT_TO_LEVEL_2 = 5467;
    private final String SRR_SENT_TO_LEVEL_2_YES = "15872";
    private final String SRR_SENT_TO_LEVEL_2_NO = "15873";

    public PathwayTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        findApplicationId();
    }

    @Override
    public void initializeTicket(History history, OneformTicket oneformTicket) {
        super.initializeTicket(history, oneformTicket);
        findApplicationId();
    }

    private void findApplicationId() {
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
            case TAG_CHARGES_PAYMENTS:
                applicationID = ACCOUNTING_APP_ID;
                break;
            case TAG_FINANCIAL_AID:
                applicationID = FINANCIAL_AID_APP_ID;
                break;
            case TAG_REGISTRATION_ISSUES:
                applicationID = SRR_APP_ID;
                break;
            case TAG_COMPLAINTS_GRIEVANCES:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            case TAG_TECHNICAL_ISSUES:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            case TAG_ALL_OTHER_QUESTIONS:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            case TAG_COURSE_MATERIALS:
                applicationID = BYUI_TICKETS_APP_ID;
                break;
            default:
                assert false :
                    "There is an uncaught case in this switch statement. " +
                        "Attribute: " +
                        this.oneformTicket.getCustomAttribute(ONEFORM_TAG_ID);
                debug.logError(
                    "An invalid tag has been used with the pathway office " +
                        "selected."
                );
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
        this.addCustomAttribute(findTagId(), findTagValue());
        this.addCustomAttribute(
            findOneFormTicketIdTag(),
            String.valueOf(oneformTicket.getId())
        );
        this.addCustomAttribute(
            findBscAgentNameId(),
            oneformTicket.getAgentName()
        );
        this.addCustomAttribute(
            findSentToLevelTwoId(),
            findEscalatedValue()
        );
    }

    private int findTagId() {
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_TAG_ID;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_TAG_ID;
            case ADVISING_APP_ID:
                return ADVISING_TAG_ID;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_TAG_ID;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_TAG_ID;
            case SRR_APP_ID:
                return SRR_TAG_ID;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_TAG_ID;
        }
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }

    private int findOneFormTicketIdTag() {
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_ONE_FORM_TICKETID_TAG;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_ONE_FORM_TICKETID_TAG;
            case ADVISING_APP_ID:
                return ADVISING_ONE_FORM_TICKETID_TAG;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_ONE_FORM_TICKETID_TAG;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_ONE_FORM_TICKETID_TAG;
            case SRR_APP_ID:
                return SRR_ONE_FORM_TICKETID_TAG;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_ONE_FORM_TICKETID_TAG;
        }
    }
    // TODO: Finish these
    private int findBscAgentNameId() {
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_BSC_AGENT_NAME;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_BSC_AGENT_NAME;
            case ADVISING_APP_ID:
                return ADVISING_BSC_AGENT_NAME;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_BSC_AGENT_NAME;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_BSC_AGENT_NAME;
            case SRR_APP_ID:
                return SRR_BSC_AGENT_NAME;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_BSC_AGENT_NAME;
        }
    }

    private int findSentToLevelTwoId() {
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                return ACCOUNTING_SENT_TO_LEVEL_2;
            case ADMISSIONS_APP_ID:
                return ADMISSIONS_SENT_TO_LEVEL_2;
            case ADVISING_APP_ID:
                return ADVISING_SENT_TO_LEVEL_2;
            case BYUI_TICKETS_APP_ID:
                return BYUI_TICKETS_SENT_TO_LEVEL_2;
            case FINANCIAL_AID_APP_ID:
                return FINANCIAL_AID_SENT_TO_LEVEL_2;
            case SRR_APP_ID:
                return SRR_SENT_TO_LEVEL_2;
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                return BYUI_TICKETS_SENT_TO_LEVEL_2;
        }
    }

    private String findEscalatedValue() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        switch (applicationID) {
            case ACCOUNTING_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return ACCOUNTING_SENT_TO_LEVEL_2_YES;
                } else {
                    return ACCOUNTING_SENT_TO_LEVEL_2_NO;
                }
            case ADMISSIONS_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return ADMISSIONS_SENT_TO_LEVEL_2_YES;
                } else {
                    return ADMISSIONS_SENT_TO_LEVEL_2_NO;
                }
            case ADVISING_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return ADVISING_SENT_TO_LEVEL_2_YES;
                } else {
                    return ADVISING_SENT_TO_LEVEL_2_NO;
                }
            case BYUI_TICKETS_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return BYUI_TICKETS_SENT_TO_LEVEL_2_YES;
                } else {
                    return BYUI_TICKETS_SENT_TO_LEVEL_2_NO;
                }
            case FINANCIAL_AID_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return FINANCIAL_AID_SENT_TO_LEVEL_2_YES;
                } else {
                    return FINANCIAL_AID_SENT_TO_LEVEL_2_NO;
                }
            case SRR_APP_ID:
                if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
                    return SRR_SENT_TO_LEVEL_2_YES;
                } else {
                    return SRR_SENT_TO_LEVEL_2_NO;
                }
            default:
                assert false :
                    "There is an uncaught case in this switch statement.";
                debug.logError(
                    this.getClass(),
                    "findEscalatedValue",
                    "There is an uncaught case in this switch statement"
                );
                return BYUI_TICKETS_SENT_TO_LEVEL_2_NO;
        }
    }
}