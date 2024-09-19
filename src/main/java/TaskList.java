import java.util.ArrayList;
import java.util.List;

public class TaskList {
    protected final ArrayList<Task> tasks;
    
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }
    
    public int getCount() {
        return this.tasks.size();
    }
    
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    
    public Task markTaskAsDone(int index) {
        Task task = this.tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task markTaskAsNotDone(int index) {
        Task task = this.tasks.get(index);
        task.markAsNotDone();
        return task;
    }
    
    public Task deleteTask(int index) {
        return this.tasks.remove(index);
    }
}
