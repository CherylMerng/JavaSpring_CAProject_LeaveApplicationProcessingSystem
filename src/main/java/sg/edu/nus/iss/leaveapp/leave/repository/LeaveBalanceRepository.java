package sg.edu.nus.iss.leaveapp.leave.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.User;

@EnableJpaRepositories
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    public LeaveBalance findByUser(User user);
}