package oneForm.OneFormEmail_V2;

import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.Logging.TDLoggingManager;

import java.util.logging.Level;

public class LoggingSupervisor {
    private History history;
    private History masterHistory = RequestCollector.history;
    private String currentMethod;
    private TDLoggingManager loggingManager;

    public LoggingSupervisor(History history) {
        loggingManager = new TDLoggingManager(Settings.debug);

        assert history != null :
            "The history value passed as a parameter has not been initialized";

        this.history = history;
    }

    public void setCurrentMethod(String newMethod) {
        assert newMethod != null && !newMethod.equals("") :
            "The method declaration is empty";

        this.currentMethod = newMethod;
    }

    public void log(Class<?> currentClass, String message) {
        assert loggingManager != null :
            "Logging Manager has not been initialized";
        assert history != null :
            "The current history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") :
            "The current method has not been declared";
        assert currentClass != null :
            "The current class has not been declared";

        if (Settings.displayInfo) {
            history.addEvent(new LoggingEvent(
                message, currentMethod,
                currentClass,
                Level.INFO)
            );
        }
    }

    public void log(Class<?> currentClass, String currentMethod,
                    String message) {
        setCurrentMethod(currentMethod);
        log(currentClass, message);
    }


    public void logWarning(Class<?> currentClass, String message) {
        assert loggingManager != null :
            "Logging Manager has not been initialized";
        assert history != null :
            "The current history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") :
            "The current method has not been declared";
        assert currentClass != null :
            "The current class has not been declared";

        history.addEvent(new LoggingEvent(
            message, currentMethod,
            currentClass,
            Level.WARNING)
        );
    }

    public void logWarning(Class<?> currentClass, String currentMethod,
                           String message) {
        setCurrentMethod(currentMethod);
        logWarning(currentClass, message);
    }

    public void logError(Class<?> currentClass, String message) {
        assert loggingManager != null :
            "Logging Manager has not been initialized";
        assert history != null :
            "The current history has not been initialized";
        assert masterHistory != null :
            "The current master history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") :
            "The current method has not been declared";
        assert currentClass != null :
            "The current class has not been declared";

        history.addEvent(new LoggingEvent(
            message,
            currentMethod,
            currentClass,
            Level.SEVERE)
        );

        assert ProcessRequest.oneFormTicketID > 0 :
            "The oneFormTicketID has not been initialized.";
        masterHistory.addEvent(new LoggingEvent(
            message,
            "" + ProcessRequest.oneFormTicketID,
            currentClass,
            Level.SEVERE)
        );
    }

    public void logError(Class<?> currentClass, String currentMethod,
                         String message) {
        setCurrentMethod(currentMethod);
        logError(currentClass, message);
    }

    public void displayLog() {
        assert loggingManager != null :
            "Logging Manager has not been initialized";
        assert history != null :
            "History has not been initialized";

        loggingManager.logHistory(history);
    }

    public History getHistory() {
        assert history != null : "History has not been initialized";

        return history;
    }

    public TDLoggingManager getLoggingManager() {
        assert loggingManager != null :
            "Logging Manager has not been initialized";

        return loggingManager;
    }
}
