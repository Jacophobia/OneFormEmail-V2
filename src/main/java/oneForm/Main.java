package oneForm;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import td.api.*;
import td.api.HttpCommunication.ResourceType;
import td.api.Logging.History;

/**
 * Contains functions to set up BootStrap and the Heroku API
 *
 * @author Robby Breidenbaugh & Dallin Crawley
 * @since 12/21/20
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        };
    }

    @Bean(name = "teamDynamix")
    public TeamDynamix getTeamDynamix() {
        History history = new History(ResourceType.NONE, "Created to allow program to run.");
        return new TeamDynamix(System.getenv("TD_API_BASE_URL"), System.getenv("USERNAME"), System.getenv("PASSWORD"), history);
    }
}
