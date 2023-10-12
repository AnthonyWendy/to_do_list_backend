package br.com.anthony.todolist.user.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.anthony.todolist.user.entity.UserModel;
import br.com.anthony.todolist.user.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserModelRepository repository;
    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserModel user){
        var userRegistred = this.repository.findByUserName(user.getUserName());

        if(userRegistred != null) {
            System.out.println("nickname já utilizado!");
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }
        var passwordHashred = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashred);
        var newUser = this.repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
