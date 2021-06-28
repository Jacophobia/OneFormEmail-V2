package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;
import td.api.Logging.TDLoggingManager;
import td.api.MultiThreading.TDRunnable;

public class ProcessRequest extends TDRunnable {

    public ProcessRequest(int ticketID) {
        TDLoggingManager loggingManager = new TDLoggingManager(true);
        History history = new History()
        super(loggingManager, history);
    }

    @Override
    public void executeTask() throws TDException {

    }
}
