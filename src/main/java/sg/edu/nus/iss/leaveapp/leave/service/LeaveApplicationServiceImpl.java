package sg.edu.nus.iss.leaveapp.leave.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.repository.LeaveApplicationRepository;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    LeaveApplicationRepository leaveApplicationRepo;
    @Autowired
    LeaveBalanceService leaveBalanceService;
    @Autowired
    PublicHolidayService publicHolService;

    @Override
    @Transactional
    public boolean saveLeaveApplication(LeaveApplication leaveApplication){
        leaveApplicationRepo.save(leaveApplication);
        leaveBalanceService.reduceLeave(leaveApplication);

        return true;

    }
    @Override
    public void approveLeaveApplication(LeaveApplication leaveApplication){
        leaveApplicationRepo.save(leaveApplication);

    }
    @Override
    @Transactional
    public boolean rejectLeaveApplication(LeaveApplication leaveApplication){
        leaveApplicationRepo.save(leaveApplication);
        leaveBalanceService.increaseLeave(leaveApplication);

        return true;
    }
    
    @Override
    public void UpdateCompensationLeaveApplication(LeaveApplication oldLeaveApplication, LeaveApplication newLeaveApplication){
        oldLeaveApplication.setContactNumber(newLeaveApplication.getContactNumber());
        oldLeaveApplication.setDateOfApplication(newLeaveApplication.getDateOfApplication());
        oldLeaveApplication.setDateOfStatus(newLeaveApplication.getDateOfStatus());
        oldLeaveApplication.setDissemination(newLeaveApplication.getDissemination());
        oldLeaveApplication.setStartDate(newLeaveApplication.getStartDate());
        oldLeaveApplication.setEndDate(newLeaveApplication.getEndDate());
        oldLeaveApplication.setHalfdayIndicator(newLeaveApplication.getHalfdayIndicator());
        oldLeaveApplication.setReason(newLeaveApplication.getReason());
        leaveApplicationRepo.save(oldLeaveApplication);
    }

    @Override
    @Transactional
    public boolean saveUpdateLeaveApplication(LeaveApplication oldLeaveApplication, LeaveApplication newLeaveApplication){
        leaveBalanceService.increaseLeave(oldLeaveApplication);
        leaveBalanceService.reduceLeave(newLeaveApplication);
        oldLeaveApplication.setContactNumber(newLeaveApplication.getContactNumber());
        oldLeaveApplication.setDateOfApplication(newLeaveApplication.getDateOfApplication());
        oldLeaveApplication.setDateOfStatus(newLeaveApplication.getDateOfStatus());
        oldLeaveApplication.setDissemination(newLeaveApplication.getDissemination());
        oldLeaveApplication.setStartDate(newLeaveApplication.getStartDate());
        oldLeaveApplication.setEndDate(newLeaveApplication.getEndDate());
        oldLeaveApplication.setHalfdayIndicator(newLeaveApplication.getHalfdayIndicator());
        oldLeaveApplication.setLeaveType(newLeaveApplication.getLeaveType());
        oldLeaveApplication.setNumberOfDays(newLeaveApplication.getNumberOfDays());
        oldLeaveApplication.setReason(newLeaveApplication.getReason());
        leaveApplicationRepo.save(oldLeaveApplication);
        return true;

    }

    @Override
    public boolean checkIfWorkingDay(LocalDate startDate) throws JsonMappingException, JsonProcessingException{
        if ((startDate.getDayOfWeek() == DayOfWeek.SATURDAY)|| (startDate.getDayOfWeek() == DayOfWeek.SUNDAY)){
            return false;
        }
        List<PublicHoliday> publicHol = publicHolService.getPublicHolList();
        for(PublicHoliday hol: publicHol){
            if (hol.getDateOfPublicHol().equals(startDate)){
                return false;
            }
        }
        return true;
  
    }

    @Override
    public boolean checkIfLeaveAppliedExceedBalance(double daysBetween, String leaveType, User user){
        LeaveBalance leaveBalance = user.getStaffleave();
        if (leaveType.equals("annual_leave") && daysBetween > leaveBalance.getAnnualLeave()){
                return true;
            }
        else if (leaveType.equals("medical_leave") && daysBetween > leaveBalance.getMedicalLeave()){
            return true;
        }
        else if (leaveType.equals("compensation_leave") && daysBetween > leaveBalance.getCompensationLeave()){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public String generateErrorMessage(boolean startDatePublicHol,boolean endDatePublicHol,boolean leaveAppliedExceedBalance, boolean overlap){
        String errorMessage = "";
        if (!startDatePublicHol){
            errorMessage += "Start Date must be a working day.";
        }
        if (!endDatePublicHol){
            errorMessage += "\nEnd Date must be a working day.";
        }
        if (leaveAppliedExceedBalance){
            errorMessage += "You have insufficient leave for this leave application.";
        }
        if (overlap){
            errorMessage += "This leave period is invalid as it overlaps with your approved/pending leave applications.";
        }
        if (!errorMessage.isEmpty()){
            errorMessage += "\nPlease return to your leave application page to edit the respective fields before review and submission.";
        }
        return errorMessage;
    }
    @Override
    public long checkNumberOfDaysOfLeave(LocalDate startDate, LocalDate endDate) throws JsonMappingException, JsonProcessingException{
        long daysBetween = 1 + ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 14){
            return daysBetween;
        }
        else{
            daysBetween = 0;
            endDate = endDate.plusDays(1);
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
                if(checkIfWorkingDay(date)){
                    daysBetween += 1;}
            }
            return daysBetween;
        }
    }
    @Override
    public List<LeaveApplication> getLeaveApplicationList(){
        return leaveApplicationRepo.findAll();
    }

    @Override
    public boolean checkIfOverlapLeave(LocalDate startDate, LocalDate endDate,User user,Long ID){
        List<LeaveApplication> userApplications = leaveApplicationRepo.findByUser(user);
        List<LeaveApplication> userPendingAndApprApplications = new ArrayList<LeaveApplication>();
        for (LeaveApplication leave: userApplications){
            if(leave.getStatus() == LeaveEventEnum.PENDING || leave.getStatus() == LeaveEventEnum.APPROVED ){
                if(leave.getId() != ID){
                    userPendingAndApprApplications.add(leave);
                }
            }
        }
        for (LeaveApplication leave: userPendingAndApprApplications){
            if (!(endDate.isBefore(leave.getStartDate()) || startDate.isAfter(leave.getEndDate()))) {
				return true;
			}
        }
        return false;
    }
    @Override
    public boolean checkIfHalfDayOverlap(LocalDate Date, String halfdayIndicator, User user, Long ID){
        List<LeaveApplication> userApplications = findLeaveApplicationByUser(user);
        List<LeaveApplication> userPendingAndApprFullDayApplications = new ArrayList<LeaveApplication>();
        List<LeaveApplication> userPendingAndApprHalfDayApplications = new ArrayList<LeaveApplication>();
        for (LeaveApplication leave: userApplications){
            if(leave.getStatus() == LeaveEventEnum.PENDING || leave.getStatus() == LeaveEventEnum.APPROVED ){
                if(leave.getHalfdayIndicator()== null){
                    if(leave.getId() != ID){
                        userPendingAndApprFullDayApplications.add(leave);
                    }
                }
                else{
                    if(leave.getId() != ID){
                        userPendingAndApprHalfDayApplications.add(leave);
                    }
                }
            }
        }
        for (LeaveApplication leave: userPendingAndApprFullDayApplications){
            if (!(Date.isBefore(leave.getStartDate()) || Date.isAfter(leave.getEndDate()))) {
				return true;
			}
        }
        for (LeaveApplication leave: userPendingAndApprHalfDayApplications){
            if (Date.isEqual(leave.getStartDate()) && halfdayIndicator.equals(leave.getHalfdayIndicator()) ) {
				return true;
			}
        }
        return false;
    }

    @Override
    public List<LeaveApplication> findLeaveApplicationByUser(User user){
        return leaveApplicationRepo.findByUser(user);

    }
    @Override
    public Optional<LeaveApplication> findLeaveApplicationById(Long id){
        return leaveApplicationRepo.findById(id);
    }
    @Override
    @Transactional
    public void DeleteLeaveApplication(LeaveApplication leaveApplication){
        leaveBalanceService.increaseLeave(leaveApplication);
        if (leaveApplication.getStatus() == LeaveEventEnum.PENDING){
            leaveApplication.setStatus(LeaveEventEnum.DELETED);
        }
        if (leaveApplication.getStatus() == LeaveEventEnum.APPROVED){
            leaveApplication.setStatus(LeaveEventEnum.CANCELLED);
        }
        leaveApplicationRepo.save(leaveApplication);
    }

    public List<LeaveApplication> findOverlappingSubordinateLeave(LeaveApplication leaveApplication, List<User> subordinateList){
        List<LeaveApplication> colleaguesLeaveApplications = new ArrayList<LeaveApplication>();
        List<LeaveApplication> colleaguesOverlappingLeaveApplications = new ArrayList<LeaveApplication>();
        for (User subordinate: subordinateList){
            if (!(leaveApplication.getUser().getUsername().equals(subordinate.getUsername()))){
                colleaguesLeaveApplications.addAll(findLeaveApplicationByUser(subordinate));
            }
		}
        for (LeaveApplication leave: colleaguesLeaveApplications){
            if (!(leaveApplication.getEndDate().isBefore(leave.getStartDate()) ||leaveApplication.getStartDate().isAfter(leave.getEndDate()))) {
				colleaguesOverlappingLeaveApplications.add(leave);
			}
        }
        return colleaguesOverlappingLeaveApplications;
    }
}