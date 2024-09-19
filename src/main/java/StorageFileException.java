public class StorageFileException extends MittensException {
    private static final String MITTENS_MESSAGE = "Meow?! Something's wrong with your storage!";
    private static final String HELP_MESSAGE = "Your storage might be corrupted. " +
            "Try clearing the data folder and restarting the program.";

    public StorageFileException(String message) {
        super(message, MITTENS_MESSAGE, HELP_MESSAGE);
    }
}
