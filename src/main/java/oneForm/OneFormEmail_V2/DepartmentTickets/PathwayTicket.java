package oneForm.OneFormEmail_V2.DepartmentTickets;

import oneForm.OneFormEmail_V2.DepartmentTicket;
import oneForm.OneFormEmail_V2.OneformTicket;
import td.api.Logging.History;

public class PathwayTicket extends DepartmentTicket {
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

    private final int ONEFORM_TAG_ID = 10333;

    public PathwayTicket(History history, OneformTicket oneformTicket) {
        super(history, oneformTicket);
        switch (oneformTicket.getCustomAttribute(ONEFORM_TAG_ID)) {
            case
        }

    }

    @Override
    protected int findTypeId() {
        return 0;
    }

    @Override
    protected int findStatusId() {
        return 0;
    }

    @Override
    protected int findFormId() {
        return 0;
    }

}