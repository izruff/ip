import java.util.Scanner;

public class Mittens {
    private final static String GREETING_MESSAGE = """
            
                ██          ██                ____________________
              ██░░██      ██░░██             / Hi, I'm Mittens!   \\
              ██░░░░██████░░░░██             | I'm a cat! Meow :3 |
            ██░░░░░░░░░░░░░░░░░░██           \\ How can I help?    /
            ██░░░░░░░░░░░░░░░░░░██            --------------------
            ██░░██░░░░░░░░██░░░░██
            ██░░██░░░░░░░░██░░░░██    ████
            ██░░░░░░░░░░░░░░░░░░██  ██    ██
              ████▒▒▒▒▒▒▒▒▒▒████    ██    ██
                  ██░░▒▒▒▒░░░░░░██    ██    ██
                  ██░░██░░████░░░░██  ██░░░░██
                  ██░░████░░░░██░░██  ██░░░░██
                  ██  ████░░░░░░░░████▒▒▒▒▒▒██
                  ██  ██    ░░░░░░██▒▒▒▒████
                  ██████████████████████
            """;

    private final static String EXIT_MESSAGE = """
            
                ██          ██                _____________
              ██░░██      ██░░██             ( Bye-bye! :3 )
              ██░░░░██████░░░░██              -------------
            ██░░░░░░░░░░░░░░░░░░██
            ██░░░░░░░░░░░░░░░░░░██
            ██░░██░░░░░░░░██░░░░██
            ██░░██░░░░░░░░██░░░░██    ████
            ██░░░░░░░░░░░░░░░░░░██  ██    ██
              ████▒▒▒▒▒▒▒▒▒▒████    ██    ██
                  ██░░▒▒▒▒░░░░░░██    ██    ██
                  ██░░██░░████░░░░██  ██░░░░██
                  ██░░████░░░░██░░██  ██░░░░██
                  ██  ████░░░░░░░░████▒▒▒▒▒▒██
                  ██  ██    ░░░░░░██▒▒▒▒████
                  ██████████████████████
            """;

    public static void greet() {
        System.out.println(GREETING_MESSAGE);
    }

    public static void echo(String command) {
        int len = command.length();
        String message = """
                
                    ██          ██                %s
                  ██░░██      ██░░██             ( %s )
                  ██░░░░██████░░░░██              %s
                ██░░░░░░░░░░░░░░░░░░██
                ██░░░░░░░░░░░░░░░░░░██
                ██░░██░░░░░░░░██░░░░██
                ██░░██░░░░░░░░██░░░░██    ████
                ██░░░░░░░░░░░░░░░░░░██  ██    ██
                  ████▒▒▒▒▒▒▒▒▒▒████    ██    ██
                      ██░░▒▒▒▒░░░░░░██    ██    ██
                      ██░░██░░████░░░░██  ██░░░░██
                      ██░░████░░░░██░░██  ██░░░░██
                      ██  ████░░░░░░░░████▒▒▒▒▒▒██
                      ██  ██    ░░░░░░██▒▒▒▒████
                      ██████████████████████
                """
                .formatted("_".repeat(len + 2),
                        command, "-".repeat(len + 2));

        System.out.println(message);
    }

    public static void exit() {
        System.out.println(EXIT_MESSAGE);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        greet();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            } else {
                echo(input);
            }
        }

        exit();
    }
}
