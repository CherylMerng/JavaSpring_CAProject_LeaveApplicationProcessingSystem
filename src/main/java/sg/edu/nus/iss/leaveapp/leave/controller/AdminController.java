package sg.edu.nus.iss.leaveapp.leave.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.DefaultLeaveEntitlementService;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveBalanceService;
import sg.edu.nus.iss.leaveapp.leave.service.PublicHolidayService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;
import sg.edu.nus.iss.leaveapp.leave.validator.UserValidator;

@Controller
@RequestMapping("adm")
public class AdminController {
	@Autowired
	private UserService userService;
	@Autowired
	private PublicHolidayService publicHolService;
	@Autowired
	private LeaveBalanceService leaveBalanceService;
	@Autowired
	private DefaultLeaveEntitlementService defaultLeaveEntitlementService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
    private UserValidator userValidator;
    @InitBinder("staff")
    private void initLeaveApplicationBinder(WebDataBinder binder){
        binder.addValidators(userValidator);
    }

	@GetMapping("/managestaff")
	public String manageStaff(@AuthenticationPrincipal UserDetails userDetails,Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
        List<User> users = userService.getUserList();
		users = users.stream().sorted((p1,p2)-> p1.getReportingStaffID().compareTo(p2.getReportingStaffID())).collect(Collectors.toList());
        model.addAttribute("staffs", users);
		model.addAttribute("staffID", staffID);
		return "managestaff";
	}
    @GetMapping("/managestaffleave")
	public String manageStaffLeave(@AuthenticationPrincipal UserDetails userDetails,Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
        List<LeaveBalance> staffLeaveBalance = leaveBalanceService.getAllLeaveBalance();
		staffLeaveBalance = staffLeaveBalance.stream().sorted((p1,p2)-> p1.getUser().getUsername().compareTo(p2.getUser().getUsername())).collect(Collectors.toList());
        model.addAttribute("staffleavebalance", staffLeaveBalance);
		return "managestaffleave";
	}

	@PostMapping("/updatestaffleave")
	public String updateStaffLeave(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="leavebalance")LeaveBalance leaveBalance, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("leavebalance", leaveBalance);
		model.addAttribute("error","");
		return "editstaffleave";
	}

	@PostMapping("/submitupdatestaffleave")
	public String submitUpdateStaffLeave (@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="leavebalance")LeaveBalance leaveBalance, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			return "editstaffleave";
		}
		else{
			boolean success = userService.updateStaffLeave(leaveBalance);
			if (success){
				model.addAttribute("leavebalance", leaveBalance);
				model.addAttribute("error","");
				model.addAttribute("action", "successful");
				return "submitstaffleave";
			}
			else{
				model.addAttribute("error", "Error(s): The annual and/or medical leave balance entered is/are greater than the staff leave entitlement. Please rectify and try again.");
				model.addAttribute("action", "unsuccessful");
				return "submitstaffleave";
			}
		}
	
	}

    @GetMapping("/managecalendar")
	public String viewCalendar(@AuthenticationPrincipal UserDetails userDetails, Model model) throws JsonMappingException, JsonProcessingException {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		//Consuming API-Public Holiday data
		List<PublicHoliday> publicHolList = publicHolService.getPublicHolList();
		publicHolList = publicHolList.stream().sorted((p1,p2)-> p1.getDateOfPublicHol().compareTo(p2.getDateOfPublicHol())).collect(Collectors.toList());
		model.addAttribute("publicHol", publicHolList);
		return "managecalendar";
	}

	@PostMapping("/publichol/update")
	public String updatePublicHol(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="hol")PublicHoliday publicHol, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("action","edit");
		model.addAttribute("publicHol", publicHol);
		model.addAttribute("error","");
		return "editcalendar";
	}

	@PostMapping("/updatepublichol")
	public String submitUpdatePublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="publicHol")PublicHoliday publicHol, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","edit");
			model.addAttribute("error","");
			return "editcalendar";}
		boolean success = publicHolService.updatePublicHoliday(publicHol);
		if (success){
			model.addAttribute("publicHol", publicHol);
			model.addAttribute("action", "update");
			return "submitpublichol";
		}
		else{
			throw new Exception("InternalServerError");
		}
	}

	@PostMapping("/publichol/delete")
	public String deletePublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@ModelAttribute(value="hol")PublicHoliday publicHol, Model model) throws Exception{
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		boolean success = publicHolService.deletePublicHoliday(publicHol);
		if (success){
			model.addAttribute("publicHol", publicHol);
			model.addAttribute("action", "delete");
			return "submitpublichol";
		}
		else{
			throw new Exception("InternalServerError");
		}
	}

	@GetMapping("/addpublichol")
	public String addPublicHol(@AuthenticationPrincipal UserDetails userDetails, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("publicHol", new PublicHoliday());
		model.addAttribute("action","add");
		model.addAttribute("error","");
		return "editcalendar";
	}

	@PostMapping("/addpublichol")
	public String submitAddPublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="publicHol")PublicHoliday publicHol, BindingResult bindingResult, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","add");
			model.addAttribute("error","");
			return "editcalendar";
		}
		boolean success = publicHolService.saveNewPublicHoliday(publicHol);
		if (success){
			model.addAttribute("publicHol", publicHol);
			model.addAttribute("action", "add");
			return "submitpublichol";
		}
		else{
			model.addAttribute("error", "This date overlap with an existing public holiday in the records. Please select another date and try again.");
			model.addAttribute("action", "add");
			return "editcalendar";

		}
	}

	@PostMapping("/updatestaff")
	public String updatestaff(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="staff")User staff, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("action","edit");
		model.addAttribute("staff",staff);
		model.addAttribute("error","");
		return "editstaff";
	}

	@PostMapping("/submitupdatestaff")
	public String submitUpdateStaff (@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="staff")User staff, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","edit");
			model.addAttribute("error","");
			return "editstaff";
		}
		else{
			boolean success = userService.updateUser(staff);
			if (success){
				model.addAttribute("staff", staff);
				model.addAttribute("action", "update");
				return "submitstaff";
			}
			else{
				throw new Exception("InternalServerError");
			}
		}
	}

	@PostMapping("/deletestaff")
	public String deleteStaff(@AuthenticationPrincipal UserDetails userDetails,
	@ModelAttribute(value="staff")User staff, Model model) throws Exception{
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		boolean success = userService.deleteUser(staff);
		if (success){
			model.addAttribute("staff", staff);
			model.addAttribute("action", "delete");
			return "submitstaff";
		}
		else{
			model.addAttribute("staff", staff);
			model.addAttribute("action", "unsuccessful");
			return "submitstaff";
		}
	}

	@GetMapping("/addstaff")
	public String addstaff(@AuthenticationPrincipal UserDetails userDetails, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("staff", new User());
		model.addAttribute("action","add");
		model.addAttribute("error","");
		return "editstaff";
	}

	@PostMapping("/submitaddstaff")
	public String submitAddPublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="staff")User staff, BindingResult bindingResult, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","add");
			model.addAttribute("error","");
			return "editstaff";
		}
		boolean success = userService.saveUser(staff);
		if (success){
			model.addAttribute("staff", staff);
			model.addAttribute("action", "add");
			return "submitstaff";
		}
		else{
			model.addAttribute("error", "Error(s): This staff ID overlaps with an existing staff ID or this reporting staff does not exist or the reporting staff does not have manager access rights. Please rectify and try again.");
			model.addAttribute("action", "add");
			return "editstaff";
		}
	}

    @GetMapping("/manageleavetypes")
	public String manageLeaveTypes(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		List<DefaultLeaveEntitlement> leaveTypes = defaultLeaveEntitlementService.getAll();
		model.addAttribute("leavetypes", leaveTypes);
		return "manageleavetypes";
	}

	@PostMapping("/updateleavetypes")
	public String updateLeaveTypes(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="leave")DefaultLeaveEntitlement leave, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("leave",leave);
		return "editleavetypes";
	}

	@PostMapping("/submitupdateleavetypes")
	public String submitUpdateLeaveTypes(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="leave")DefaultLeaveEntitlement leave, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			return "editleavetypes";}
		else{
			boolean success = userService.updateDefaultLeaveEntitlement(leave) ;
			if (success){
				model.addAttribute("leave", leave);
				return "submitleavetypes";
			}
			else{
				throw new Exception("InternalServerError");
			}
		}
	}

	@GetMapping("/managehierarchy")
	public String manageHierarchy(@AuthenticationPrincipal UserDetails userDetails,Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
        List<User> users = userService.getUserList();
		users = users.stream().sorted((p1,p2)-> p1.getReportingStaffID().compareTo(p2.getReportingStaffID())).collect(Collectors.toList());
        model.addAttribute("staffs", users);
		return "managehierarchy";
	}

	@PostMapping("/updatehierarchy")
	public String updateHierarchy(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="staff")User staff, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("staff",staff);
		model.addAttribute("error","");
		return "edithierarchy";
	}

	@PostMapping("/submitupdatehierarchy")
	public String submitUpdateHierarchy (@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="staff")User staff, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("error","");
			return "edithierarchy";}
		else{
			boolean success = userService.updateHierarchy(staff);
			if (success){
				model.addAttribute("staff", staff);
				model.addAttribute("action", "updatehierarchy");
				return "submitstaff";
			}
			else{
				model.addAttribute("error", "Error(s): This reporting staff does not exist or the reporting staff does not have manager access rights. Please rectify and try again.");
				return "edithierarchy";
			}
		}
	}

	@GetMapping("/manageaccess")
	public String manageAccess(@AuthenticationPrincipal UserDetails userDetails,Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
        List<User> users = userService.getUserList();
		users = users.stream().sorted((p1,p2)-> p1.getUsername().compareTo(p2.getUsername())).collect(Collectors.toList());
        model.addAttribute("staffs", users);
		return "manageaccess";
	}

	@PostMapping("/updateaccess")
	public String updateAccess(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="staff")User staff, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("staff", staff);
		model.addAttribute("error","");
		return "editaccess";
	}

	@PostMapping("/submitupdateaccess")
	public String submitUpdateAccess (@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="staff")User staff, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("error","");
			return "editaccess";}
		else{
			boolean success = userService.updateAccess(staff);
			if (success){
				model.addAttribute("staff", staff);
				return "submitaccess";
			}
			else{
				model.addAttribute("error", "Error(s): This reporting staff have subordinates and require at least manager rights (Job Grade of ISS02). If intended, please rectify by managing the approval hierarchy and try again.");
				return "editaccess";
			}
		}
	}
}