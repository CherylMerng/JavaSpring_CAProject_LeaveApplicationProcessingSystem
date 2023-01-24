package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;

public interface PublicHolidayService {
    boolean saveNewPublicHoliday(PublicHoliday publicHoliday);
    boolean updatePublicHoliday(PublicHoliday publicHoliday);
    boolean deletePublicHoliday(PublicHoliday publicHoliday);
    List<PublicHoliday> getPublicHolList()throws JsonMappingException, JsonProcessingException;
    
}