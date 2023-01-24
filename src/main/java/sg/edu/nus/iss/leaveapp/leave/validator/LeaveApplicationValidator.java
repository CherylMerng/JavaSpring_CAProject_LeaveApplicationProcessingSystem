package sg.edu.nus.iss.leaveapp.leave.validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;


@Component
public class LeaveApplicationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return LeaveApplication.class.isAssignableFrom(clazz);
    }
    @Override
    public void validate(Object obj, Errors errors){
        LeaveApplication leaveApplication = (LeaveApplication) obj;
        if (leaveApplication.getLeaveType()!= null){
            if (!(leaveApplication.getLeaveType().equals("compensation_leave") && leaveApplication.getHalfdayIndicator() != null)){
                if(leaveApplication.getEndDate() ==null){
                    errors.rejectValue("endDate","errors.dates", "End Date cannot be blank.");
                }
    
            }

        }
        if ((leaveApplication.getStartDate()!=null && leaveApplication.getEndDate()!=null) && (leaveApplication.getStartDate().compareTo(leaveApplication.getEndDate())>0)){
            errors.rejectValue("endDate", "error.dates", "End Date must be later than Start Date");
        }
        if (leaveApplication.getStartDate()!=null && leaveApplication.getStartDate().compareTo(LocalDate.now())<0){
            errors.rejectValue("startDate", "error.dates", "Start Date must be later than Today");
        }
        if (leaveApplication.getEndDate()!=null && leaveApplication.getEndDate().compareTo(LocalDate.now())<0){
            errors.rejectValue("endDate", "error.dates", "End Date must be later than Today");

        }   
    }   
}