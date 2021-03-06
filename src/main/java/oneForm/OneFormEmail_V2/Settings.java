package oneForm.OneFormEmail_V2;

public class Settings {
    // If you would like to run the program in debug mode set this
    // variable to true. In debug mode the comments are longer and more
    // detailed.
    public static boolean debug = false;

    // This determines whether notes will be added to the history. Turn
    // this on if you want the papertrail to display far more detail.
    public static boolean displayNotes = true;

    // If you would like tickets to be created in the teamdynamix
    // sandbox instead of the live version of teamdynamix then set this
    // value to true.
    public static boolean sandbox = false;

    // If you would like to display only error messages instead of all
    // messages, set this variable to false.
    public static boolean displayInfo = true;

    // If you would like to display the ticket bodies for each ticket
    // upon upload, set this variable to true.
    public static boolean displayTicketBodies = false;
}
