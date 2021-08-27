package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class AdmissionsTicket extends DepartmentTicket {
    private final int TYPE_ID = 654;
    private final int FORM_ID = 1970;
    private final int STATUS_CLOSED = 403;
    private final int STATUS_NEW = 399;

    private final int TAG_ID = 10946;

    private final int    ONEFORM_OVERRIDE_ID = 10943;
    private final String ONEFORM_OVERRIDE_YES = "34016";
    private final String ONEFORM_OVERRIDE_NO = "34017";
    private final String ONEFORM_OVERRIDE_DOESNT_NEED = "34018";

    private final int    OVERRIDE_ID = 8171;
    private final String OVERRIDE_YES = "22635";
    private final String OVERRIDE_NO = "22636";
    private final String OVERRIDE_DOESNT_NEED = "32854";

    private final int ONE_FORM_TICKETID_TAG = 11014;

    private final int BSC_AGENT_NAME = 5871;

    private final int    SENT_TO_LEVEL_2 = 5183;
    private final String SENT_TO_LEVEL_2_YES = "15672";
    private final String SENT_TO_LEVEL_2_NO = "15673";

    public AdmissionsTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        applicationID = ADMISSIONS_APP_ID;
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
        if (oneformTicket.containsAttribute(ONEFORM_OVERRIDE_ID))
            this.addCustomAttribute(OVERRIDE_ID, findOverrideEmailID());
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

    private String findOverrideEmailID() {
        assert oneformTicket.containsAttribute(ONEFORM_OVERRIDE_ID) :
            "The oneform ticket does not contain the override attribute";
        switch(this.getCustomAttribute(ONEFORM_OVERRIDE_ID)) {
            case ONEFORM_OVERRIDE_YES:
                return OVERRIDE_YES;
            case ONEFORM_OVERRIDE_NO:
                return OVERRIDE_NO;
            case ONEFORM_OVERRIDE_DOESNT_NEED:
                return OVERRIDE_DOESNT_NEED;
            default:
                assert false :
                    "There is an uncaught case in this switch statement";
                return OVERRIDE_DOESNT_NEED;
        }
    }


}