public abstract class Command<T> {
    protected boolean isExit;
    protected T result;
    protected MittensException error;
    
    public Command() {
        this.isExit = false;
        this.result = null;
        this.error = null;
    }
    
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);
    public boolean isExit() {
        return this.isExit;
    }
}
