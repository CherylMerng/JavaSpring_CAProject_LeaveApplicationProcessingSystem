package sg.edu.nus.iss.leaveapp.leave.service;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepo;
    @Autowired
    LeaveBalanceService leaveBalanceService;
    @Autowired
    RoleService roleService;
    @Autowired
    DefaultLeaveEntitlementService defaultLeaveService;

    public boolean checkIfReportingStaffValid(String reportingStaffID){
        User reportingStaff = getUserByUsername(reportingStaffID);
        if (reportingStaffID.equals("None")){
            return true;
        }
        else if (reportingStaff == null){
            return false;
        }
        else{

            if(reportingStaff.getJobGrade().equals("ISS02")||reportingStaff.getJobGrade().equals("ISS03")){
                return true;

            }
            else{
                return false;
            }
        }
    }
    @Override
    public Boolean updateHierarchy(User staff){
        String reportingStaffID = staff.getReportingStaffID();
        boolean reportingStaffValid = checkIfReportingStaffValid(reportingStaffID);
        if (reportingStaffValid){
            User staffRetrieved = userRepo.getUserByUsername(staff.getUsername());
            staffRetrieved.setReportingStaffID(reportingStaffID);
            userRepo.save(staffRetrieved);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Boolean updateAccess(User user){
        Role admin= roleService.findRoleByRoleName("Admin").get(0);
        Role manager= roleService.findRoleByRoleName("Manager").get(0);
        Role employee= roleService.findRoleByRoleName("Employee").get(0);
        String staffID = user.getUsername();
        List<User> subordinateList = findSubordinateByManagerID(staffID);
        User staffRetrieved = userRepo.getUserByUsername(staffID);
        if (subordinateList.size() != 0 && user.getJobGrade().equals("ISS01")){
            return false;
        }
        else{
            if (user.getJobGrade().equals("ISS03")){
                staffRetrieved.setReportingStaffID("None");
            }
            staffRetrieved.setPassword(user.getPassword());
            staffRetrieved.setJobGrade(user.getJobGrade());
            DefaultLeaveEntitlement userLeaveEntitlement = defaultLeaveService.findByJobGrade(user.getJobGrade()).get();
            staffRetrieved.setRoles(null);
            setUserRolesByJobGrade(staffRetrieved,employee, manager, admin);
            staffRetrieved.setDefaultLeaveEntitlement(userLeaveEntitlement);
            LeaveBalance retrievedUserLeaveBalance = leaveBalanceService.getLeaveBalanceByUser(staffRetrieved);
            retrievedUserLeaveBalance.setAnnualLeave(userLeaveEntitlement.getAnnualLeave());
            retrievedUserLeaveBalance.setMedicalLeave(userLeaveEntitlement.getMedicalLeave());
            leaveBalanceService.saveLeaveBalance(retrievedUserLeaveBalance);
            userRepo.save(staffRetrieved);
            return true;

        }
    }
    
    @Override
    public Boolean saveUser(User user) {
        Role admin= roleService.findRoleByRoleName("Admin").get(0);
        Role manager= roleService.findRoleByRoleName("Manager").get(0);
        Role employee= roleService.findRoleByRoleName("Employee").get(0);
        DefaultLeaveEntitlement userLeaveEntitlement = defaultLeaveService.findByJobGrade(user.getJobGrade()).get();
        user.setDefaultLeaveEntitlement(userLeaveEntitlement);
        LeaveBalance userLeaveBalance = new LeaveBalance(user);
        List<User> allStaff = getUserList();
        String reportingStaffID = user.getReportingStaffID();
        if (checkIfReportingStaffValid(reportingStaffID)){
            for (User staff: allStaff){
                if (staff.getUsername().equals(user.getUsername())){
                return false;}}
                setUserRolesByJobGrade(user,employee, manager, admin);
                userRepo.save(user);
                leaveBalanceService.saveLeaveBalance(userLeaveBalance);
                return true;}
       return false;
    }

    public void setUserRolesByJobGrade(User user, Role employee, Role manager, Role admin){
        List<Role> ISSOne = new ArrayList<Role>();
        ISSOne.add(employee);
        List<Role> ISSTwo = new ArrayList<Role>();
        ISSTwo.add(employee);
        ISSTwo.add(manager);
        List<Role> ISSThree = new ArrayList<Role>();
        ISSThree.add(employee);
        ISSThree.add(manager);
        ISSThree.add(admin);
        if (user.getJobGrade().equals("ISS03")){
            user.setRoles(ISSThree);
        }
        else if (user.getJobGrade().equals("ISS02")){
            user.setRoles(ISSTwo);
    
        }
        else {
            user.setRoles(ISSOne);
        }
    }
    @Override
    public Boolean updateUser(User user) {
        String staffID = user.getUsername();
        User oldUser = userRepo.getUserByUsername(staffID);
        oldUser.setFullName(user.getFullName());
        oldUser.setDesignation(user.getDesignation());
        oldUser.setEmail(user.getEmail());
        oldUser.setMobilePhone(user.getMobilePhone());
        userRepo.save(oldUser);
        return true;
    }

    @Override
    public List<User> getUserList() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public Boolean deleteUser(User user){
        User fullUserDetails = getUserByUsername(user.getUsername());
        String staffID = user.getUsername();
        List<User> subordinateList = findSubordinateByManagerID(staffID);
        if (subordinateList.size()== 0){
            leaveBalanceService.deleteLeaveBalance(fullUserDetails.getStaffleave());
            return true;
        }
        return false;
        //deletecascade
        //userRepo.delete(user);
    }

    @Override
    public User getUserByUsername(String username){
        return userRepo.getUserByUsername(username);
    }
    @Override
    public List<User> findSubordinateByManagerID(String managerID){
        return userRepo.findByReportingStaffID(managerID);
    }

    @Override
    public List<User> findUsersByJobGrade(String jobGrade){
        return userRepo.findByJobGrade(jobGrade);
    }

    @Override
    @Transactional
    public boolean updateDefaultLeaveEntitlement(DefaultLeaveEntitlement leaveEntitlement){
        List<User> users = findUsersByJobGrade(leaveEntitlement.getJobGrade());
        DefaultLeaveEntitlement original = defaultLeaveService.findByJobGrade(leaveEntitlement.getJobGrade()).get();
        double annualLeaveDifference = leaveEntitlement.getAnnualLeave() - original.getAnnualLeave();
        double medicalLeaveDifference = leaveEntitlement.getMedicalLeave() - original.getMedicalLeave();
        original.setAnnualLeave(leaveEntitlement.getAnnualLeave());
        original.setMedicalLeave(leaveEntitlement.getMedicalLeave());
        defaultLeaveService.saveDefaultLeaveEntitlement(original);
        for (User staff: users){
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceByUser(staff);
            Double originalAnnualLeaveBalance = staff.getStaffleave().getAnnualLeave();
            Double originalMedicalLeaveBalance = staff.getStaffleave().getMedicalLeave();
            Double finalAnnualLeaveBalance = originalAnnualLeaveBalance + annualLeaveDifference;
            Double finalMedicalLeaveBalance = originalMedicalLeaveBalance + medicalLeaveDifference;
            if (finalAnnualLeaveBalance<0){
                finalAnnualLeaveBalance = 0.0;
            }
            if (finalMedicalLeaveBalance<0){
                finalMedicalLeaveBalance = 0.0;
            }
            leaveBalance.setAnnualLeave(finalAnnualLeaveBalance);
            leaveBalance.setMedicalLeave(finalMedicalLeaveBalance);
            leaveBalanceService.saveLeaveBalance(leaveBalance);
        }
        return true;

    }

    @Override
    public boolean updateStaffLeave(LeaveBalance leaveBalance){
        String staffID = leaveBalance.getUser().getUsername();
        User staff = getUserByUsername(staffID);
        LeaveBalance original = leaveBalanceService.getLeaveBalanceByUser(staff);
        DefaultLeaveEntitlement staffleaveentitlement = defaultLeaveService.findByJobGrade(staff.getJobGrade()).get();
        if (leaveBalance.getAnnualLeave() > staffleaveentitlement.getAnnualLeave()){
            return false;
        }
        if (leaveBalance.getMedicalLeave() > staffleaveentitlement.getMedicalLeave()){
            return false;
        }
        original.setAnnualLeave(leaveBalance.getAnnualLeave());
        original.setMedicalLeave(leaveBalance.getMedicalLeave());
        original.setCompensationLeave(leaveBalance.getCompensationLeave());
        original.setOvertimeHours(leaveBalance.getOvertimeHours());
        leaveBalanceService.saveLeaveBalance(original);
        return true;
    }
}