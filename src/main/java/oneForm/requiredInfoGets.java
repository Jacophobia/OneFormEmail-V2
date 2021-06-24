package oneForm;

import td.api.*;

import java.util.ArrayList;
import td.api.Logging.*;
import java.util.logging.Level;

import static java.lang.Integer.parseInt;
import td.api.Exceptions.*;


/**
 * All of the getters for information required to create tickets in TD.
 *
 * @author Robby Breidenbaugh
 * @since 12/21/20
 */


public class requiredInfoGets {
    History history;
    public requiredInfoGets(History history) {
        this.history = history;
    }

    /**
     * getFormID():
     * Gets the form ID based on the chosen office.
     *
     * @param officeListVal, The chosen office from the oneForm ticket.
     *
     * @return the ID of the form to be used.
     */
    public int getFormID(int officeListVal) {
        // history.addEvent(new LoggingEvent("", "getFormID", requiredInfoGets.class, Level.INFO));
        switch (officeListVal) {
            case IDs.ACADEMIC_OFFICE_LIST_VAL:
                return IDs.ACADEMIC_FORM_ID;
            case IDs.ACCOUNTING_OFFICE_LIST_VAL:
                return IDs.ACCOUNTING_FORM_ID;
            case IDs.ADMISSIONS_OFFICE_LIST_VAL:
                return IDs.ADMISSIONS_FORM_ID;
            case IDs.ADVISING_OFFICE_LIST_VAL:
                return IDs.ADVISING_FORM_ID;
            case IDs.FACILITIES_OFFICE_LIST_VAL:
                return IDs.FACILITIES_FORM_ID;
            case IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                return IDs.FINANCIAL_AID_FORM_ID;
            case IDs.GENERAL_OFFICE_LIST_VAL:
                return IDs.GENERAL_FORM_ID;
            case IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                return IDs.HEALTH_CENTER_FORM_ID;
            case IDs.IT_OFFICE_LIST_VAL:
                return IDs.IT_FORM_ID;
            case IDs.SRR_OFFICE_LIST_VAL:
                return IDs.SRR_FORM_ID;
            case IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                return IDs.UNIVERSITY_STORE_FORM_ID;
        }
        return 0;
    }


    /**
     * getTypeID:
     * Gets the type ID based on the chosen office.
     *
     * @param officeListVal, The chosen office from the oneForm ticket.
     *
     * @return the type ID of the ticket
     */
    public int getTypeID(int officeListVal) {
        // history.addEvent(new LoggingEvent("", "getTypeID", requiredInfoGets.class, Level.INFO));
        switch (officeListVal) {
            case IDs.ACADEMIC_OFFICE_LIST_VAL:
                return IDs.ACADEMIC_TYPE_ID;
            case IDs.ACCOUNTING_OFFICE_LIST_VAL:
                return IDs.ACCOUNTING_TYPE_ID;
            case IDs.ADMISSIONS_OFFICE_LIST_VAL:
                return IDs.ADMISSIONS_TYPE_ID;
            case IDs.ADVISING_OFFICE_LIST_VAL:
                return IDs.ADVISING_TYPE_ID;
            case IDs.FACILITIES_OFFICE_LIST_VAL:
                return IDs.FACILITIES_TYPE_ID;
            case IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                return IDs.FINANCIAL_AID_TYPE_ID;
            case IDs.GENERAL_OFFICE_LIST_VAL:
                return IDs.GENERAL_TYPE_ID;
            case IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                return IDs.HEALTH_CENTER_TYPE_ID;
            case IDs.IT_OFFICE_LIST_VAL:
                return IDs.IT_TYPE_ID;
            case IDs.SRR_OFFICE_LIST_VAL:
                return IDs.SRR_TYPE_ID;
            case IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                return IDs.UNIVERSITY_STORE_TYPE_ID;
        }

        return 0;
    }

    /**
     * getTitle:
     * Get the correct title based off of the office.
     *
     * @param officeListVal, The chosen office from the oneForm ticket.
     *
     * @return the correct title for the department ticket
     */

    //USE DEFAULT TITLES FOR FORMS UNLESS KARL SAYS OTHERWISE
    String getTitle(int officeListVal) {
        // history.addEvent(new LoggingEvent("", "getTitle", requiredInfoGets.class, Level.INFO));
        switch (officeListVal) {
            case IDs.ACADEMIC_OFFICE_LIST_VAL:
                return IDs.ACADEMIC_TITLE;
            case IDs.ACCOUNTING_OFFICE_LIST_VAL:
                return IDs.ACCOUNTING_TITLE;
            case IDs.ADMISSIONS_OFFICE_LIST_VAL:
                return IDs.ADMISSIONS_TITLE;
            case IDs.ADVISING_OFFICE_LIST_VAL:
                return IDs.ADVISING_TITLE;
            case IDs.FACILITIES_OFFICE_LIST_VAL:
                return IDs.FACILITIES_TITLE;
            case IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                return IDs.FINANCIAL_AID_TITLE;
            case IDs.GENERAL_OFFICE_LIST_VAL:
                return IDs.GENERAL_TITLE;
            case IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                return IDs.HEALTH_CENTER_TITLE;
            case IDs.IT_OFFICE_LIST_VAL:
                return IDs.IT_TITLE;
            case IDs.SRR_OFFICE_LIST_VAL:
                return IDs.SRR_TITLE;
            case IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                return IDs.UNIVERSITY_STORE_TITLE;
        }
        return "";
    }


    /**
     * getAccountID:
     * Here we get the account Id of the oneform and return it.
     *
     * @param oneFormTicket, The oneForm ticket that we will be referencing.
     *
     * @return the AccountID of the ticket (For now same as oneform)
     */
    //SAME AS ONE FORM ID
    public int getAccountID(Ticket oneFormTicket) {
        // history.addEvent(new LoggingEvent("", "getAccountID", requiredInfoGets.class, Level.INFO));
        int accountID = 0;
        accountID = oneFormTicket.getAccountId();
        return accountID;
    }

    /**
     * getStatusID:
     * Here we loop through the attributes of the one form to find the action taken.
     *  then we use the action taken to find and return the correct status ID
     *
     * @param attributes, The list of attributes from the oneform.
     *
     * @return the status ID for the department ticket.
     */
    public int getStatusID(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getStatusID", requiredInfoGets.class, Level.INFO));
        String action;
        for (int i = 0; i < attributes.size(); i++) {

            if (attributes.get(i).getId() == IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {

                switch (parseInt(attributes.get(i).getValue())) {
                    case oneForm.IDs.ACADEMIC_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.ACADEMIC_STATUS_NEW;
                                } else
                                    return IDs.ACADEMIC_STATUS_CLOSED;
                            }
                        }

                    case IDs.ACCOUNTING_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.ACCOUNTING_STATUS_NEW;
                                } else
                                    return IDs.ACCOUNTING_STATUS_CLOSED;
                            }
                        }
                    case IDs.ADMISSIONS_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.ADMISSIONS_STATUS_NEW;
                                } else
                                    return IDs.ADMISSIONS_STATUS_CLOSED;
                            }
                        }

                    case IDs.ADVISING_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.ADVISING_STATUS_NEW;
                                } else
                                    return IDs.ADVISING_STATUS_CLOSED;

                            }
                        }


                    case IDs.FACILITIES_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.FACILITIES_STATUS_NEW;
                                } else
                                    return IDs.FACILITIES_STATUS_CLOSED;

                            }
                        }

                    case IDs.FINANCIAL_AID_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.FINANCIAL_AID_STATUS_NEW;
                                } else
                                    return IDs.FINANCIAL_AID_STATUS_CLOSED;

                            }
                        }

                    case IDs.GENERAL_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.GENERAL_STATUS_NEW;
                                } else
                                    return IDs.GENERAL_STATUS_CLOSED;

                            }
                        }


                    case IDs.HEALTH_CENTER_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.HEALTH_CENTER_STATUS_NEW;
                                } else
                                    return IDs.HEALTH_CENTER_STATUS_CLOSED;
                            }
                        }

                    case IDs.IT_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.IT_STATUS_NEW;
                                } else
                                    return IDs.IT_STATUS_CLOSED;

                            }
                        }

                    case IDs.SRR_OFFICE_LIST_VAL:
                        // SRR Tickets should always be closed
                        return IDs.SRR_STATUS_CLOSED;

                    case IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:

                        for (int j = 0; j < attributes.size(); j++) {
                            if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                                action = attributes.get(j).getValue();
                                if (action.equals(IDs.EMAIL_ACTIONS_CHOICE_ESCALATE) || action.equals(IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING)) {
                                    return IDs.UNIVERSITY_STORE_STATUS_NEW;
                                } else
                                    return IDs.UNIVERSITY_STORE_STATUS_CLOSED;

                            }
                        }
                }
            }
        }
        return 0;
    }


    /**
     * getPriorityID:
     * Will always return a priority of 3.
     *
     *
     *
     * @return the ID or priority 3
     */
    //WILL ALWAYS BE 3
    public int getPriorityID() {
        return IDs.PRIORITY_3_ID;
    }



    /**
     * getRequestorID:
     * Gets the requestor UID and returns it.
     *
     * There is a check at the beginning, if the appID is BYUI tickets ID and the type ID is health center
     *  then the requestor must be communityMemberUnknown, unless he/she is a type that is listed in the
     *  herpa list. (See IDs.java for herpa list)
     *
     * Commented out portion was experimenting around with the new contact creation, which has been decided to
     *  not be implemented at this time.
     *
     * @param attributes, The attributes of the one form ticket
     * @param appID, The appID of the issue/dept ticket
     * @param typeID, The typeID of the dept ticket
     *
     * @return the UID of the requestor.
     */
    public String getRequestorUID(Ticket oneFormTicket, ArrayList<CustomAttribute> attributes, int appID, int typeID) throws TDException {
        // history.addEvent(new LoggingEvent("", "getRequestorUID", requiredInfoGets.class, Level.INFO));
        // TeamDynamix api = new TeamDynamix(System.getenv("TD_API_BASE_URL"), System.getenv("USERNAME"), System.getenv("PASSWORD"));
        String UID;

        //GET THE REQUESTOR UID IF THERE IS ONE
        UID = oneFormTicket.getRequestorUid();
        history.addEvent(new LoggingEvent("UID FROM REQUESTOR ONE FORM: " + UID, "getRequestorUID", requiredInfoGets.class, Level.INFO));



        //IF IT IS A HEALTH CENTER TICKET AND THEY AREN'T A STUDENT
        if (appID == IDs.BYUI_TICKETS_APPLICATION_ID) {
            int check = 0;
            for (int i = 0; i < IDs.herpa.length; i++) {
                if (IDs.herpa[i] == oneFormTicket.getAccountId()) {
                    check = 1;
                }
            }
            if ((check == 0) && (typeID == IDs.HEALTH_CENTER_TYPE_ID)) {
                UID = "d36a035f-50e7-e311-80cc-005056ac5ec6";
                return UID;
            }
        }

        //OTHERWISE CONTINUE AND SEARCH BY THE GIVEN FNAME LNAME AND EMAIL
        /*for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.NEW_CONTACT_ID) {
                if (attributes.get(i).getValue() == IDs.NEW_CONTACT_YES) {
                    String firstName = "";
                    String lastName = "";
                    String email = "";
                    for (int j = 0; j < oneFormTicket.getAttributes().size(); j++) {
                        if (oneFormTicket.getAttributes().get(j).getId() == 10326) {
                            firstName = oneFormTicket.getAttributes().get(j).getName();
                        }
                        if (oneFormTicket.getAttributes().get(j).getId() == 10327) {
                            lastName = oneFormTicket.getAttributes().get(j).getName();
                        }
                        if (oneFormTicket.getAttributes().get(j).getId() == 10328) {
                            email = oneFormTicket.getAttributes().get(j).getName();
                        }
                    }


                    ArrayList<User> userList = api.getUsers(lastName, 1000);

                    for (int l = 0; l < userList.size(); l++) {
                        if ((userList.get(l).getFirstName() == firstName) && (userList.get(l).getPrimaryEmail() == email) && (userList.get(l).getLastName() == lastName)) {
                            UID = userList.get(l).getUID();
                            return UID;
                        }
                    }

                    return "Unknown";
                }

                //ELSE ITS NOT A NEW CONTACT SO JUST RETURN THE REQUESTOR ID
                else {
                    return UID;
                }
            }
        }*/
        return UID;
    }


}
