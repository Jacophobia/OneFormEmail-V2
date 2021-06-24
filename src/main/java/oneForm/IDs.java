package oneForm;

import java.util.ArrayList;
/**
 * IDs that are required by the oneForm application.
 *
 * @author Robby Breidenbaugh
 * @since 12/21/20
 */

public class IDs {



    /**********************PRIORITY IDs***********************************************/
    public static final int PRIORITY_3_ID = 21;

    /*****************************************************************************/

    /**********************ATTRIBUTESID***********************************************/
    public static final int ONE_FORM_TAG_ID = 10333;
    public static final int ONE_FORM_ANDON_CORD_NOTES_ID = 10379;

    /********************************************************************************/

    /***********************OFFICE LIST 1 VALUES**********************************/
    // SOURCE -> https://td.byui.edu/TDAdmin/fcb76cab-d63e-41d6-a1f0-34c67299d6bf/48/Attributes/AttributeEdit.aspx?CMPID=9&ATTID=10329

    //The ID of the office radio button
    public static final int OFFICE_LIST_1_ATTRIBUTE_ID = 10329;


    public static final int ACADEMIC_OFFICE_LIST_VAL = 31724;
    public static final int ACCOUNTING_OFFICE_LIST_VAL = 31723;
    public static final int ADMISSIONS_OFFICE_LIST_VAL = 31725;
    public static final int ADVISING_OFFICE_LIST_VAL = 31727;
    public static final int FACILITIES_OFFICE_LIST_VAL = 31728;
    public static final int FINANCIAL_AID_OFFICE_LIST_VAL = 31729;
    public static final int GENERAL_OFFICE_LIST_VAL = 31730;
    public static final int HEALTH_CENTER_OFFICE_LIST_VAL = 31731;
    public static final int IT_OFFICE_LIST_VAL = 31732;
    public static final int SRR_OFFICE_LIST_VAL = 31733;
    public static final int UNIVERSITY_STORE_OFFICE_LIST_VAL = 31734;
    public static final int PATHWAY_OFFICE_LIST_VAL = 36695;

    /****************************************************************************/

    /**********************APP IDs***********************************************/
    public static final int ACCOUNTING_APPLICATION_ID = 54;
    public static final int BYUI_TICKETS_APPLICATION_ID = 40;
    public static final int ADMISSIONS_APPLICATION_ID = 52;
    public static final int FINANCIAL_AID_APPLICATION_ID = 53;
    public static final int SRR_APPLICATION_ID = 55;
    public static final int ONE_FORM_APPLICATION_ID = 48;
    public static final int BSC_OPERATIONS_APPLICATION_ID = 42;
    public static final int ADVISING_APPLICATION_ID = 56;
    /*****************************************************************************/

    /************FORM IDs*************************************/
    public static final int ACADEMIC_FORM_ID = 1969;//
    public static final int ACCOUNTING_FORM_ID = 1691;//
    public static final int ADMISSIONS_FORM_ID = 1970;//
    public static final int ADVISING_FORM_ID = 3355;
    public static final int FACILITIES_FORM_ID = 1969;//
    public static final int FINANCIAL_AID_FORM_ID = 1681;//
    public static final int GENERAL_FORM_ID = 1969;//
    public static final int HEALTH_CENTER_FORM_ID = 1971;//
    public static final int IT_FORM_ID = 2425;//
    public static final int SRR_FORM_ID = 1975;//
    public static final int UNIVERSITY_STORE_FORM_ID = 2452;//
    public static final int ONE_FORM_EMAIL_FORM_ID = 3332;
    /*********************************************************/

    /***********TYPE IDs**************************************/
    public static final int ACADEMIC_TYPE_ID = 369;
    public static final int ACCOUNTING_TYPE_ID = 568;
    public static final int ADMISSIONS_TYPE_ID = 654;
    public static final int ADVISING_TYPE_ID = 880;
    public static final int FACILITIES_TYPE_ID = 369;
    public static final int FINANCIAL_AID_TYPE_ID = 554;
    public static final int GENERAL_TYPE_ID = 369;
    public static final int HEALTH_CENTER_TYPE_ID = 370;
    public static final int IT_TYPE_ID = 702;
    public static final int SRR_TYPE_ID = 666;
    public static final int UNIVERSITY_STORE_TYPE_ID = 373;
    /*********************************************************/

    /*****************FORM TITLES*****************************/
    public static final String ACADEMIC_TITLE = "BSC General Email";
    public static final String ACCOUNTING_TITLE = "BSC Accounting Email";
    public static final String ADMISSIONS_TITLE = "BSC Admissions Email";
    public static final String ADVISING_TITLE = "BSC Advising Email";
    public static final String FACILITIES_TITLE = "BSC General Email";
    public static final String FINANCIAL_AID_TITLE = "BSC Financial Aid Email";
    public static final String GENERAL_TITLE = "BSC General Email";
    public static final String HEALTH_CENTER_TITLE = "BSC Health Center Email";
    public static final String IT_TITLE = "BSC IT Email";
    public static final String SRR_TITLE = "BSC SRR Email";
    public static final String UNIVERSITY_STORE_TITLE = "BSC University Store Phone Email";
    /*********************************************************/




    /***********STATUS CLOSED**************************************/
    public static final int ACADEMIC_STATUS_CLOSED = 200;
    public static final int ACCOUNTING_STATUS_CLOSED = 417;
    public static final int ADMISSIONS_STATUS_CLOSED = 403;
    public static final int ADVISING_STATUS_CLOSED = 431;
    public static final int FACILITIES_STATUS_CLOSED = 200;
    public static final int FINANCIAL_AID_STATUS_CLOSED = 410;
    public static final int GENERAL_STATUS_CLOSED = 200;
    public static final int HEALTH_CENTER_STATUS_CLOSED = 200;
    public static final int IT_STATUS_CLOSED = 200;
    public static final int SRR_STATUS_CLOSED = 424;
    public static final int UNIVERSITY_STORE_STATUS_CLOSED = 200;
    public static final int ONE_FORM_CLOSED = 326;
    /*********************************************************/

    /***********STATUS NEW**************************************/
    public static final int ACADEMIC_STATUS_NEW = 196;
    public static final int ACCOUNTING_STATUS_NEW = 413;
    public static final int ADMISSIONS_STATUS_NEW = 399;
    public static final int ADVISING_STATUS_NEW = 427;
    public static final int FACILITIES_STATUS_NEW = 196;
    public static final int FINANCIAL_AID_STATUS_NEW = 406;
    public static final int GENERAL_STATUS_NEW = 196;
    public static final int HEALTH_CENTER_STATUS_NEW = 196;
    public static final int IT_STATUS_NEW = 196;
    public static final int SRR_STATUS_NEW = 420;
    public static final int UNIVERSITY_STORE_STATUS_NEW = 196;
    public static final int ONE_FORM_NEW = 323;
    /*********************************************************/

    /***********ACTIONS_ATTRIBUTE**************************************/
    public static final int EMAIL_ACTIONS_ATTR = 11398;
    /*********************************************************/

    /***********ACTIONS_ATTRIBUTE**************************************/
    public static final String EMAIL_ACTIONS_CHOICE_ESCALATE = "35388";
    public static final String EMAIL_ACTIONS_CHOICE_RESOLVE_BY_CALLING = "35489";
    public static final String EMAIL_ACTIONS_CHOICE_RESOVLED_KB = "35389";
    public static final String EMAIL_ACTIONS_CHOICE_SCHEDULE = "35490";
    /*********************************************************/

    /***********ACTIONS_CHOICES ONE FORM**************************************/
    public static final String ACADEMIC_ACTIONS_CHOICES_CALL = "33456";
    public static final String ACADEMIC_ACTIONS_CHOICES_RESOLVED = "33457";
    public static final String ACADEMIC_ACTIONS_CHOICES_CALL_BACK = "33458";


    public static final String ACCOUNTING_ACTIONS_CHOICES_ESCALATE = "33477";
    public static final String ACCOUNTING_ACTIONS_CHOICES_PHONE_CALL = "33474";
    public static final String ACCOUNTING_ACTIONS_CHOICES_CALLING_ACCOUNTING = "33909";
    public static final String ACCOUNTING_ACTIONS_CHOICES_RESOLVED = "33475";
    public static final String ACCOUNTING_ACTIONS_CHOICES_CALL_BACK = "33476";


    public static final String ADMISSIONS_ACTIONS_CHOICES_CALLBACK_STUDENT = "33902";
    public static final String ADMISSIONS_ACTIONS_CHOICES_CALLBACK_OFFICE = "33901";
    public static final String ADMISSIONS_ACTIONS_CHOICES_ESCALATE = "33478";
    public static final String ADMISSIONS_ACTIONS_CHOICES_ESCALATE_PHONE = "33479";
    public static final String ADMISSIONS_ACTIONS_CHOICES_RESOLVED = "33480";


    public static final String ADVISING_CAMPUS_ACTIONS_CHOICES_RESOLVED = "33915";
    public static final String ADVISING_CAMPUS_ACTIONS_CHOICES_SCHEDULE = "33916";
    public static final String ADVISING_CAMPUS_ACTIONS_CHOICES_SEND = "33917";
    public static final String ADVISING_CAMPUS_ACTIONS_CHOICES_TRANSFER = "33914";


    public static final String ADVISING_ONLINE_ACTIONS_CHOICES_REFER = "33485";
    public static final String ADVISING_ONLINE_ACTIONS_CHOICES_SCHEDULE = "33484";
    public static final String ADVISING_ONLINE_ACTIONS_CHOICES_RESOLVED = "33987";


    public static final String FACILITIES_ACTIONS_CHOICES_ESCALATE = "33481";
    public static final String FACILITIES_ACTIONS_CHOICES_CONTACT_INFO = "33919";
    public static final String FACILITIES_ACTIONS_CHOICES_RESOVLED = "33482";
    public static final String FACILITIES_ACTIONS_CHOICES_CALLBACK = "33483";


    public static final String FINANCIAL_AID_ACTIONS_CHOICES_CREATED = "33898";
    public static final String FINANCIAL_AID_ACTIONS_CHOICES_ESCALATE = "33497";
    public static final String FINANCIAL_AID_ACTIONS_CHOICES_REFER = "33899";
    public static final String FINANCIAL_AID_ACTIONS_CHOICES_RESOLVED_KB = "33495";
    public static final String FINANCIAL_AID_ACTIONS_CHOICES_RESOLVED_EMAIL = "33900";


    public static final String GENERAL_ACTIONS_CHOICES_ESCALATE = "33498";
    public static final String GENERAL_ACTIONS_CHOICES_CONTACT_INFO = "33921";
    public static final String GENERAL_ACTIONS_CHOICES_RESOLVED = "33499";
    public static final String GENERAL_ACTIONS_CHOICES_CALLBACK = "33500";


    public static final String HEALTH_CENTER_ACTIONS_CHOICES_ESCALATED = "33502";
    public static final String HEALTH_CENTER_ACTIONS_CHOICES_RESOLVED = "33503";
    public static final String HEALTH_CENTER_ACTIONS_CHOICES_CALLBACK = "33504";


    public static final String IT_ACTIONS_CHOICES_AFTER_HOURS_FORM = "33923";
    public static final String IT_ACTIONS_CHOICES_ESCALATE = "33505";
    public static final String IT_ACTIONS_CHOICES_RESOLVED = "33506";
    public static final String IT_ACTIONS_CHOICES_SEND_EMAIL = "33507";


    public static final String SRR_ACTIONS_CHOICES_RESOLVED = "33509";
    public static final String SRR_ACTIONS_CHOICES_TRANSFER = "33508";
    public static final String SRR_ACTIONS_CHOICES_EMAIL = "33510";


    public static final String UNIVERSITY_STORE_ACTIONS_CHOICES_ESCALATED = "32083";
    public static final String UNIVERSITY_STORE_ACTIONS_CHOICES_RESOLVED = "32084";
    public static final String UNIVERSITY_STORE_ACTIONS_CHOICES_CALLBACK = "32085";


    /*********************************************************/



    /***************SENT TO LEVEL 2 **************************/

    public static final int ACADEMIC_SENT_TO_LEVEL_2 = 2281;
    public static final String ACADEMIC_SENT_TO_LEVEL_2_YES = "5919";
    public static final String ACADEMIC_SENT_TO_LEVEL_2_NO = "5920";

    public static final int ACCOUNTING_SENT_TO_LEVEL_2 = 3290;
    public static final String ACCOUNTING_SENT_TO_LEVEL_2_YES = "12813";
    public static final String ACCOUNTING_SENT_TO_LEVEL_2_NO = "12814";

    public static final int ADMISSIONS_SENT_TO_LEVEL_2 = 5183;
    public static final String ADMISSIONS_SENT_TO_LEVEL_2_YES = "15672";
    public static final String ADMISSIONS_SENT_TO_LEVEL_2_NO = "15673";

    public static final int ADVISING_SENT_TO_LEVEL_2 = 11600;
    public static final String ADVISING_SENT_TO_LEVEL_2_YES = "36274";
    public static final String ADVISING_SENT_TO_LEVEL_2_NO = "36275";

    public static final int FACILITIES_SENT_TO_LEVEL_2 = 2281;
    public static final String FACILITIES_SENT_TO_LEVEL_2_YES = "5919";
    public static final String FACILITIES_SENT_TO_LEVEL_2_NO = "5920";

    public static final int FINANCIAL_AID_SENT_TO_LEVEL_2 = 3275;
    public static final String FINANCIAL_AID_SENT_TO_LEVEL_2_YES = "12507";
    public static final String FINANCIAL_AID_SENT_TO_LEVEL_2_NO = "12508";

    public static final int GENERAL_SENT_TO_LEVEL_2 = 2281;
    public static final String GENERAL_SENT_TO_LEVEL_2_YES = "5919";
    public static final String GENERAL_SENT_TO_LEVEL_2_NO = "5920";

    public static final int HEALTH_CENTER_SENT_TO_LEVEL_2 = 2281;
    public static final String HEALTH_CENTER_SENT_TO_LEVEL_2_YES = "5919";
    public static final String HEALTH_CENTER_SENT_TO_LEVEL_2_NO = "5920";

    public static final int IT_SENT_TO_LEVEL_2 = 2281;
    public static final String IT_SENT_TO_LEVEL_2_YES = "5919";
    public static final String IT_SENT_TO_LEVEL_2_NO = "5920";

    public static final int SRR_SENT_TO_LEVEL_2 = 5467;
    public static final String SRR_SENT_TO_LEVEL_2_YES = "15872";
    public static final String SRR_SENT_TO_LEVEL_2_NO = "15873";

    public static final int UNIVERSITY_STORE_SENT_TO_LEVEL_2 = 2281;
    public static final String UNIVERSITY_STORE_SENT_TO_LEVEL_2_YES = "5919";
    public static final String UNIVERSITY_STORE_SENT_TO_LEVEL_2_NO = "5920";


    /*********************************************************/

    /*********************TAG IDs********************************/
    public static final int ACADEMIC_TAG_ID = 2304;
    public static final int ACCOUNTING_TAG_ID = 8421;
    public static final int ADMISSIONS_TAG_ID = 5154;
    public static final int ADVISING_TAG_ID = 11605;
    public static final int FACILITIES_TAG_ID = 2304;
    public static final int FINANCIAL_AID_TAG_ID = 7053;
    public static final int GENERAL_TAG_ID = 2304;
    public static final int HEALTH_CENTER_TAG_ID = 2306;
    public static final int IT_TAG_ID = 6520;
    public static final int SRR_TAG_ID = 5505;
    public static final int UNIVERSITY_STORE_TAG_ID = 2313;

    /************************************************************/

    /*****************ADVISING ONLINE ACTION CHOICES*************/

    public static final int ADVISING_ONLINE_ACTION_TAKEN = 10233;
    public static final String ADVISING_ONLINE_ACTION_TAKEN_SCHEDULE = "31578";
    public static final String ADVISING_ONLINE_ACTION_TAKEN_USED_KB = "31577";

    public static final int ADVISING_ONLINE_PEOPLE_TAB = 8079;
    public static final String ADVISING_ONLINE_PEOPLE_TAB_YES = "22361";
    public static final String ADVISING_ONLINE_PEOPLE_TAB_NO = "22362";

    public static final int ADVISING_ONLINE_FILLED = 7834;
    public static final String ADVISING_ONLINE_FILLED_YES = "21752";
    public static final String ADVISING_ONLINE_FILLED_NO = "21753";

    public static final int ADVISING_ONLINE_3_DAYS = 7835;
    public static final String ADVISING_ONLINE_3_DAYS_YES = "21754";
    public static final String ADVISING_ONLINE_3_DAYS_NO = "21755";




    public static final int ADVISING_ONLINE_PEOPLE_TAB_ONEFORM = 10491;
    public static final String ADVISING_ONLINE_PEOPLE_TAB_YES_ONEFORM = "33486";
    public static final String ADVISING_ONLINE_PEOPLE_TAB_NO_ONEFORM = "33487";

    public static final int ADVISING_ONLINE_FILLED_ONEFORM = 10493;
    public static final String ADVISING_ONLINE_FILLED_YES_ONEFORM = "33489";
    public static final String ADVISING_ONLINE_FILLED_NO_ONEFORM = "33490";

    public static final int ADVISING_ONLINE_3_DAYS_ONEFORM = 10495;
    public static final String ADVISING_ONLINE_3_DAYS_YES_ONEFORM = "33491";
    public static final String ADVISING_ONLINE_3_DAYS_NO_ONEFORM = "33492";


    /************************************************************/


/************************LAST NAME INTIAL?*************/
    public static final int LAST_NAME_INITIAL = 3276;
/************************************************************/


/********************NEW CONTACT CHECKBOX***************************/

    public static final int NEW_CONTACT_ID = 10325;
    public static final String NEW_CONTACT_YES = "31722";

/*******************************************************/

/******************************LIST OF HERPA*******************/
    //List of codes for student types
    public static final int [] herpa = {548,564,563,565,537,546,542,547,538,545,540,544,710,727,712,391};

/********************************************************/

/****************TAG MISMATCH****************************/

    public static final int BYUI_TICKETS_TAG_MISMATCH = 10929;
    public static final int ACCOUNTING_TAG_MISMATCH = 10930;
    public static final int ADMISSIONS_TAG_MISMATCH = 10931;
    public static final int FINANCIAL_AID_TAG_MISMATCH = 10932;
    public static final int SRR_TAG_MISMATCH = 10933;

/********************************************************/

/****************BYUI SUPPORT CENTER LOCATION************/

    public static final int LOCATION_ID = 1;

/*******************************************************/

/****************SOURCE ID******************************/

    public static final int SOURCE_ID_EMAIL = 6;

/*******************************************************/

/*********************OVERRIDE IDS**********************/

    public static final int ADMISSIONS_OVERIDE_ID_ONEFORM = 10943;
    public static final String ADMISSIONS_OVERIDE_YES_ONEFORM = "34016";
    public static final String ADMISSIONS_OVERIDE_NO_ONEFORM = "34017";
    public static final String ADMISSIONS_OVERIDE_DOESNT_NEED_ONEFORM = "34018";


    public static final int ADMISSIONS_OVERIDE_ID = 8171;
    public static final String ADMISSIONS_OVERIDE_YES = "22635";
    public static final String ADMISSIONS_OVERIDE_NO = "22636";
    public static final String ADMISSIONS_OVERIDE_DOESNT_NEED = "32854";


/******************************************************/


/*******************ONE FORM TAG NAME******************/

    public static final int ACCOUNTING_ONE_FORM_TAG_NAME_ID = 10944;
    public static final int BYUI_TICKETS_ONE_FORM_TAG_NAME_ID = 10945;
    public static final int ADMISSIONS_ONE_FORM_TAG_NAME_ID = 10946;
    public static final int FINANCIAL_AID_ONE_FORM_TAG_NAME_ID = 10947;
    public static final int SRR_ONE_FORM_TAG_NAME_ID = 10948;
    public static final int ADVISING_ONE_FORM_TAG_NAME_ID = 11602;

/******************************************************/


    /*******************ONE FORM TICKET ID******************/

    public static final int ACCOUNTING_ONE_FORM_TICKETID_TAG_ID = 11013;
    public static final int BYUI_TICKETS_ONE_FORM_TICKETID_TAG_ID = 11015;
    public static final int ADMISSIONS_ONE_FORM_TICKETID_TAG_ID = 11014;
    public static final int FINANCIAL_AID_ONE_FORM_TICKETID_TAG_ID = 11016;
    public static final int SRR_ONE_FORM_TICKETID_TAG_ID = 11017;
    public static final int ADVISING_ONE_FORM_TICKETID_TAG_ID = 11601;

/******************************************************/


    /*******************ANDON CORD IDs******************/

    public static final int BSC_OPERATIONS_ANDON_FORM_ID = 3068;
    public static final int BSC_OPERATIONS_ANDON_FORM_STATUS_NEW = 210;
    public static final int BSC_OPERATIONS_ANDON_FORM_STATUS_CLOSED = 214;
    public static final int BSC_OPERATIONS_TYPE_ID = 632;
    public static final int BSC_OPERATIONS_INCIDENT_ATTRIBUTE_ID = 7901;
    public static final int BSC_OPERATIONS_ANDON_CORD_NOTES_ATTRIBUTE_ID = 9510;

    public static final int BSC_OPERATIONS_OFFICE_ATTRIBUTE_ID = 11042;
    public static final int BSC_OPERATIONS_ONEFORM_ISSUE_ATTRIBUTE_ID = 11041;

    public static final int BSC_OPERATIONS_ANDON_CORD_NEEDED = 10376;
    public static final String BSC_OPERATIONS_ANDON_CORD_NEEDED_YES = "32086";



/******************************************************/


/*********************SRR EXTRA ATTRIBUTES AND ON ONEFORM*************/

    public static final int SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ID = 9829;
    public static final String SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ON_CAMPUS_ID = "30087";
    public static final String SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ONLINE_ID = "30088";

    public static final int SRR_ONLINE_FILLED_ATTRIBUTE_ID = 9851;
    public static final String SRR_ONLINE_FILLED_ATTRIBUTE_YES_ID = "30163";
    public static final String SRR_ONLINE_FILLED_ATTRIBUTE_NO_ID = "30164";

    public static final int SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ID_ONEFORM = 11044;
    public static final String SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ON_CAMPUS_ID_ONEFORM = "34478";
    public static final String SRR_ONLINE_AND_CAMPUS_ATTRIBUTE_ONLINE_ID_ONEFORM = "34479";

    public static final int SRR_ONLINE_FILLED_ATTRIBUTE_ID_ONEFORM = 11045;
    public static final String SRR_ONLINE_FILLED_ATTRIBUTE_YES_ID_ONEFORM = "34480";
    public static final String SRR_ONLINE_FILLED_ATTRIBUTE_NO_ID_ONEFORM = "34481";

/******************************************************/


/*****DEPARTMENT TICKET BSC AGENT NAME ATTRIBUTES*****/

    public static final int ACCOUNTING_BSC_AGENT_NAME = 5514;
    public static final int BYUI_TICKETS_BSC_AGENT_NAME = 5520;
    public static final int ADMISSIONS_BSC_AGENT_NAME = 5871;
    public static final int FINANCIAL_AID_BSC_AGENT_NAME = 5523;
    public static final int SRR_BSC_AGENT_NAME = 5533;
    public static final int ADVISING_BSC_AGENT_NAME = 11603;

/******************************************************/

/******************BSC OPERATIONS COUNT FORM**********************/

    public static final int OPERATIONS_COUNT_DATA_DATE = 3057;
    public static final int OPERATIONS_COUNT_FORM_ID = 3343;
    public static final int OPERATIONS_COUNT_EMAIL_VOLUME = 11509;
    public static final int OPERATIONS_COUNT_SPAM_VOLUME = 11510;
    public static final int OPERATIONS_COUNT_REPLY_VOLUME = 11511;
    public static final int OPERATIONS_COUNT_EXCALATED_VOLUME = 11361;

/****************TICKET FEED UPDATER ATTRIBUTE********************/
    public static final int BYUI_TICKETS_FEED_UPDATER_ATTR = 11514;
    public static final int ACCOUNTING_FEED_UPDATER_ATTR = 11515;
    public static final int ADMISSIONS_FEED_UPDATER_ATTR = 11516;
    public static final int FINANCIAL_AID_FEED_UPDATER_ATTR = 11517;
    public static final int SRR_FEED_UPDATER_ATTR = 11518;
    public static final int ADVISING_FEED_UPDATER_ATTR = 11647;


/****************************************************************/

/*********************PATHWAY TAGS*************************************/
    public static final int BSC_TAG_1_ADVISING = 36696;
    public static final int BSC_TAG_1_APPLICATION_QUESTIONS = 36697;
    public static final int BSC_TAG_1_COMPLAINTS_GRIEVANCES = 36698;
    public static final int BSC_TAG_1_CHARGES_PAYMENTS = 36699;
    public static final int BSC_TAG_1_FINANCIAL_AID = 36700;
    public static final int BSC_TAG_1_TECHNICAL_ISSUES = 36701;
    public static final int BSC_TAG_1_REGISTRATION_ISSUES = 36702;
    public static final int BSC_TAG_1_ALL_OTHER_QUESTIONS = 36703;
/**********************************************************************/

/*********************EXTRAS**************************/
    public static final int ONE_FORM_DEPT_TICK_ID = 11452;



}
