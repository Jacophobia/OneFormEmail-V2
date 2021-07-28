package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class FinancialAidTicket extends DepartmentTicket {
    private final int TYPE_ID = 554;
    private final int FORM_ID = 1681;
    private final int STATUS_CLOSED = 410;
    private final int STATUS_NEW = 406;
    private final int LAST_NAME_INITIAL = 3276;
    private final int TAG_ID = 10947;

    private final int ONE_FORM_TICKETID_TAG = 11016;

    private final int BSC_AGENT_NAME = 5523;

    private final int    SENT_TO_LEVEL_2 = 3275;
    private final String SENT_TO_LEVEL_2_YES = "12507";
    private final String SENT_TO_LEVEL_2_NO = "12508";

    public FinancialAidTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = FINANCIAL_AID_APP_ID;
    }

    @Override
    protected int findTypeId() {
        return TYPE_ID;
    }

    @Override
    protected int findStatusId() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return STATUS_NEW;
        }
        else {
            return STATUS_CLOSED;
        }
    }

    @Override
    protected int findFormId() {
        return FORM_ID;
    }

    @Override
    protected void setDepartmentSpecificAttributes() {
        this.addCustomAttribute(LAST_NAME_INITIAL, findLastNameInitialID());
        this.addCustomAttribute(TAG_ID, findTagValue());
        this.addCustomAttribute(
            ONE_FORM_TICKETID_TAG,
            String.valueOf(oneformTicket.getId())
        );
        this.addCustomAttribute(
            BSC_AGENT_NAME,
            oneformTicket.getAgentName()
        );
        this.addCustomAttribute(SENT_TO_LEVEL_2, findEscalatedValue());
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }

    private String findEscalatedValue() {
        String action = oneformTicket.getCustomAttribute(EMAIL_ACTIONS_ATTR);
        if (action.equals(EMAIL_ACTIONS_CHOICE_ESCALATE)) {
            return SENT_TO_LEVEL_2_YES;
        }
        else {
            return SENT_TO_LEVEL_2_NO;
        }
    }

    private String findLastNameInitialID() {
        switch (Character.toUpperCase(
            oneformTicket.getRequestorLastName().charAt(0))) {
            case 'A':
                return "12509";
            case 'B':
                return "12510";
            case 'C':
                return "12511";
            case 'D':
                return "12535";
            case 'E':
                return "12512";
            case 'F':
                return "12513";
            case 'G':
                return "12514";
            case 'H':
                return "12515";
            case 'I':
                return "12516";
            case 'J':
                return "12517";
            case 'K':
                return "12518";
            case 'L':
                return "12519";
            case 'M':
                return "12520";
            case 'N':
                return "12521";
            case 'O':
                return "12522";
            case 'P':
                return "12523";
            case 'Q':
                return "12524";
            case 'R':
                return "12525";
            case 'S':
                return "12526";
            case 'T':
                return "12527";
            case 'U':
                return "12528";
            case 'V':
                return "12529";
            case 'W':
                return "12530";
            case 'X':
                return "12531";
            case 'Y':
                return "12533";
            case 'Z':
                return "12534";
            default:
                assert false :
                    "There is an uncaught case in this switch statement " +
                    "Last Initial: " +
                    oneformTicket.getRequestorLastName().charAt(0);
                return "12509"; // A
        }
    }
}
