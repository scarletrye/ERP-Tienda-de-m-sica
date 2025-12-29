package grupoB.erp.service;

import grupoB.erp.domain.Task;
import java.util.*;

public interface TaskService {
    public List<Task> getAllTasks();
    public Task findTaskById(String id);
    public Task saveTask(Task task);
    public void delete(Task task);
}
