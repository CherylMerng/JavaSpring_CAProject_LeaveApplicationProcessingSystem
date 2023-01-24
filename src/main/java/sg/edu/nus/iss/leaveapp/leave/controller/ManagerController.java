package sg.edu.nus.iss.leaveapp.leave.controller;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import sg.edu.nus.iss.leaveapp.leave.exception.ResourceNotFoundException;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.MgrPage;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveApplicationService;
import sg.edu.nus.iss.leaveapp.leave.service.PaginationService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;
import sg.edu.nus.iss.leaveapp.leave.util.SendEmail;
import sg.edu.nus.iss.leaveapp.leave.validator.MgrPageValidator;

@Controller
@RequestMapping("mgr")

public class ManagerController {

	@Autowired
    private LeaveApplicationService leaveApplicationService;
	@Autowired
	private UserService userService;
	@Autowired
	private PaginationService pageService;

	@Autowired
    private SendEmail send;

	@Autowired
  	private MgrPageValidator pagevalid;

	@InitBinder("pagevalid")
	private void initPageBinder(WebDataBinder binder) {
		binder.addValidators(pagevalid);
	}
	
	@GetMapping("/mgrviewleave")
	public String getAllSubordinateLeavePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		MgrPage page = new MgrPage();
		page.setSize(2);
		page.setLeaveType("all");

		Map<String, Page<LeaveApplication>> list = pageService.getAllSubLeaveAppli(userDetails, page);

		page.setPage(1);
		
		model.addAttribute("list", list);
		model.addAttribute("mgrpage", new MgrPage());
		model.addAttribute("currentPage", page.getPage());
		model.addAttribute("size", page.getSize());
		model.addAttribute("leaveType", page.getLeaveType());
		
		return "mgrviewleave";
	}

	@PostMapping("/mgrviewleave")
	public String getAllLeavesPage(@AuthenticationPrincipal UserDetails userDetails, @Valid @ModelAttribute("mgrpage") MgrPage page, BindingResult br, Model model){
		if(br.hasErrors()) {
			return "redirect:/mgr/mgrviewleave";
		}
		
		Map<String, Page<LeaveApplication>> list = pageService.getAllSubLeaveAppli(userDetails, page);

		int currentPage = page.getPage();
		int currentSize = page.getSize();

		if(list.get(page.getName()) != null){
			if(currentPage > list.get(page.getName()).getTotalPages()){
				page.setPage(list.get(page.getName()).getTotalPages());
			}
		}
		
		model.addAttribute("list", list);
		model.addAttribute("mgrpage", new MgrPage());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("size", currentSize);
		model.addAttribute("targetStaff", page.getName());
		model.addAttribute("startDate", page.getStartDate());
		model.addAttribute("endDate", page.getEndDate());
		model.addAttribute("leaveType", page.getLeaveType());
		
		return "mgrviewleave";
	}

	@GetMapping("/mgrviewleaveforapproval")
	public String getAllSubordinateLeaveforApprovalPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String ManagerID = userDetails.getUsername();
		User user = userService.getUserByUsername(ManagerID);
		model.addAttribute("fullName", user.getFullName());
		List<LeaveApplication> allSubordinateLeaveApplication = getAllSubordinateLeave(userDetails);
		List<LeaveApplication> allSubordinatePendingLeaveApplication = new ArrayList<LeaveApplication>();
		for (LeaveApplication leaveApplication: allSubordinateLeaveApplication){
				if(leaveApplication.getStatus() == LeaveEventEnum.PENDING){
					allSubordinatePendingLeaveApplication.add(leaveApplication);
				}
			}
		if (allSubordinatePendingLeaveApplication.isEmpty()){
			model.addAttribute("leaveApplicationList", null);
		}
		else{
			model.addAttribute("leaveApplicationList", allSubordinatePendingLeaveApplication);
		}
		return "mgrviewpending";
	}

	public List<LeaveApplication> getAllSubordinateLeave(@AuthenticationPrincipal UserDetails userDetails){
		String ManagerID = userDetails.getUsername();
		List<User> subordinateList = userService.findSubordinateByManagerID(ManagerID);
		List<LeaveApplication> leaveApplications = new ArrayList<LeaveApplication>();
		for (User subordinate: subordinateList){
			leaveApplications.addAll(leaveApplicationService.findLeaveApplicationByUser(subordinate));
		}
		return leaveApplications;
	}

	@GetMapping("/decideleaveoutcome")
    public String decideleaveoutcome(@AuthenticationPrincipal UserDetails userDetails,@RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
		String ManagerID = userDetails.getUsername();
		User user = userService.getUserByUsername(ManagerID);
		model.addAttribute("fullName", user.getFullName());
		List<User> subordinateList = userService.findSubordinateByManagerID(ManagerID);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
		List<LeaveApplication> overlappingLeaveApplications = new ArrayList<LeaveApplication>();
        if (leaveApplication.isPresent()){
            model.addAttribute("leaveApplication", leaveApplication.get());
			overlappingLeaveApplications = leaveApplicationService.findOverlappingSubordinateLeave(leaveApplication.get(), subordinateList);
			model.addAttribute("overlapLeave", overlappingLeaveApplications);
			model.addAttribute("view", "decide");
			model.addAttribute("message", null);
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        return "decideleaveoutcome";
    }

	@GetMapping("/viewempspecificleave")
	public String viewleavedetails(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
		String ManagerID = userDetails.getUsername();
		User user = userService.getUserByUsername(ManagerID);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (leaveApplication.isPresent()){
			model.addAttribute("fullName", user.getFullName());
            model.addAttribute("leaveApplication", leaveApplication.get());
			model.addAttribute("view", "view");
			model.addAttribute("message", null);
			
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        return "decideleaveoutcome";
    }

	@PostMapping("/finaliseoutcome")
	public String finaliseOutcome(@AuthenticationPrincipal UserDetails userDetails,@RequestParam("id") String id, @RequestParam("finalstatus") String finalstatus,
	@RequestParam("managerComment") String managerComment, Model model){
		String ManagerID = userDetails.getUsername();
		User user = userService.getUserByUsername(ManagerID);
		List<User> subordinateList = userService.findSubordinateByManagerID(ManagerID);
		LeaveApplication leaveApplication = leaveApplicationService.findLeaveApplicationById((Long.parseLong(id))).get();
		List<LeaveApplication> overlappingLeaveApplications = leaveApplicationService.findOverlappingSubordinateLeave(leaveApplication, subordinateList);
		leaveApplication.setUser(leaveApplication.getUser());
		model.addAttribute("fullName", user.getFullName());
		if (finalstatus.equals("NONE")){
			model.addAttribute("leaveApplication",leaveApplication);
			model.addAttribute("overlapLeave", overlappingLeaveApplications);
			model.addAttribute("view", "decide");
			model.addAttribute("message", "Leave outcome cannot be empty. Please click approve or reject and try again.");
			return "decideleaveoutcome";}
		if (finalstatus.equals("REJECTED") && managerComment.isEmpty()){
			model.addAttribute("leaveApplication",leaveApplication);
			model.addAttribute("overlapLeave", overlappingLeaveApplications);
			model.addAttribute("view", "decide");
			model.addAttribute("message", "NOTE: Comment have to be filled in when rejecting leave applications. Please try again.");
			return "decideleaveoutcome";}

		else{
			if (finalstatus.equals("REJECTED")){
				leaveApplication.setStatus(LeaveEventEnum.REJECTED);
				leaveApplication.setManagerComment(managerComment);
				leaveApplicationService.rejectLeaveApplication(leaveApplication);
				model.addAttribute("info", "After Rejection");
			}
			else{
				leaveApplication.setStatus(LeaveEventEnum.APPROVED);
				leaveApplication.setManagerComment(managerComment);
				leaveApplicationService.approveLeaveApplication(leaveApplication);
				model.addAttribute("info", "After Approval.");
			}
			model.addAttribute("more", "Please do not refresh this page as it will result in incorrect tallying of the leave.");
			model.addAttribute("leaveApplication",leaveApplication);
			model.addAttribute("message",null);
			model.addAttribute("view", "view");

			send.sendProcessedMessage(send.address, leaveApplication.getUser().getEmail(), "Leave Application Processed", leaveApplication);
			
			return "decideleaveoutcome";
		}
	}
}