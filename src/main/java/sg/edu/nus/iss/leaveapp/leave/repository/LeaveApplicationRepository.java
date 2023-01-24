package sg.edu.nus.iss.leaveapp.leave.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.User;

@EnableJpaRepositories
@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    public List<LeaveApplication> findByUser(User user);
    public List<LeaveApplication> findByStatus(LeaveEventEnum status);
    List<LeaveApplication> findPendingAndApprovedApplicationsByUser(@Param("id") String id);
    Optional<LeaveApplication> findById(Long id);

    
}