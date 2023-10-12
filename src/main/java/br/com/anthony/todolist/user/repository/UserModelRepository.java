package br.com.anthony.todolist.user.repository;

import br.com.anthony.todolist.user.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUserName(String name);
}
