package oneForm.OneFormEmail_V2;

import td.api.Logging.History;
import td.api.Logging.LoggingEvent;
import td.api.Logging.TDLoggingManager;

import java.util.logging.Level;

public class LoggingSupervisor {
    History history;
    Class<?> currentClass;
    String currentMethod;
    TDLoggingManager loggingManager;

    public LoggingSupervisor(History history) {
        loggingManager = new TDLoggingManager(Config.debug);
        this.history = history;
    }

    public void setCurrentClass(Class<?> newClass) {
        this.currentClass = newClass;
    }

    public void setCurrentMethod(String newMethod) {
        this.currentMethod = newMethod;
    }

    public void log(String message) {
        history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.INFO));
    }

    public void logWarning(String message) {
        history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.WARNING));
    }

    public void logError(String message) {
        history.addEvent(new LoggingEvent(message, currentMethod, currentClass, Level.SEVERE));
    }

    public void displayLog() {
        loggingManager.logHistory(history);
    }

    public History getHistory() {
        return history;
    }

    public TDLoggingManager getLoggingManager() {
        return loggingManager;
    }
}
