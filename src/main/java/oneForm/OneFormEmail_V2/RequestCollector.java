package oneForm.OneFormEmail_V2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;
import td.api.TeamDynamix;

import javax.annotation.Resource;

@RestController
public class RequestCollector {
    @Resource(name = "teamDynamix")
    private TeamDynamix Td;

    private static final RequestManager requestManager = new RequestManager();
    public static History history = new History(
        ResourceType.NONE,
        "Master History"
    );
    public static enum ACTION_TAKEN {
        ESCALATED, RESOLVED, SPAM
    }

    /**
     * sendNewTicket:
     * This is where the magic happens. On creation we get the ID of the
     * One Form ticket, we then use that to call all of the functions
     * needed to pass that information into the correct ticket of the
     * correct application.
     */


    @RequestMapping(value = "/send-escalated-request", params = {"ticketID"})
    public @ResponseBody
    int sendEscalatedRequest(@RequestParam(value = "ticketID") int ticketID
            ) throws InterruptedException {
        requestManager.addServiceRequest(
            new ProcessRequest(ticketID, ACTION_TAKEN.ESCALATED),
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
            new ProcessRequest(ticketID, ACTION_TAKEN.RESOLVED),
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
            new ProcessRequest(ticketID, ACTION_TAKEN.SPAM),
            history,
            "SPAM Ticket ID: " + ticketID
        );

        return 123;
    }
}
