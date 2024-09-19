import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TextUi extends Ui {
    private final static String GREETING_MESSAGE = """
             /\\_/\\     ____________________
             >^,^<    / Hi, I'm Mittens!   \\
              / \\     \\ I'm a cat! Meow :3 /
             (___)_/   --------------------
            """;

    private final static String EXIT_MESSAGE = """
             /\\_/\\     _____________
             >^,^<    ( Bye-bye! :3 )
              / \\      -------------
             (___)_/
            """;

    private final Scanner in;
    private final PrintStream out;

    public TextUi() {
        this(System.in, System.out);
    }

    public TextUi(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }
    
    public void printGreetingMessage() {
        this.printBlankLine();
        this.out.println(GREETING_MESSAGE);
        this.printBlankLine();
    }

    public void printExitMessage() {
        this.printBlankLine();
        this.out.println(EXIT_MESSAGE);
        this.printBlankLine();
    }
    
    public void printMessage(String message) {
        this.out.println(message);
    }
    
    public void printBlankLine() {
        this.out.println();
    }

    public void printMittens(String message) {
        int len = message.length();
        String uiMessage = """
                 /\\_/\\     %s
                 >^,^<    ( %s )
                  / \\      %s
                 (___)_/
                """.formatted("_".repeat(len + 2),
                message, "-".repeat(len + 2));

        this.out.println(uiMessage);
    }

    @Override
    public <T> void showCommandCompletion(Command<T> c) {
        // TODO
    }

    @Override
    public void showError(MittensException e) {
        String mittensMessage = e.getMittensMessage();
        int len = mittensMessage.length();

        String helpMessage = e.getHelpMessage();
        String errorMessage = e.getMessage();

        String uiMessage = """
                 /\\_/\\     %s
                 >x.x<    ( %s )
                  / \\      %s
                 (___)_/
                
                Error: %s
                %s
                """.formatted("_".repeat(len + 2), mittensMessage,
                "-".repeat(len + 2), errorMessage, helpMessage);

        this.out.println(uiMessage);
    }

    @Override
    public String getUserInput() {
        this.out.print("> ");
        return this.in.nextLine();
    }
}
