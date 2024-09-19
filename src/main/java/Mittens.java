import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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
    
    
    private Ui ui;
    private Storage storage;
    private TaskList taskList;
    
    public Mittens(String storageFilePath) {
        this.ui = new TextUi();
        this.storage = new Storage(storageFilePath);
        this.taskList = new TaskList();
    }
    
    public void greet() {
        System.out.println(GREETING_MESSAGE);
    }

    public void echo(String command) {
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

    public void addTask(Task task) {
        this.taskList.addTask(task);
        System.out.printf("\nI've added \"%s\" to your list :3\n\n", task.getDescription());
    }

    public void listTasks() {
        if (this.taskList.getCount() == 0) {
            System.out.println("\nMeow?! Your list is empty!\n");
            return;
        }
        System.out.printf("\nYou have %d tasks in your list, here they are :3\n", this.taskList.getCount());
        List<Task> tasks = this.taskList.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.printf("%d. %s\n", i + 1, task.toString());
        }
        System.out.print("\n");
    }

    public void markTaskAsDone(int index) throws BadInputException {
        try {
            Task task = this.taskList.markTaskAsDone(index - 1);
            System.out.printf("\nMeow, I scratched the check box for you:\n%s\n\n", task.toString());
        } catch (IndexOutOfBoundsException e) {
            // TODO: Define custom exceptions for TaskList operations
            throw new BadInputException("Task index is out of range");
        }
    }

    public void markTaskAsNotDone(int index) throws BadInputException {
        try {
            Task task = this.taskList.markTaskAsNotDone(index - 1);
            System.out.printf("\nMeow, I unscratched the check box for you:\n%s\n\n", task.toString());
        } catch (IndexOutOfBoundsException e) {
            // TODO: Define custom exceptions for TaskList operations
            throw new BadInputException("Task index is out of range");
        }
    }
    
    public void deleteTask(int index) throws BadInputException {
        try {
            Task task = this.taskList.deleteTask(index - 1);
            System.out.printf("\nMeow, I deleted the task '%s' for you :3\n\n", task.getDescription());
        } catch (IndexOutOfBoundsException e) {
            // TODO: Define custom exceptions for TaskList operations
            throw new BadInputException("Task index is out of range");
        }
    }

    public static void exit() {
        System.out.println(EXIT_MESSAGE);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        
        try {
            this.taskList = this.storage.load();
        } catch (StorageFileException e) {
            e.echo();
            
            System.out.print("Would you like to continue with a new list instead? (y/n)\n> ");
            String input = scanner.nextLine();
            if (input.equals("y")) {
                this.taskList = new TaskList();
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
                    AddCommand command = new AddCommand(newTodo);
                    command.execute(this.taskList, this.ui, this.storage);
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
                    AddCommand command = new AddCommand(newDeadline);
                    command.execute(this.taskList, this.ui, this.storage);
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
                    AddCommand command = new AddCommand(newEvent);
                    command.execute(this.taskList, this.ui, this.storage);
                } else if (input.equals("save")) {
                    this.storage.save(this.taskList);
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
    
    public static void main(String[] args) {
        Mittens mittens = new Mittens("data/data.txt");
        mittens.run();
    }
}
