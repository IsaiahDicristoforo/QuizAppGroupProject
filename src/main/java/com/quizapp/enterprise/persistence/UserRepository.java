package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email =:username")
    User findUserByUsername(@Param("username") String username);

}
