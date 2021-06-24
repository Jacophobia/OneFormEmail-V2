//THE ANDON CORD REPORT https://td.byui.edu/TDNext/Apps/42/Tickets/New?formId=3068

package oneForm;

import oneForm.OneFormEmail_V2.RequestManager;
import oneForm.Runnables.ServiceRequest;
import org.springframework.web.bind.annotation.*;
import td.api.*;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.*;

import javax.annotation.Resource;

import static java.lang.Integer.parseInt;


/**
 * This is the main driver, here we call all of the functions needed to create the new
 *  tickets and upload them to TD. We also define the create and upload functions here.
 *
 * @author Robby Breidenbaugh
 * @since 12/21/20
 */

@RestController
public class oneFormDriver {
    @Resource(name = "teamDynamix")
    private TeamDynamix Td;

    private static final RequestManager threadManager = new RequestManager();
    History history = new History(ResourceType.NONE, "Master History");

    /**
     * sendNewTicket:
     * This is where the magic happens. On creation we get the ID of the One Form ticket,
     * we then use that to call all of the functions needed to pass that information into
     * the correct ticket of the correct application.
     */


    @RequestMapping(value = "/executeProgram", params = {"ticketID"})
    public @ResponseBody
    int sendNewTicket(@RequestParam(value = "ticketID") int ticketID) throws InterruptedException {
        threadManager.addServiceRequest(new ServiceRequest(ticketID), history, "Ticket ID: " + ticketID);

        return 123;

    }

}