package oneForm;

import td.api.CustomAttribute;
import td.api.Ticket;
import td.api.Logging.*;
import java.util.logging.Level;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import static java.lang.Integer.parseInt;

/**
 * All of the information that is not required to create a ticket in TD,
 *  but is needed for BYUI reports and departments.
 *
 * @author Robby Breidenbaugh
 * @since 12/21/20
 */

public class nonRequiredInfoGets {

    //These are the IDs for the letters of the initials in the FA form
    Map<String, String> initialIDs = new HashMap<String, String>() {{
        put("A", "12509");
        put("B", "12510");
        put("C", "12511");
        put("D", "12535");
        put("E", "12512");
        put("F", "12513");
        put("G", "12514");
        put("H", "12515");
        put("I", "12516");
        put("J", "12517");
        put("K", "12518");
        put("L", "12519");
        put("M", "12520");
        put("N", "12521");
        put("O", "12522");
        put("P", "12523");
        put("Q", "12524");
        put("R", "12525");
        put("S", "12526");
        put("T", "12527");
        put("U", "12528");
        put("V", "12529");
        put("W", "12530");
        put("X", "12531");
        put("Y", "12533");
        put("Z", "12534");
    }};
    History history;

    public nonRequiredInfoGets(History history) {
        this.history = history;
    }


    /**
     * getLastNameInitial:
     * Gets the first intial of the Requestor's lastname
     *
     * @param oneFormTicket, the oneFormTicket
     * @return the first initial of the requestor's lastname
     */
    public String getLastNameInitialID(History history, Ticket oneFormTicket) {
        // history.addEvent(new LoggingEvent("", "getLastNameInitialID", nonRequiredInfoGets.class, Level.INFO));
        String initial = "";
        String initialID = "";
        String lastName = oneFormTicket.getRequestorLastName();

        if (!oneFormTicket.getRequestorLastName().equals("")) {
            initial = lastName.substring(0, 1);
            initial = initial.toUpperCase();
        }
        initialID = initialIDs.getOrDefault(initial, "0");
        history.addEvent(new LoggingEvent("Last name initial ID obtained", "getLastNameInitialID", nonRequiredInfoGets.class, Level.INFO));
        return initialID;
    }

    /**
     * getOverrideEmailID:
     * Get the ID of the override email choice
     *
     * @param attributes, The attributes of the OneForm Ticket.
     * @return the ID of the override email action.
     */
    public String getOverrideEmailID(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getOverrideEmailID", nonRequiredInfoGets.class, Level.INFO));
        String override = "";
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.ADMISSIONS_OVERIDE_ID_ONEFORM) {
                override = attributes.get(i).getValue();
                break;
            }
        }

        switch (override) {
            case IDs.ADMISSIONS_OVERIDE_YES_ONEFORM:
                return IDs.ADMISSIONS_OVERIDE_YES;
            case IDs.ADMISSIONS_OVERIDE_NO_ONEFORM:
                return IDs.ADMISSIONS_OVERIDE_NO;
            case IDs.ADMISSIONS_OVERIDE_DOESNT_NEED_ONEFORM:
                return IDs.ADMISSIONS_OVERIDE_DOESNT_NEED;
        }
        return "0";
    }


    /**
     * getCampusOrOnline:
     * Get the Id of the choice of the campus or online option
     *
     * @param attributes, the attributes of the OneForm Ticket
     * @return The ID of the campus/online attribute as it corresponds to the one form's ID
     */
    public String getCampusOrOnline(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getCampusOrOnline", nonRequiredInfoGets.class, Level.INFO));
        String value = "";
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ID_ONEFORM) {
                value = attributes.get(i).getValue();
                break;
            }
        }

        switch (value) {
            case IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ON_CAMPUS_ID_ONEFORM:
                return IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ON_CAMPUS_ID;
            case IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ONLINE_ID_ONEFORM:
                return IDs.SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ONLINE_ID;
        }
        return "0";
    }


    /**
     * getOnlineformFilled:
     * Get the ID of whether the form was filled or not on the SRR cascading attributes.
     *
     * @param attributes, the attributes of the OneForm.
     * @return the ID of one of the cascading SRR attributes
     */
    public String getOnlineFormFilled(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getOnlineFormFilled", nonRequiredInfoGets.class, Level.INFO));
        String filled = "";
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.SRR_ONLINE_FILLED_ATTRIBUTE_ID_ONEFORM) {
                filled = attributes.get(i).getValue();
                break;
            }
        }

        switch (filled) {
            case IDs.SRR_ONLINE_FILLED_ATTRIBUTE_YES_ID_ONEFORM:
                return IDs.SRR_ONLINE_FILLED_ATTRIBUTE_YES_ID;
            case IDs.SRR_ONLINE_FILLED_ATTRIBUTE_NO_ID_ONEFORM:
                return IDs.SRR_ONLINE_FILLED_ATTRIBUTE_NO_ID;
        }
        return "0";
    }


    /**
     * getChoiceTextString:
     * get the text of the chosen tag on the oneform.
     *
     * @param attributes, The attributes of the oneform ticket.
     * @return the text of the chosen tag on the oneform ticket
     */
    public String getChoiceTextString(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getChoiceTextString", nonRequiredInfoGets.class, Level.INFO));
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                return attributes.get(i).getChoicesText();
            }
        }
        return "Did Not Find";
    }

    /**
     * getChoiceTextID:
     * get the ID of the Dept Ticket one form Tag text attribute
     *
     * @param appID, The appID of the Dept Ticket
     * @return the ID of the chosen tag on the oneform ticket
     */
    public int getChoiceTextID(int appID) {
        // history.addEvent(new LoggingEvent("", "getChoiceTextID", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_ONE_FORM_TAG_NAME_ID;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.BYUI_TICKETS_ONE_FORM_TAG_NAME_ID;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_ONE_FORM_TAG_NAME_ID;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_ONE_FORM_TAG_NAME_ID;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_ONE_FORM_TAG_NAME_ID;
            case IDs.ADVISING_APPLICATION_ID:
                return IDs.ADVISING_ONE_FORM_TAG_NAME_ID;
        }
        return 0;
    }


    /**
     * getChoiceTextID:
     * get the ID of the Dept Ticket one form Tag text attribute
     *
     * @param appID, The appID of the Dept Ticket
     * @return the ID of the chosen tag on the oneform ticket
     */
    public int getAgentNameAttrID(int appID) {
        // history.addEvent(new LoggingEvent("", "getAgentNameAttrID", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_BSC_AGENT_NAME;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.BYUI_TICKETS_BSC_AGENT_NAME;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_BSC_AGENT_NAME;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_BSC_AGENT_NAME;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_BSC_AGENT_NAME;
            case IDs.ADVISING_APPLICATION_ID:
                return IDs.ADVISING_BSC_AGENT_NAME;
        }
        return 0;
    }

    /**
     * getSentToLevel2ID:
     * Get the ID of the Sent to Level 2 attribute
     *
     * @param appID, The appID of the Dept Ticket
     * @return the ID of the Sent to Level 2 attribute
     */
    public int getSentToLevel2ID(int appID) {
        // history.addEvent(new LoggingEvent("", "getSentToLevel2ID", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_SENT_TO_LEVEL_2;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.GENERAL_SENT_TO_LEVEL_2;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_SENT_TO_LEVEL_2;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_SENT_TO_LEVEL_2;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_SENT_TO_LEVEL_2;
            case IDs.ADVISING_APPLICATION_ID:
                return IDs.ADVISING_SENT_TO_LEVEL_2;

        }
        return 0;
    }

    /**
     * getMismatchID:
     * Get the ID of the Mismatch attribute
     *
     * @param appID, The appID of the department ticket.
     * @return the ID of the Mismatch attribute
     */
    int getMismatchID(int appID) {
        // history.addEvent(new LoggingEvent("", "getMismatchID", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_TAG_MISMATCH;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.BYUI_TICKETS_TAG_MISMATCH;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_TAG_MISMATCH;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_TAG_MISMATCH;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_TAG_MISMATCH;

        }
        return 0;
    }

    /**
     * getTicketFeedUpdateAttr:
     * Get the ID of the Mismatch attribute
     *
     * @param appID, The appID of the department ticket.
     * @return the ID of the Mismatch attribute
     */
    public int getTicketFeedUpdateAttr(int appID) {
        // history.addEvent(new LoggingEvent("", "getTicketFeedUpdateAttr", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_FEED_UPDATER_ATTR;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.BYUI_TICKETS_FEED_UPDATER_ATTR;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_FEED_UPDATER_ATTR;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_FEED_UPDATER_ATTR;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_FEED_UPDATER_ATTR;
            case IDs.ADVISING_APPLICATION_ID:
                return IDs.ADVISING_FEED_UPDATER_ATTR;

        }
        return 0;
    }

    /**
     * getOneFormTicketIDID:
     * Get the ID of the "oneForm Ticket ID" attribute
     *
     * @param appID, The appID of the department ticket.
     * @return the ID of the oneForm Ticket ID attribute
     */
    public int getOneFormTicketIDID(int appID) {
        // history.addEvent(new LoggingEvent("", "getOneFormTicketIDID", nonRequiredInfoGets.class, Level.INFO));
        switch (appID) {
            case IDs.ACCOUNTING_APPLICATION_ID:
                return IDs.ACCOUNTING_ONE_FORM_TICKETID_TAG_ID;
            case IDs.BYUI_TICKETS_APPLICATION_ID:
                return IDs.BYUI_TICKETS_ONE_FORM_TICKETID_TAG_ID;
            case IDs.ADMISSIONS_APPLICATION_ID:
                return IDs.ADMISSIONS_ONE_FORM_TICKETID_TAG_ID;
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                return IDs.FINANCIAL_AID_ONE_FORM_TICKETID_TAG_ID;
            case IDs.SRR_APPLICATION_ID:
                return IDs.SRR_ONE_FORM_TICKETID_TAG_ID;
            case IDs.ADVISING_APPLICATION_ID:
                return IDs.ADVISING_ONE_FORM_TICKETID_TAG_ID;

        }
        return 0;
    }

    /**
     * getAndonCordNotes:
     * Get the string of Andon Cord notes from the oneForm.
     *
     * @param attributes, The attributes of the oneForm ticket.
     * @return a string of the andon cord notes on the oneForm ticket.
     */
    public String getAndonCordNotes(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getAndonCordNotes", nonRequiredInfoGets.class, Level.INFO));
        String notes = "";

        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.ONE_FORM_ANDON_CORD_NOTES_ID) {
                notes = attributes.get(i).getValueText();
                break;
            }
        }

        return notes;
    }



    /**
     * getTagID:
     * Get the corresponding tag attribute ID from the oneForm.
     *
     * @param attributes, The attributes of the oneForm ticket.
     *
     * @return The ID of the Tag attribute for the department ticket.
     */
    int getTagID(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getTagID", nonRequiredInfoGets.class, Level.INFO));

        int officeListVal = 0;
        for (int i = 0; i < attributes.size(); i++) {

            if (attributes.get(i).getId() == IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {

                officeListVal = parseInt(attributes.get(i).getValue());

            }


            switch (officeListVal) {
                case IDs.ACADEMIC_OFFICE_LIST_VAL:
                    return IDs.ACADEMIC_TAG_ID;
                case IDs.ACCOUNTING_OFFICE_LIST_VAL:
                    return IDs.ACCOUNTING_TAG_ID;
                case IDs.ADMISSIONS_OFFICE_LIST_VAL:
                    return IDs.ADMISSIONS_TAG_ID;
                case IDs.ADVISING_OFFICE_LIST_VAL:
                    return IDs.ADVISING_TAG_ID;
                case IDs.FACILITIES_OFFICE_LIST_VAL:
                    return IDs.FACILITIES_TAG_ID;
                case IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                    return IDs.FINANCIAL_AID_TAG_ID;
                case IDs.GENERAL_OFFICE_LIST_VAL:
                    return IDs.GENERAL_TAG_ID;
                case IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                    return IDs.HEALTH_CENTER_TAG_ID;
                case IDs.IT_OFFICE_LIST_VAL:
                    return IDs.IT_TAG_ID;
                case IDs.SRR_OFFICE_LIST_VAL:
                    return IDs.SRR_TAG_ID;
                case IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                    return IDs.UNIVERSITY_STORE_TAG_ID;


            }
            return 0;
        }
        return 0;
    }


    /**
     * getTagName:
     * Get the text of the tag from the oneForm
     *
     * @param attributes, The attributes of the oneForm ticket.
     *
     * @return a string of the tag ID choice for the department ticket.
     */
    public String getTagName(ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getTagName", nonRequiredInfoGets.class, Level.INFO));
        String tag = "0";
        String oneFormID = "";

        IDs IDs = new IDs();
        tagMaps map = new tagMaps();

        /*for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                oneFormID = attributes.get(i).getValue();
            }
        }*/

        for (int j = 0; j < attributes.size(); j++) {

            if (attributes.get(j).getId() == IDs.OFFICE_LIST_1_ATTRIBUTE_ID) {

                switch (parseInt(attributes.get(j).getValue())) {
                    case oneForm.IDs.ACADEMIC_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.academicMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.ACCOUNTING_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.accountingMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.ADMISSIONS_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.admissionsMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.ADVISING_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.advisingOnlineMap.getOrDefault(oneFormID, "0");
                                if (tag.equals("0")) {
                                    tag = map.advisingCampusMap.getOrDefault(oneFormID, "0");
                                }
                                return tag;
                            }

                        }
                    case oneForm.IDs.FACILITIES_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.facilitiesMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.FINANCIAL_AID_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.financialAidMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.GENERAL_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.generalMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.HEALTH_CENTER_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.healthCenterMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.IT_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.itMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.SRR_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.srrMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }
                    case oneForm.IDs.UNIVERSITY_STORE_OFFICE_LIST_VAL:
                        for (int i = 0; i < attributes.size(); i++) {
                            if (attributes.get(i).getId() == IDs.ONE_FORM_TAG_ID) {
                                oneFormID = attributes.get(i).getValue();
                                tag = map.universityStoreMap.getOrDefault(oneFormID, "0");
                                return tag;
                            }

                        }


                }
            }

            //tag = map.accountingMap.getOrDefault(oneFormID,"0");
            //System.out.println("TAGID: " + tag);
            return tag;
        }
        return tag;
    }


    /**
     * getSentToLevel2Value:
     * Gets the ID of the value of the sent to level two attribute choice.
     *
     * @param attributes, The attributes of the oneForm ticket.
     * @param appID, the application ID of the department ticket
     *
     * @return a ID of the value of the sent to level two attribute choice.
     */
    public String getSentToLevel2Value(int appID, ArrayList<CustomAttribute> attributes) {
        // history.addEvent(new LoggingEvent("", "getSentToLevel2Value", nonRequiredInfoGets.class, Level.INFO));
        String action = "";
        switch (appID) {


            case IDs.ACCOUNTING_APPLICATION_ID:
                for (int j = 0; j < attributes.size(); j++) {

                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.ACCOUNTING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.ACCOUNTING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.ACCOUNTING_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.ACCOUNTING_SENT_TO_LEVEL_2_NO;
                        }
                    }
                }
            case IDs.ADMISSIONS_APPLICATION_ID:
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.ADMISSIONS_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.ADMISSIONS_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.ADMISSIONS_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.ADMISSIONS_SENT_TO_LEVEL_2_NO;

                        }
                    }
                }
            case IDs.FINANCIAL_AID_APPLICATION_ID:
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.FINANCIAL_AID_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.FINANCIAL_AID_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.FINANCIAL_AID_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.FINANCIAL_AID_SENT_TO_LEVEL_2_NO;
                        }
                    }
                }

                case IDs.SRR_APPLICATION_ID:
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.SRR_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.SRR_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.SRR_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.SRR_SENT_TO_LEVEL_2_NO;
                        }
                    }
                }
            case IDs.ADVISING_APPLICATION_ID: {
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_NO;
                        }
                    }
                }
            }

            case IDs.BYUI_TICKETS_APPLICATION_ID:
            {
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.ACADEMIC_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.ACADEMIC_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.ACADEMIC_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.SRR_SENT_TO_LEVEL_2_NO;
                        }
                    }
                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.ADVISING_SENT_TO_LEVEL_2_NO;
                        }
                    }

                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.FACILITIES_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.FACILITIES_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.FACILITIES_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.FACILITIES_SENT_TO_LEVEL_2_NO;
                        }
                    }

                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.GENERAL_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.GENERAL_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.GENERAL_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.GENERAL_SENT_TO_LEVEL_2_NO;
                        }
                    }

                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.HEALTH_CENTER_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.HEALTH_CENTER_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.HEALTH_CENTER_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.SRR_SENT_TO_LEVEL_2_NO;
                        }
                    }

                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.IT_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.IT_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.IT_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.IT_SENT_TO_LEVEL_2_NO;
                        }
                    }
                    else if (attributes.get(j).getId() == IDs.EMAIL_ACTIONS_ATTR) {
                        action = attributes.get(j).getValue();
                        switch (action) {
                            case IDs.EMAIL_ACTIONS_CHOICE_ESCALATE:
                                return IDs.UNIVERSITY_STORE_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING:
                                return IDs.UNIVERSITY_STORE_SENT_TO_LEVEL_2_YES;
                            case IDs.EMAIL_ACTIONS_CHOICE_RESOVLED_KB:
                                return IDs.UNIVERSITY_STORE_SENT_TO_LEVEL_2_NO;
                            case IDs.EMAIL_ACTIONS_CHOICE_SCHEDULE:
                                return IDs.SRR_SENT_TO_LEVEL_2_NO;
                        }
                    }
                }
            }
        }


        return "0";
    }

}
