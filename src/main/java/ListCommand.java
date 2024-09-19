import java.util.ArrayList;
import java.util.List;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<String> messages = new ArrayList<>();
            
        int count = tasks.getCount();
        if (count == 0) {
            messages.add("Meow?! Your list is empty!");
        }
        
        for (int i = 0; i < count; i++) {
            Task task = tasks.getTask(i);
            messages.add("%d. %s".formatted(i + 1, task));
        }
            
        ui.showRegularMessage(messages);
    }
}
