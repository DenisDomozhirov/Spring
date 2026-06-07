package work.dir.console;

import org.springframework.stereotype.Component;
import work.dir.operations.ConsoleOperationType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleInput {

    private final Scanner scan;

    public ConsoleInput(Scanner scan) {
        this.scan = scan;
    }

    public ConsoleOperationType readOperationType(){
        while(true){

            System.out.println("Print operation command: ");
            String value = scan.nextLine().trim();
            if(value.isBlank()){
                System.out.println("Error! Command must not be blank");
                printAvailbleCommands();
                continue;
            }
            try{
                return ConsoleOperationType.valueOf(value.toUpperCase(Locale.ROOT));
            } catch(IllegalArgumentException e){
                System.out.println("Error! Unknown command");
                printAvailbleCommands();
            }
        }
    }

    public String readRequiredString(String inputLine, String fieldName){
        while(true){
            System.out.println(inputLine);
            String value = scan.nextLine().trim();
            if(!value.isBlank()){
                return value;
            }
            System.out.println("Error! " + fieldName + " must not be blank!");
        }
    }

    public int readPositiveInt(String inputLine, String fieldName){
        while(true){
            System.out.println(inputLine);
            String value = scan.nextLine().trim();
            if(value.isBlank()) {
                System.out.println("Error! " + fieldName + " must not be blank!");
                continue;
            }
            try{
                int parsed = Integer.parseInt(value);
                if(parsed <= 0){
                    System.out.println("Error! " + fieldName + " must not be blank!");
                    continue;
                }
                return parsed;
            } catch (NumberFormatException e){
                System.out.println("Error! " + fieldName + " must not be blank!");
            }
        }
    }

    public void printAvailbleCommands(){
        String availableCommands = Arrays.stream(ConsoleOperationType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        System.out.println("Available commands: " + availableCommands);
    }
}
