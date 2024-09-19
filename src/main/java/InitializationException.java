public class InitializationException extends MittensException {
    private static final String MITTENS_MESSAGE = "Something is preventing me from starting...";
    private static final String HELP_MESSAGE = "This is most likely a bug in the software.";

    public InitializationException(String message) {
        super(message, MITTENS_MESSAGE, HELP_MESSAGE);
    }
}
