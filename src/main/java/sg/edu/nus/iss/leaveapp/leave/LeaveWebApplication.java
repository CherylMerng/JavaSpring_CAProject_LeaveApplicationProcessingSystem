package sg.edu.nus.iss.leaveapp.leave;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.DefaultLeaveEntitlementService;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveApplicationService;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveBalanceService;
import sg.edu.nus.iss.leaveapp.leave.service.PublicHolidayService;
import sg.edu.nus.iss.leaveapp.leave.service.RoleService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;

@SpringBootApplication
@EnableJpaRepositories
public class LeaveWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveWebApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner commandLineRun(UserService userService, RoleService roleService, LeaveBalanceService leaveBalanceService, 
	DefaultLeaveEntitlementService defaultLeaveEntitlementService,PublicHolidayService publicHolService, LeaveApplicationService leaveAppService){
		return args -> {
			System.out.println("---- Create some roles");
			roleService.saveRole(new Role("Admin"));
			roleService.saveRole(new Role("Employee"));
			roleService.saveRole(new Role("Manager"));

			System.out.println("---- Create some users");
			User john = new User("2531", "John123@", "John Tan Meng Keng", "92887201", "shizuyl328@gmail.com", "Overall Head of ISS", "None", "ISS03");
			User sally = new User("2811", "Sally123@", "Sally Ang Jean Tee", "92888000", "shizuyl328@gmail.com", "Professor","2531", "ISS02");
			User tom = new User("1835", "Tom123@@", "Tom Lee Shin", "92887888", "shizuyl328@gmail.com","Lecturer", "2811", "ISS01");
			User jerry = new User("5833", "Jerry123@", "Jerry Heng An Tan", "92895888", "shizuyl328@gmail.com", "Lecturer","2811", "ISS01");

			DefaultLeaveEntitlement admins = new DefaultLeaveEntitlement("ISS03");
			DefaultLeaveEntitlement manage = new DefaultLeaveEntitlement("ISS02");
			DefaultLeaveEntitlement employ = new DefaultLeaveEntitlement("ISS01");
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(admins);
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(manage);
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(employ);

			//saving the users will help to set the roles.
			//john.setRoles(Arrays.asList(admin, employee, manager));
			//tom.setRoles(Arrays.asList(employee));
			//sally.setRoles(Arrays.asList(manager, employee));
			//jerry.setRoles(Arrays.asList(employee));

			//saving the users will help set the leave entitlement as well.
			//john.setDefaultLeaveEntitlement(admins);
			//tom.setDefaultLeaveEntitlement(employ);
			//sally.setDefaultLeaveEntitlement(manage);
			//jerry.setDefaultLeaveEntitlement(employ);

			userService.saveUser(john);
			userService.saveUser(sally);
			userService.saveUser(tom);
			userService.saveUser(jerry);
//since leave entitlement contains the foreign key staff_ID, it can only be set when staff is saved in the database/repository with staff ID.
			//saving the users will help set the leave balance as well.
			//LeaveBalance johnBalanceLeave = new LeaveBalance(john);
			//LeaveBalance tomBalanceLeave = new LeaveBalance(tom);
			//LeaveBalance sallyBalanceLeave = new LeaveBalance(sally);
			//LeaveBalance jerryBalanceLeave = new LeaveBalance(jerry);
			//leaveBalanceService.saveLeaveBalance(johnBalanceLeave);
			//leaveBalanceService.saveLeaveBalance(tomBalanceLeave);
			//leaveBalanceService.saveLeaveBalance(sallyBalanceLeave);
			//leaveBalanceService.saveLeaveBalance(jerryBalanceLeave);

			//johnBalanceLeave.setCompensationLeave(null);
			//sallyBalanceLeave.setCompensationLeave(2.5);
			//jerryBalanceLeave.setCompensationLeave(2.5);
			//tomBalanceLeave.setCompensationLeave(2.5);

			//LeaveType annualLeave = new LeaveType("annual_leave","1");
			//LeaveType medicalLeave = new LeaveType("medical_leave","1");
			//LeaveType compensationLeave = new LeaveType("compensation_leave","0.5");
			//leaveTypeService.saveLeaveType(annualLeave);
			//leaveTypeService.saveLeaveType(medicalLeave);
			//leaveTypeService.saveLeaveType(compensationLeave);

			//DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yy");
			//Map<String, String> dateofPublicHol = new HashMap<>(){{
				//put("01/01/23", "New Year’s Day");
				//put("02/01/23", "New Year’s Day In Lieu");
				//put("22/01/23", "Chinese New Year");
				//put("23/01/23", "Chinese New Year");
				//put("24/01/23", "Chinese New Year In Lieu");
				//put("07/04/23", "Good Friday");
				//put("22/04/23", "Hari Raya Puasa");
				//put("01/05/23", "Labour Day");
				//put("03/06/23", "Vesak Day");
				//put("29/06/23", "Hari Raya Haji");
				//put("09/08/23", "National Day");
				//put("12/11/23", "Deepavali");
				//put("13/11/23", "Deepavali In Lieu");
				//put("25/12/23", "Christmas");


			//}};
			//List<PublicHoliday> listOfPublicHol = new ArrayList<PublicHoliday>();
			//for (String date: dateofPublicHol.keySet()){
				//LocalDate localDate = LocalDate.parse(date,df1);
				//String description = dateofPublicHol.get(date);
				//listOfPublicHol.add(new PublicHoliday(localDate, description));
			//}
			//for (PublicHoliday publichol: listOfPublicHol ){
				//publicHolService.savePublicHoliday(publichol);
			//}
			LeaveApplication leaveApplication = new LeaveApplication("annual_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication.setNumberOfDays(1.0);
			leaveApplication.setDateOfApplication(LocalDate.now());
			leaveApplication.setDateOfStatus(LocalDate.now());
			leaveApplication.setStatus(LeaveEventEnum.PENDING);
			leaveApplication.setUser(jerry);
			leaveAppService.saveLeaveApplication(leaveApplication);

			tom = userService.getUserByUsername("1835");
			LeaveBalance tomLeaveBalance = leaveBalanceService.getLeaveBalanceByUser(tom);
			tomLeaveBalance.setCompensationLeave(5.0);
			leaveBalanceService.saveLeaveBalance(tomLeaveBalance);
			LeaveApplication leaveApplication2 = new LeaveApplication("compensation_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication2.setNumberOfDays(0.5);
			leaveApplication2.setHalfdayIndicator("AM");
			leaveApplication2.setDateOfApplication(LocalDate.now());
			leaveApplication2.setDateOfStatus(LocalDate.now());
			leaveApplication2.setStatus(LeaveEventEnum.PENDING);
			leaveApplication2.setUser(tom);
			leaveAppService.saveLeaveApplication(leaveApplication2);

			jerry = userService.getUserByUsername("5833");
			LeaveBalance jerryLeaveBalance = leaveBalanceService.getLeaveBalanceByUser(jerry);
			jerryLeaveBalance.setCompensationLeave(6.0);
			leaveBalanceService.saveLeaveBalance(jerryLeaveBalance);

			//LeaveApplication leaveApplication2 = new LeaveApplication((long)2811, "compensation_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			//leaveApplication.setNumberOfDays(0.5);
			//leaveApplication.setHalfdayIndicator("AM");
			//leaveApplication.setDateOfApplication(LocalDate.now());
			//leaveApplication.setDateOfStatus(LocalDate.now());
			//leaveApplication.setStatus(LeaveEventEnum.APPROVED);
			//leaveApplication.setUser(run);
			//leaveAppService.saveLeaveApplication(leaveApplication2);
			
			LeaveApplication leaveApplication4 = new LeaveApplication("annual_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication4.setNumberOfDays(1.0);
			leaveApplication4.setDateOfApplication(LocalDate.now());
			leaveApplication4.setDateOfStatus(LocalDate.now());
			leaveApplication4.setStatus(LeaveEventEnum.PENDING);
			leaveApplication4.setUser(sally);
			leaveAppService.saveLeaveApplication(leaveApplication4);

			LeaveApplication leaveApplication5 = new LeaveApplication("medical_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication5.setNumberOfDays(1.0);
			leaveApplication5.setDateOfApplication(LocalDate.now());
			leaveApplication5.setDateOfStatus(LocalDate.now());
			leaveApplication5.setStatus(LeaveEventEnum.APPROVED);
			leaveApplication5.setUser(sally);
			leaveAppService.saveLeaveApplication(leaveApplication5);
			
		
		};
		
	}

}
