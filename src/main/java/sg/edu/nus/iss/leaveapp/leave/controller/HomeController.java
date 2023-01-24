package sg.edu.nus.iss.leaveapp.leave.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.DefaultLeaveEntitlementService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;

import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	@Autowired
	private DefaultLeaveEntitlementService defaultLeaveEntitlementService;

	@GetMapping({"/","/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
	public String getWelcomePage(@AuthenticationPrincipal UserDetails userDetails,
	Model model,HttpSession sessionObj) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName", user.getFullName());
		model.addAttribute("designation", user.getDesignation());
		return "welcomePage";
	}
    
	@GetMapping("/admin")
	public String getAdminPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName", user.getFullName());
		return "adminPage";
	}
	
	@GetMapping("/emp")
	public String getEmployeePage(@AuthenticationPrincipal UserDetails userDetails,
	Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		String jobGrade = user.getJobGrade();
		DefaultLeaveEntitlement defaultLeave = defaultLeaveEntitlementService.findByJobGrade(jobGrade).get();
		model.addAttribute("fullName", user.getFullName());
		model.addAttribute("medicalleave", defaultLeave.getMedicalLeave());
		model.addAttribute("annualleave", defaultLeave.getAnnualLeave());
		model.addAttribute("staffleave", user.getStaffleave());
		return "empPage";
	}

	@GetMapping("/mgr")
	public String getManagerPage(@AuthenticationPrincipal UserDetails userDetails,
	Model model) {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName", user.getFullName());
		return "mgrPage";
	}
	
    @GetMapping("/accessDenied")
	public String getAccessDeniedPage() {
		return "accessDeniedPage";
	}
}