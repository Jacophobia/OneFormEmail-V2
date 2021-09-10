package oneForm.OneFormEmail_V2;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import td.api.*;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains functions to set up BootStrap and the Heroku API
 *
 * @author Jacob Morgan, Robby Breidenbaugh, & Dallin Crawley
 * @since 12/21/20
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        generateArt();
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        };
    }

    @Bean(name = "teamDynamix")
    public TeamDynamix getTeamDynamix() {
        History history = new History(
            ResourceType.NONE,
            "Master History"
        );
        String SB = Settings.sandbox ? "SB" : "";
        return new TeamDynamix(
            System.getenv("TD_API_BASE_URL") + SB,
            System.getenv("USERNAME"),
            System.getenv("PASSWORD"),
            history
        );
    }


    private static void generateArt() {
        Date date = new Date();
        String semester = "";
        SimpleDateFormat formatter = new SimpleDateFormat(
            "MM-dd-yyyy HH:mm:ss"
        );
        String formattedDate = formatter.format(date);
        if (formattedDate.charAt(0) == '0') {
            if (
                formattedDate.charAt(1) >= '1' &&
                formattedDate.charAt(1) <= '4'
            ) {
                semester = "Winter";
            }
            else if (
                formattedDate.charAt(1) >= '5' &&
                formattedDate.charAt(1) <= '7'
            ) {
                semester = "Spring";
            }
            else if (formattedDate.charAt(1) == '8') {
                semester = "Summer";
            }
            else if (formattedDate.charAt(1) == '9') {
                semester = "Fall";
            }
        }
        else if (formattedDate.charAt(1) == '1') {
            semester = "Fall";
        }
        if (semester.equals("Spring"))
            System.out.println(
                "\n\n  .   ____          _            __ _ _\n" +
                " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n" +
                "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n" +
                " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n" +
                "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n" +
                " =========|_|==============|___/=/_/_/_/\n\n");

        else if (semester.equals("Summer"))
            System.out.println(
                "\n\n  .   ____                                \n" +
                " /\\\\ / ___'_   _ _ __  ___ _ __  ___  ___  ____\n" +
                "( ( )\\___ | | | | '_ \\/ _ \\ '_ \\/ _ \\/ _ \\|  __\\\n" +
                " \\\\/  ___)| |_| | |  ||  || |  ||  || ___/| |\n" +
                "  '  |____|___._|_|  ||  ||_|  ||  ||\\___||_|\n" +
                " ===============================================\n\n");

        else if (semester.equals("Fall"))
            System.out.println(
                "\n\n  .    _____         _   _\n" +
                " /\\\\  |  ___|___ _  | | | |\n" +
                "( ( ) | |_  /   ' | | | | |\n" +
                " \\\\/  |  _||  |   | | | | |\n" +
                "  '   |_|   \\___^_| |_| |_|\n" +
                "==============================\n\n"
            );
        else if (semester.equals("Winter"))
            System.out.println(
                "\n\n  .   __          __ _           _\n" +
                " /\\\\  \\ \\   __   / /(_)_ __   __| |__  ___  ____\n" +
                "( ( )  \\ \\ /  \\ / / | | '__ \\|__   __|/ _ \\|  __\\\n" +
                " \\\\/    \\ V /\\ V /  | | /  \\ |  | |  | ___/| |\n" +
                "  '      \\_/  \\_/   |_|_|  |_|  |_|   \\___||_|\n" +
                "===================================================\n\n"
            );
    }
}






