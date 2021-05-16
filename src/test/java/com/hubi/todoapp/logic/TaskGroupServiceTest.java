package com.hubi.todoapp.logic;

import com.hubi.todoapp.model.TaskGroup;
import com.hubi.todoapp.model.repository.TaskGroupRepository;
import com.hubi.todoapp.model.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        //system under test SUT
        var SUT = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> SUT.toggleGroup(0));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        TaskGroupRepository repository = mock(TaskGroupRepository.class);
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test SUT
        var SUT = new TaskGroupService(repository, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> SUT.toggleGroup(0));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should toggle group ")
    void toggleGroup_worksAsExpected() {
        //given
        var group = new TaskGroup();
        var beforeToggle = group.isDone();

        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        TaskGroupRepository repository = mock(TaskGroupRepository.class);
        when(repository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test SUT
        var SUT = new TaskGroupService(repository, mockTaskRepository);
        //when
        SUT.toggleGroup(0);
        //then
        assertThat(group.isDone()).isNotEqualTo(beforeToggle);
    }

}