public class DeleteCommand extends Command {
    protected int index;

    public DeleteCommand(int index) {
        super();
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            Task deletedTask = tasks.deleteTask(this.index);

            storage.save(tasks);

            ui.showRegularMessage("Meow, I deleted the task \"%s\" for you :3"
                    .formatted(deletedTask.getDescription()));
        } catch (MittensException e) {
            ui.showErrorMessage(e);
        }
    }
}
