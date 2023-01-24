package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;

@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public boolean updatePublicHoliday(PublicHoliday publicHoliday){
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<PublicHoliday> entity = new HttpEntity<PublicHoliday>(publicHoliday,headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080//api/publichol/update", HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return true;}
        else{
            return false;
        }
    }
    @Override
    public boolean saveNewPublicHoliday(PublicHoliday publicHoliday){
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<PublicHoliday> entity = new HttpEntity<PublicHoliday>(publicHoliday,headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080//api/publichol/add", HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return true;}
        else{
            return false;
        }
    }
    @Override
    public List<PublicHoliday> getPublicHolList() throws JsonMappingException, JsonProcessingException{
        HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity <String> entity = new HttpEntity<String>(headers);
		String response = restTemplate.exchange("http://localhost:8080/api/publichol", HttpMethod.GET, entity, String.class).getBody();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		List<PublicHoliday> publicHolList = mapper.readValue(response, new TypeReference<List<PublicHoliday>>(){});
        return publicHolList;
    }
    @Override
    public boolean deletePublicHoliday(PublicHoliday publicHoliday){
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<PublicHoliday> entity = new HttpEntity<PublicHoliday>(publicHoliday,headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080//api/publichol/delete", HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return true;}
        else{
            return false;
        }
    }
    
}