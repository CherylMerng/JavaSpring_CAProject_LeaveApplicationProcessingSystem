package sg.edu.nus.iss.leaveapp.leave.validator;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import sg.edu.nus.iss.leaveapp.leave.model.MgrPage;

@Component
public class MgrPageValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		return MgrPage.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
        if(obj != null){
            MgrPage page = (MgrPage) obj;

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
            
            if(startDate != null && endDate != null){
                if(startDate.after(endDate)) {
                    errors.rejectValue("startDate", "error.startDate", "Start Date should not be before End Date");
                }
            }
        }
		
	}
}
