package br.com.anthony.todolist.task.controller;

import br.com.anthony.todolist.task.entity.Task;
import br.com.anthony.todolist.task.repository.TaskRepository;
import br.com.anthony.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskRepository respository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();

        if(task.getStartAt().isAfter(task.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data final!");
        }
        if(currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior que a data atual!");
        }

        var newTask = this.respository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @GetMapping("/")
    public List<Task> get(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var tasks = this.respository.findByUserId((UUID) userId);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Task updateTask, @PathVariable UUID id, HttpServletRequest request){
        var task = this.respository.findById((UUID) id).orElse(null);
        var userId = request.getAttribute("userId");

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esta tarefa nao existe!!");
        }

        if(!task.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario nao tem permissao para alterar essa tarefa!!");
        }
        Utils.copyNulllProperties(updateTask, task);
        Task newTask = this.respository.save(task);
        return ResponseEntity.ok().body(newTask);
    }
}