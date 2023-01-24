package sg.edu.nus.iss.leaveapp.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.User;
@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    List<User> findByReportingStaffID(String reportingStaffID);

    List<User> findByJobGrade(String jobGrade);
    
}