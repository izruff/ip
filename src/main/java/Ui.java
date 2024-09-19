import java.util.List;

public abstract class Ui {
    public abstract void showGreetingMessage();
    public abstract void showGoodbyeMessage();
    public abstract void showRegularMessage(List<String> messages);
    public abstract void showMittensMessage(List<String> messages);
    public abstract void showErrorMessage(MittensException e);
    public abstract String getUserInput();
}
