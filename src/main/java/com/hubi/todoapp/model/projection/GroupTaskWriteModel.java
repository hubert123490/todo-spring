package com.hubi.todoapp.model.projection;

import com.hubi.todoapp.model.Task;
import com.hubi.todoapp.model.TaskGroup;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
public class GroupTaskWriteModel {
    @NotBlank(message = "Task's description cannot be null!")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(final TaskGroup group){
        return new Task(description, deadline, group);
    }
}
