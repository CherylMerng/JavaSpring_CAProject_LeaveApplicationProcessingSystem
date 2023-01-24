package sg.edu.nus.iss.leaveapp.leave.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.edu.nus.iss.leaveapp.leave.model.User;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return User.class.isAssignableFrom(clazz);
    }
    @Override
    public void validate(Object obj, Errors errors){
        User user = (User) obj;
        if (!user.getUsername().matches("[\\d]{4}")){
            errors.rejectValue("username", "error.username", "Staff ID must consist of only four numerical digits.");
        }
        if (!(user.getReportingStaffID().matches("[\\d]{4}")|| user.getReportingStaffID().equals("None"))){
            errors.rejectValue("reportingStaffID", "error.reportingStaffID", "Reporting Staff ID must consist of only four digits. If not applicable, Reporting Staff ID must be None");
            
        }
        if(user.getUsername().equals(user.getReportingStaffID())){
            errors.rejectValue("reportingStaffID", "error.reportingStaffID", "Reporting Staff ID must not be the same as the Staff ID.");
        }   
    }   
}