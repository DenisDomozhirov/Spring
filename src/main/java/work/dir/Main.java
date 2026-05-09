package work.dir;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import work.dir.console.OperationConsoleListener;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("work.dir");

        OperationConsoleListener consoleListener = context.getBean(OperationConsoleListener.class);
        consoleListener.runProgramm();
    }
}