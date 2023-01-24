package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface LeaveBalanceService {
    LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance);
    void reduceLeave(LeaveApplication leaveApplication);
    void increaseLeave(LeaveApplication oldLeaveApplication);
    boolean deleteLeaveBalance(LeaveBalance leaveBalance);
    LeaveBalance getLeaveBalanceByUser(User user);
    List<LeaveBalance> getAllLeaveBalance();
    
}