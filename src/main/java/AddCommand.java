import java.util.ArrayList;
import java.util.List;

public class AddCommand extends Command<Task> {
    protected Task toAdd;
    
    public AddCommand(Task toAdd) {
        this.toAdd = toAdd;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            tasks.addTask(this.toAdd);
            this.result = this.toAdd;
            
            storage.save(tasks);
            
            List<String> messages = new ArrayList<String>();
            messages.add("I've added \"%s\" to your list :3"
                    .formatted(this.result.getDescription()));
            ui.showRegularMessage(messages);
        } catch (MittensException e) {
            ui.showErrorMessage(e);
        }
    }
}
