import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class Mittens {

    // Unfortunately we were unable to use the initial cat ASCII art due to the
    // Unicode characters interfering with the UI tests.
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
    
    
    private final static Storage storage = new Storage("data/data.txt");
    private static TaskList taskList;
    
    public static void greet() {
        System.out.println(GREETING_MESSAGE);
    }

    public static void echo(String command) {
        int len = command.length();
        String message = """
                
                 /\\_/\\     %s
                 >^,^<    ( %s )
                  / \\      %s
                 (___)_/
                """.formatted("_".repeat(len + 2),
                command, "-".repeat(len + 2));

        System.out.println(message);
    }

    public static void addTask(Task task) {
        taskList.addTask(task);
        System.out.printf("\nI've added \"%s\" to your list :3\n\n", task.getDescription());
    }

    public static void listTasks() {
        if (taskList.getCount() == 0) {
            System.out.println("\nMeow?! Your list is empty!\n");
            return;
        }
        System.out.printf("\nYou have %d tasks in your list, here they are :3\n", taskList.getCount());
        for (int i = 0; i < taskList.getCount(); i++) {
            Task task = taskList.getTask(i);
            System.out.printf("%d. %s\n", i + 1, task.toString());
        }
        System.out.print("\n");
    }

    public static void markTaskAsDone(int index) throws BadInputException {
        if (index > taskList.getCount()) {
            throw new BadInputException("Task index is out of range");
        }
        Task task = taskList.getTask(index - 1);
        task.markAsDone();
        System.out.printf("\nMeow, I scratched the check box for you:\n%s\n\n", task.toString());
    }

    public static void markTaskAsNotDone(int index) throws BadInputException {
        if (index > taskList.getCount()) {
            throw new BadInputException("Task index is out of range");
        }
        Task task = taskList.getTask(index - 1);
        task.markAsNotDone();
        System.out.printf("\nMeow, I unscratched the check box for you:\n%s\n\n", task.toString());
    }
    
    public static void deleteTask(int index) throws BadInputException {
        if (index > taskList.getCount()) {
            throw new BadInputException("Task index is out of range");
        }
        Task task = taskList.removeTask(index - 1);
        System.out.printf("\nMeow, I deleted the task '%s' for you :3\n\n", task.getDescription());
    }

    public static void exit() {
        System.out.println(EXIT_MESSAGE);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            taskList = storage.load();
        } catch (StorageFileException e) {
            e.echo();
            
            System.out.print("Would you like to continue with a new list instead? (y/n)\n> ");
            String input = scanner.nextLine();
            if (input.equals("y")) {
                taskList = new TaskList();
            } else {
                return;
            }
        } catch (Exception e) {
            InitializationException newException = new InitializationException(e.getMessage());
            newException.echo();
            return;
        }
        
        greet();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    listTasks();
                } else if (input.startsWith("mark")) {
                    try {
                        int index = Integer.parseInt(input.split(" ")[1]);
                        markTaskAsDone(index);
                    } catch (NumberFormatException e) {
                        throw new BadInputException("Argument for command 'mark' must be a number");
                    }
                } else if (input.startsWith("unmark")) {
                    try {
                        int index = Integer.parseInt(input.split(" ")[1]);
                        markTaskAsNotDone(index);
                    } catch (NumberFormatException e) {
                        throw new BadInputException("Argument for command 'mark' must be a number");
                    }
                } else if (input.startsWith("delete")) {
                    try {
                        int index = Integer.parseInt(input.split(" ")[1]);
                        deleteTask(index);
                    } catch (NumberFormatException e) {
                        throw new BadInputException("Argument for command 'delete' must be a number");
                    }
                } else if (input.startsWith("todo")) {
                    String description = input.substring(5);

                    Todo newTodo = new Todo(description);
                    addTask(newTodo);
                } else if (input.startsWith("deadline")) {
                    // Separate the inputs so that the first element contains the description while
                    // the rest contains flags.
                    String[] inputs = input.split(" /");
                    String description = inputs[0].substring(9);
                    
                    LocalDate by = null;
                    for (int i = 1; i < inputs.length; i++) {
                        String[] flagWords = inputs[i].split(" ");
                        if (flagWords[0].equals("by")) {
                            if (by == null) {
                                try {
                                    by = LocalDate.parse(inputs[i].substring(3));
                                } catch (DateTimeParseException e) {
                                    throw new BadInputException("Invalid date format for 'by' flag");
                                }
                            } else {
                                throw new BadInputException("Found duplicate of 'by' flag");
                            }
                        } else {
                            throw new BadInputException("'%s' is not a known flag".formatted(flagWords[0]));
                        }
                    }
                    
                    if (by == null) {
                        throw new BadInputException("Command 'deadline' must have a 'by' flag");
                    }

                    Deadline newDeadline = new Deadline(description, by);
                    addTask(newDeadline);
                } else if (input.startsWith("event")) {
                    // Separate the inputs so that the first element contains the description while
                    // the rest contains flags.
                    String[] inputs = input.split(" /");
                    String description = inputs[0].substring(6);

                    LocalDate from = null;
                    LocalDate to = null;
                    for (int i = 1; i < inputs.length; i++) {
                        String[] flagWords = inputs[i].split(" ");
                        if (flagWords[0].equals("from")) {
                            if (from == null) {
                                try {
                                    from = LocalDate.parse(inputs[i].substring(5));
                                } catch (DateTimeParseException e) {
                                    throw new BadInputException("Invalid date format for 'from' flag");
                                }
                            } else {
                                throw new BadInputException("Found duplicate of 'from' flag");
                            }
                        } else if (flagWords[0].equals("to")) {
                            if (to == null) {
                                try {
                                    to = LocalDate.parse(inputs[i].substring(3));
                                } catch (DateTimeParseException e) {
                                    throw new BadInputException("Invalid date format for 'to' flag");
                                }
                            } else {
                                throw new BadInputException("Found duplicate of 'to' flag");
                            }
                        } else {
                            throw new BadInputException("'%s' is not a known flag".formatted(flagWords[0]));
                        }
                    }

                    if (from == null) {
                        throw new BadInputException("Command 'event' must have a 'from' flag");
                    }

                    if (to == null) {
                        throw new BadInputException("Command 'event' must have a 'to' flag");
                    }

                    Event newEvent = new Event(description, from, to);
                    addTask(newEvent);
                } else if (input.equals("save")) {
                    storage.save(taskList);
                } else {
                    throw new BadInputException("'%s' is not a known command".formatted(input));
                }
            } catch (MittensException e) {
                e.echo();
            } catch (Exception e) {
                UnknownException newException = new UnknownException(e.getMessage());
                newException.echo();
            }
        }

        exit();
    }
}
