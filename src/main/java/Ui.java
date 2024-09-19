public abstract class Ui {
    public abstract <T> void showCommandCompletion(Command<T> c);
    public abstract void showError(MittensException e);
    public abstract String getUserInput();
}
