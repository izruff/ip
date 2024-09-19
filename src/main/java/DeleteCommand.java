import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends Command<Task> {
    protected int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            this.result = tasks.deleteTask(this.index);

            storage.save(tasks);

            ui.showRegularMessage("Meow, I deleted the task '%s' for you :3"
                    .formatted(this.result.getDescription()));
        } catch (MittensException e) {
            ui.showErrorMessage(e);
        }
    }
}
