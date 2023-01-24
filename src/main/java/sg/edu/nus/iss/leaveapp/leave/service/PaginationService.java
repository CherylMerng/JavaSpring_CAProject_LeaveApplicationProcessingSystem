package sg.edu.nus.iss.leaveapp.leave.service;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.EmpPage;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.MgrPage;
import sg.edu.nus.iss.leaveapp.leave.model.User;

import sg.edu.nus.iss.leaveapp.leave.repository.LeavePaginationRepository;
import sg.edu.nus.iss.leaveapp.leave.repository.UserRepository;

@Service
public class PaginationService {
    @Autowired
    private LeavePaginationRepository leaveRepo;

    @Autowired
    private UserRepository userRepo;

    public Map<String, Page<LeaveApplication>> getAllSubLeaveAppli(UserDetails manager, MgrPage page){
        String managerId = manager.getUsername();
        List<User> stafflist = userRepo.findByReportingStaffID(managerId);

        Map<String, Page<LeaveApplication>> result = new HashMap<>();

        int currentPage;
        int pageSize = page.getSize();
        String targetname = page.getName();
        Date startDate = null;
        Date endDate = null;

        try{
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(page.getStartDate());
        }
        catch(Exception e){}

        try{
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(page.getEndDate());
        }
        catch(Exception e){}
        
        for(User user:stafflist){
            if(targetname != null && targetname.equals(user.getFullName())){
                currentPage = page.getPage() - 1;
            }
            else {
                currentPage = 0;
                if(pageSize == 0){
                    pageSize = 2;
                }
            }

            Pageable paging = PageRequest.of(currentPage, pageSize, Sort.by("start_date").descending());
            
            Page<LeaveApplication> list = null;

            String leaveType = page.getLeaveType();

            if(leaveType.equals("all")){
                leaveType = "%";
            }
            else{
                leaveType = "%" + leaveType + "%";
            }

            if(startDate != null || endDate != null){
                list = getByDate(user.getUsername(), startDate, endDate, leaveType, paging);
            }
            else{
                System.out.println(leaveType);
                System.out.println("==================================");
                list = leaveRepo.findByUserStaffId(user.getUsername(), leaveType, paging);
            }
            
            result.put(user.getFullName(), list);
        }
        
        return result;
    }

    public Page<LeaveApplication> getByDate(String username, Date startdate, Date enddate, String leaveType, Pageable paging){
        Page<LeaveApplication> list = null;

        if(startdate != null && enddate != null){
            list = leaveRepo.findByUserStaffIdAndStartEndDate(username, leaveType, startdate, enddate, paging);
        }
        else if(startdate != null){
            list = leaveRepo.findByUserStaffIdAndStartDate(username, leaveType, startdate, paging);
        }
        else if(enddate != null){
            list = leaveRepo.findByUserStaffIdAndEndDate(username, leaveType, enddate, paging);
        }
        
        return list;
    }

    public LeaveApplication getSubLeaveAppliDetails(Long id){
        return leaveRepo.findById(id).orElse(null);
    }

    public Page<LeaveApplication> findByPage(User user,EmpPage page){
            int currentPage;
            int pageSize = page.getSize();
            Date startDate = null;
            Date endDate = null;    
            try{
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(page.getStartDate());
            }
            catch(Exception e){}
    
            try{
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(page.getEndDate());
            }
            catch(Exception e){}
            currentPage = page.getPage() - 1;
    
            Pageable paging = PageRequest.of(currentPage, pageSize, Sort.by("start_date").descending());
                
            Page<LeaveApplication> list = null;
    
            String leaveType = page.getLeaveType();
    
            if(leaveType.equals("all")){
                leaveType = "%";
            }else{
                leaveType="%"+leaveType+"%";
            }
    
            if(startDate != null || endDate != null){
                list = getByDate(user.getUsername(), startDate, endDate, leaveType, paging);
            }
            else{
                list = leaveRepo.findByUserStaffId(user.getUsername(), leaveType, paging);
            }
               
            return list;
        }
}
