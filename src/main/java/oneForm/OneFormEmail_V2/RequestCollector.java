package oneForm.OneFormEmail_V2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import td.api.CustomAttribute;
import td.api.Exceptions.TDException;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.TeamDynamix;
import td.api.Ticket;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

import static oneForm.OneFormEmail_V2.ProcessRequest.push;

@RestController
public class RequestCollector {
    @Resource(name = "teamDynamix")
    private TeamDynamix Td;

    private static final RequestManager requestManager = new RequestManager();
    public static History history = new History(
        ResourceType.NONE,
        "Master History"
    );
    public static enum ACTION_REQUESTED {
        ESCALATED, RESOLVED, SPAM
    }

    @RequestMapping(value = "/send-escalated-request", params = {"ticketID"})
    public @ResponseBody
    int sendEscalatedRequest(@RequestParam(value = "ticketID") int ticketID
            ) throws InterruptedException {
        if (Settings.sandbox)
            System.out.println("\tEscalated Request ID: " + ticketID);
        requestManager.addServiceRequest(
            new ProcessRequest(ticketID, ACTION_REQUESTED.ESCALATED),
            history,
            "ESCL Ticket ID: " + ticketID
        );

        return 123;
    }

    @RequestMapping(value = "/send-resolved-request", params = {"ticketID"})
    public @ResponseBody
    int sendResolvedRequest(@RequestParam(value = "ticketID") int ticketID
            ) throws InterruptedException {
        if (Settings.sandbox)
            System.out.println("\tResolved Request ID: " + ticketID);
        requestManager.addServiceRequest(
            new ProcessRequest(ticketID, ACTION_REQUESTED.RESOLVED),
            history,
            "RSLV Ticket ID: " + ticketID
        );

        return 123;
    }

    @RequestMapping(value = "/send-spam-request", params = {"ticketID"})
    public @ResponseBody
    int sendSpamRequest(@RequestParam(value = "ticketID") int ticketID
            ) throws InterruptedException {
        if (Settings.sandbox)
            System.out.println("\tSpam Request ID: " + ticketID);
        requestManager.addServiceRequest(
            new ProcessRequest(ticketID, ACTION_REQUESTED.SPAM),
            history,
            "SPAM Ticket ID: " + ticketID
        );

        return 123;
    }

    /**
     * If you would like to adjust the program's settings while the
     * program is still running, you can use this endpoint to do so.
     * @param debug determines whether the program displays debugging
     *              information.
     * @param displayNotes determines if low priority messages should be
     *                     added to the history.
     * @param sandbox determines whether tickets should be created in
     *                TDSandbox or TDNext.
     * @param displayInfo If this is true then only errors and warnings
     *                    will be displayed. Only enable this if you
     *                    need to conserve as much space as possible on
     *                    the papertrail.
     * @param displayTicketBodies set this value to true if you want to
     *                            display the contents of a ticket when
     *                            it is uploaded and when errors occur.
     * @return the status code of the program.
     */
    @RequestMapping(value = "/adjust-settings", params = {
        "debug", "displayNotes", "sandbox", "displayInfo", "displayTicketBodies"
    })
    public @ResponseBody
    int adjustSettings(
        @RequestParam(value = "debug") boolean debug,
        @RequestParam(value = "displayNotes") boolean displayNotes,
        @RequestParam(value = "sandbox") boolean sandbox,
        @RequestParam(value = "displayInfo") boolean displayInfo,
        @RequestParam(value = "displayTicketBodies") boolean displayTicketBodies
            ) {
        System.out.println("\nSettings Changed From - ");
        System.out.println(
            "\ndebug               :  " + Settings.debug + "\n" +
            "displayNotes        :  " + Settings.displayNotes + "\n" +
            "sandbox             :  " + Settings.sandbox + "\n" +
            "displayInfo         :  " + Settings.displayInfo + "\n" +
            "displayTicketBodies :  " + Settings.displayTicketBodies
        );

        Settings.debug = debug;
        Settings.displayNotes = displayNotes;
        Settings.sandbox = sandbox;
        Settings.displayInfo = displayInfo;
        Settings.displayTicketBodies = displayTicketBodies;

        System.out.println("\nTo - ");
        System.out.println(
            "\ndebug               :  " + Settings.debug + "\n" +
            "displayNotes        :  " + Settings.displayNotes + "\n" +
            "sandbox             :  " + Settings.sandbox + "\n" +
            "displayInfo         :  " + Settings.displayInfo + "\n" +
            "displayTicketBodies :  " + Settings.displayTicketBodies
        );
        return 123;
    }


    /**
     * If you need to run a large number of tickets all at once, use
     * this endpoint. It will probably be easiest in postman or a
     * similar service. Just create a report or use the OneForm Email
     * Clean-Up Report (ID 19112). Send the report's ID as a parameter
     * in the HTTP request and the program will cycle through all the
     * tickets in the report.
     * @param reportId The id of the report you would like to pull
     *                 tickets from.
     * @return The status code of the program to send to the requester.
     * @throws TDException This is our custom exception class which
     * gives custom errors for things that we frequently encounter
     * working with teamdynamix.
     * @throws InterruptedException if you connect this to the program
     * this is required to account for semaphores.
     */
    @RequestMapping(value = "/run-report", params = {"reportID"})
    public @ResponseBody
    int runReport(@RequestParam(value = "reportID") int reportId
            ) throws TDException, InterruptedException {
        String SB = Settings.sandbox ? "SB" : "";
        TeamDynamix push = new TeamDynamix(
            System.getenv("TD_API_BASE_URL") + SB,
            System.getenv("USERNAME"),
            System.getenv("PASSWORD"),
            history
        );
        TeamDynamix pull = new TeamDynamix(
            System.getenv("TD_API_BASE_URL"),
            System.getenv("USERNAME"),
            System.getenv("PASSWORD"),
            history
        );
        System.out.println("\tRun Report Request ID: " + reportId);
        ArrayList<Map<String, String>> report =
            pull.getReport(reportId, true, "").getDataRows();

        // academic
        ArrayList<String> academicIds = new ArrayList<>();
        academicIds.add("31788");
        academicIds.add("31789");
        academicIds.add("31797");
        academicIds.add("31790");
        academicIds.add("31791");
        academicIds.add("31792");
        academicIds.add("31793");
        academicIds.add("31794");
        academicIds.add("31795");
        academicIds.add("31808");
        academicIds.add("31800");
        academicIds.add("31801");
        academicIds.add("31802");
        academicIds.add("31803");
        academicIds.add("31804");
        academicIds.add("31805");
        academicIds.add("31806");
        academicIds.add("31796");
        academicIds.add("31826");

        // accounting
        ArrayList<String> accountingIds = new ArrayList<>();
        accountingIds.add("31999");
        accountingIds.add("32000");
        accountingIds.add("31997");
        accountingIds.add("32002");
        accountingIds.add("32003");
        accountingIds.add("32004");
        accountingIds.add("32005");
        accountingIds.add("31994");
        accountingIds.add("32006");
        accountingIds.add("32007");
        accountingIds.add("32008");
        accountingIds.add("32009");
        accountingIds.add("32010");
        accountingIds.add("32011");
        accountingIds.add("32001");
        accountingIds.add("32012");
        accountingIds.add("32013");
        accountingIds.add("32014");
        accountingIds.add("31995");
        accountingIds.add("31996");
        accountingIds.add("32015");
        accountingIds.add("31998");
        accountingIds.add("32017");
        accountingIds.add("31826");

        // admissions
        ArrayList<String> admissionsIds = new ArrayList<>();
        admissionsIds.add("31966");
        admissionsIds.add("31967");
        admissionsIds.add("31980");
        admissionsIds.add("31968");
        admissionsIds.add("31969");
        admissionsIds.add("31970");
        admissionsIds.add("31971");
        admissionsIds.add("31972");
        admissionsIds.add("31973");
        admissionsIds.add("31974");
        admissionsIds.add("31975");
        admissionsIds.add("31976");
        admissionsIds.add("31977");
        admissionsIds.add("31978");
        admissionsIds.add("31979");
        admissionsIds.add("31981");
        admissionsIds.add("31982");
        admissionsIds.add("31983");
        admissionsIds.add("31984");
        admissionsIds.add("31985");
        admissionsIds.add("31986");
        admissionsIds.add("31987");
        admissionsIds.add("31988");
        admissionsIds.add("31989");
        admissionsIds.add("31990");
        admissionsIds.add("31991");
        admissionsIds.add("31992");
        admissionsIds.add("31993");
        admissionsIds.add("31826");

        // advising
        ArrayList<String> advisingIds = new ArrayList<>();
        advisingIds.add("31953");
        advisingIds.add("31954");
        advisingIds.add("31955");
        advisingIds.add("31965");
        advisingIds.add("31956");
        advisingIds.add("31957");
        advisingIds.add("31958");
        advisingIds.add("31959");
        advisingIds.add("31961");
        advisingIds.add("31962");
        advisingIds.add("31963");
        advisingIds.add("31964");
        advisingIds.add("31826");
        advisingIds.add("31960");

        // facilities
        ArrayList<String> facilitiesIds = new ArrayList<>();
        facilitiesIds.add("31952");
        facilitiesIds.add("31945");
        facilitiesIds.add("31946");
        facilitiesIds.add("31947");
        facilitiesIds.add("33595");
        facilitiesIds.add("31948");
        facilitiesIds.add("31899");
        facilitiesIds.add("33598");
        facilitiesIds.add("31949");
        facilitiesIds.add("31950");
        facilitiesIds.add("33596");
        facilitiesIds.add("31951");
        facilitiesIds.add("33597");
        facilitiesIds.add("31826");

        // financial aid
        ArrayList<String> financialAidIds = new ArrayList<>();
        financialAidIds.add("31921");
        financialAidIds.add("31922");
        financialAidIds.add("31924");
        financialAidIds.add("31944");
        financialAidIds.add("31936");
        financialAidIds.add("31937");
        financialAidIds.add("31925");
        financialAidIds.add("31938");
        financialAidIds.add("31926");
        financialAidIds.add("31927");
        financialAidIds.add("31928");
        financialAidIds.add("31929");
        financialAidIds.add("31930");
        financialAidIds.add("31931");
        financialAidIds.add("31923");
        financialAidIds.add("31933");
        financialAidIds.add("31932");
        financialAidIds.add("31934");
        financialAidIds.add("31935");
        financialAidIds.add("31939");
        financialAidIds.add("31941");
        financialAidIds.add("31942");
        financialAidIds.add("31943");
        financialAidIds.add("31940");
        financialAidIds.add("31826");

        // general
        ArrayList<String> generalIds = new ArrayList<>();
        generalIds.add("31877");
        generalIds.add("31878");
        generalIds.add("31879");
        generalIds.add("31881");
        generalIds.add("31882");
        generalIds.add("31880");
        generalIds.add("31883");
        generalIds.add("31884");
        generalIds.add("31885");
        generalIds.add("31886");
        generalIds.add("31887");
        generalIds.add("31888");
        generalIds.add("31889");
        generalIds.add("31890");
        generalIds.add("31891");
        generalIds.add("31892");
        generalIds.add("31893");
        generalIds.add("31895");
        generalIds.add("31894");
        generalIds.add("33587");
        generalIds.add("31896");
        generalIds.add("31902");
        generalIds.add("33585");
        generalIds.add("33588");
        generalIds.add("33586");
        generalIds.add("31897");
        generalIds.add("31898");
        generalIds.add("31901");
        generalIds.add("31903");
        generalIds.add("31904");
        generalIds.add("31905");
        generalIds.add("31906");
        generalIds.add("31907");
        generalIds.add("31908");
        generalIds.add("33593");
        generalIds.add("31909");
        generalIds.add("31910");
        generalIds.add("31911");
        generalIds.add("31912");
        generalIds.add("31913");
        generalIds.add("31914");
        generalIds.add("31915");
        generalIds.add("31916");
        generalIds.add("31917");
        generalIds.add("31918");
        generalIds.add("31919");
        generalIds.add("31920");
        generalIds.add("31826");
        generalIds.add("34472");
        generalIds.add("34471");

        // IT
        ArrayList<String> ItIds = new ArrayList<>();
        ItIds.add("31862");
        ItIds.add("31863");
        ItIds.add("31856");
        ItIds.add("31857");
        ItIds.add("31858");
        ItIds.add("31859");
        ItIds.add("31860");
        ItIds.add("31861");
        ItIds.add("31826");

        // SRR
        ArrayList<String> SrrIds = new ArrayList<>();
        SrrIds.add("31827");
        SrrIds.add("31847");
        SrrIds.add("31828");
        SrrIds.add("31829");
        SrrIds.add("31830");
        SrrIds.add("31831");
        SrrIds.add("31832");
        SrrIds.add("31833");
        SrrIds.add("31834");
        SrrIds.add("31835");
        SrrIds.add("31855");
        SrrIds.add("31836");
        SrrIds.add("31837");
        SrrIds.add("31840");
        SrrIds.add("31841");
        SrrIds.add("31839");
        SrrIds.add("31844");
        SrrIds.add("31842");
        SrrIds.add("31843");
        SrrIds.add("31845");
        SrrIds.add("31846");
        SrrIds.add("31838");
        SrrIds.add("31848");
        SrrIds.add("31849");
        SrrIds.add("31850");
        SrrIds.add("31851");
        SrrIds.add("31852");
        SrrIds.add("31853");
        SrrIds.add("31854");
        SrrIds.add("31826");

        int counter = 0;
        
        for (Map<String, String> row : report) {
            for (String tagId : academicIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31724"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : accountingIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31723"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : admissionsIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31725"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : advisingIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31727"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : facilitiesIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31728"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : financialAidIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31729"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : generalIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31730"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : ItIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31732"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }
            for (String tagId : SrrIds) {
                int ticketId = (int) Float.parseFloat(row.get("TicketID"));
                Ticket ticket = pull.getTicket(48, ticketId);
                // v v v v v v v v v v v v v v v v v v v v v v v v v v v v
                ticket.getAttributes().add(new CustomAttribute(10329, "31733"));
                ticket.getAttributes().add(new CustomAttribute(10333, tagId));
                // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
                // Insert code here that modifies the ticket in the way you
                // want to.
                pull.editTicket(false, ticket);
                if (Settings.sandbox)
                    System.out.println(
                        "\tEscalated Request ID: " + ticketId + " : " + counter
                    );
                requestManager.addServiceRequest(
                    new ProcessRequest(ticketId, ACTION_REQUESTED.ESCALATED),
                    history,
                    "ESCL Ticket ID: " + ticketId + " : " + counter
                );
                counter++;
                Thread.sleep(10000L);
            }

        }

        return 123;
    }
}