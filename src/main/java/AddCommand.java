public class AddCommand extends Command {
    protected Task toAdd;
    
    public AddCommand(Task toAdd) {
        super();
        this.toAdd = toAdd;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            tasks.addTask(this.toAdd);
            
            storage.save(tasks);
            
            ui.showRegularMessage("I've added \"%s\" to your list :3"
                    .formatted(this.toAdd.getDescription()));
        } catch (MittensException e) {
            ui.showErrorMessage(e);
        }
    }
}
