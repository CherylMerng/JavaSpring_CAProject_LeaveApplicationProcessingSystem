package sg.edu.nus.iss.leaveapp.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.Role;

@EnableJpaRepositories
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRoleName(String roleName);
    
}