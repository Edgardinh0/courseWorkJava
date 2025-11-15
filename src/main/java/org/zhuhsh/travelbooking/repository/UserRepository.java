package org.zhuhsh.travelbooking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zhuhsh.travelbooking.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
