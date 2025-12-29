package grupoB.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupoB.erp.domain.*;
import java.util.List;


import grupoB.erp.dao.TaskDAO;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;


    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public Task saveTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(String id) {
        return taskDAO.findByName(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return (List<Task>) taskDAO.findAll();
    }

    @Override
    public void delete(Task task) {
        taskDAO.delete(task);
    }
}

