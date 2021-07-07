package oneForm.OneFormEmail_V2;

import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.Logging.TDLoggingManager;

import java.util.logging.Level;

public class LoggingSupervisor {
    private History history;
    private Class<?> currentClass;
    private String currentMethod;
    private TDLoggingManager loggingManager;

    public LoggingSupervisor(History history, Class<?> currentClass) {
        loggingManager = new TDLoggingManager(Settings.debug);

        this.currentClass = currentClass;

        assert currentClass != null : "The class value passed as a parameter has not been initialized";

        assert history != null : "The history value passed as a parameter has not been initialized";

        this.history = history;
    }

    public void setCurrentClass(Class<?> newClass) {
        assert newClass != null : "The class declaration is empty";

        this.currentClass = newClass;
    }

    public void setCurrentMethod(String newMethod) {
        assert newMethod != null && !newMethod.equals("") : "The method declaration is empty";

        this.currentMethod = newMethod;
    }

    public void log(String message) {
        assert loggingManager != null : "Logging Manager has not been initialized";
        assert history != null : "The current history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") : "The current method has not been declared";
        assert currentClass != null : "The current class has not been declared";

        if (Settings.displayInfo) {
            history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.INFO));
        }
    }

    public void log(String currentMethod, String message) {
        setCurrentMethod(currentMethod);
        log(message);
    }


    public void logWarning(String message) {
        assert loggingManager != null : "Logging Manager has not been initialized";
        assert history != null : "The current history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") : "The current method has not been declared";
        assert currentClass != null : "The current class has not been declared";

        history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.WARNING));
    }

    public void logWarning(String currentMethod, String message) {
        setCurrentMethod(currentMethod);
        logWarning(message);
    }

    public void logError(String message) {
        assert loggingManager != null : "Logging Manager has not been initialized";
        assert history != null : "The current history has not been initialized";
        assert currentMethod != null && !currentMethod.equals("") : "The current method has not been declared";
        assert currentClass != null : "The current class has not been declared";

        history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.SEVERE));
    }

    public void logError(String currentMethod, String message) {
        setCurrentMethod(currentMethod);
        logError(message);
    }

    public void displayLog() {
        assert loggingManager != null : "Logging Manager has not been initialized";
        assert history != null : "History has not been initialized";

        loggingManager.logHistory(history);
    }

    public History getHistory() {
        assert history != null : "History has not been initialized";

        return history;
    }

    public TDLoggingManager getLoggingManager() {
        assert loggingManager != null : "Logging Manager has not been initialized";

        return loggingManager;
    }
}
