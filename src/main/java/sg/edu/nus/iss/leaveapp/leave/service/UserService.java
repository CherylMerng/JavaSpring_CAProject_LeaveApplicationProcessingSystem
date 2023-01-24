package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface UserService {

    Boolean saveUser(User user);
    
    List<User> getUserList();

    Boolean updateUser(User user) ;

    Boolean deleteUser(User user);

    User getUserByUsername(String username);

    List<User> findSubordinateByManagerID(String managerID);

    Boolean updateHierarchy(User staff);

    Boolean updateAccess(User staff);

    List<User> findUsersByJobGrade(String jobGrade);

    boolean updateDefaultLeaveEntitlement(DefaultLeaveEntitlement leave);

    boolean updateStaffLeave(LeaveBalance leaveBalance);
    
}