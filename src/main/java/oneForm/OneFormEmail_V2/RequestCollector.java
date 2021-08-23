package oneForm.OneFormEmail_V2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import td.api.Exceptions.TDException;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.TeamDynamix;
import td.api.Ticket;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

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
        ArrayList<Map<String, String>> reportRows =
            Td.getReport(reportId, true, "").getDataRows();
        for (Map<String, String> row : reportRows) {
            int ticketId = (int) Float.parseFloat(row.get("TicketID"));
            Ticket ticket = Td.getTicket(48, ticketId);
            // v v v v v v v v v v v v v v v v v v v v v v v v v v v v

            // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
            // Insert code here that modifies the ticket in the way you
            // want to.
            Td.editTicket(false, ticket);
        }

        return 123;
    }
}
