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
        this.addCustomAttribute(OVERRIDE_ID, findOverrideEmailID());
        this.addCustomAttribute(TAG_ID, findTagValue());
    }

    private String findTagValue() {
        return oneformTicket.getAttributeText(ONEFORM_TAG_ID);
    }

    private String findOverrideEmailID() {
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