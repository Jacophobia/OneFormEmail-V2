package oneForm.OneFormEmail_V2;

import td.api.Exceptions.TDException;
import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.Logging.TDLoggingManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import static oneForm.OneFormEmail_V2.ProcessRequest.*;

public class LoggingSupervisor {
    private History history;
    private History masterHistory = RequestCollector.history;
    private String currentMethod;
    private TDLoggingManager loggingManager;
    private ErrorTicket errorTicket = null;

    private final int OPERATIONS_APP_ID = 42;

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

    public void logNote(String note) {
        if (!Settings.displayNotes)
            return;
        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        StackTraceElement recentElement = stackTraceElements[2];
        history.addEvent(new LoggingEvent(
            note,
            recentElement.getMethodName(),
            recentElement.getClassName(),
            Level.INFO)
        );
    }

    public void logNote(Class<?> currentClass, String currentMethod,
                        String message) {
        if (!Settings.displayNotes)
            return;
        setCurrentMethod(currentMethod);
        log(currentClass, message);
    }

    private void log(Class<?> currentClass, String message) {
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

    public void log(String message) {
        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        StackTraceElement recentElement = stackTraceElements[2];
        history.addEvent(new LoggingEvent(
            message,
            recentElement.getMethodName(),
            recentElement.getClassName(),
            Level.INFO)
        );
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

    public void logWarning(String message) {
        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        StackTraceElement recentElement = stackTraceElements[2];
        history.addEvent(new LoggingEvent(
            message,
            recentElement.getMethodName(),
            recentElement.getClassName(),
            Level.WARNING)
        );
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

        assert history.getTitle() != null :
            "The History's title has not been set.";

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
            "MM-dd-yyyy HH:mm:ss"
        );

        masterHistory.addEvent(new LoggingEvent(
            "Error in " + history.getTitle() +
            " at " + formatter.format(date),
            history.getTitle(),
            currentClass,
            Level.SEVERE)
        );

        if (errorTicket == null) {
            errorTicket = new ErrorTicket(history.getTitle() +
                "Error in " + history.getTitle() + " at " +
                formatter.format(date) + "\n\n");
        }
        errorTicket.logError(message);
    }

    public void logError(Class<?> currentClass, String currentMethod,
            String message) {
        setCurrentMethod(currentMethod);
        logError(currentClass, message);
    }

    public void logError(String message) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
            "MM-dd-yyyy HH:mm:ss"
        );

        if (errorTicket == null) {
            errorTicket = new ErrorTicket(history.getTitle() +
                "Error in " + history.getTitle() + " at " +
                formatter.format(date) + "\n\n");
        }
        errorTicket.logError(message);

        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        StackTraceElement recentElement = stackTraceElements[2];
        history.addEvent(new LoggingEvent(
            message,
            recentElement.getMethodName(),
            recentElement.getClassName(),
            Level.SEVERE)
        );
    }

    public void displayLog() {
        assert loggingManager != null :
            "Logging Manager has not been initialized";
        assert history != null :
            "History has not been initialized";
        if (errorTicket != null) {
            this.sendErrorReport();
        }

        loggingManager.logHistory(history);
    }

    public void sendErrorReport() {
        assert errorTicket != null :
            "The error ticket has not been initialized";
        if (!Settings.sandbox && errorTicket != null) {
            try {
                push.createTicket(OPERATIONS_APP_ID, errorTicket);
                history.addEvent(new LoggingEvent(
                    "Error Ticket Created.",
                    "sendErrorReport",
                    this.getClass(),
                    Level.WARNING
                ));
            }
            catch (TDException exception) {
                history.addEvent(new LoggingEvent(
                    "Error: Unable to send Error Ticket.",
                    "sendErrorReport",
                    this.getClass(),
                    Level.SEVERE
                ));
            }
        }
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
