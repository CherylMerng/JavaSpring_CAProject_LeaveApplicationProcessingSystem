package sg.edu.nus.iss.leaveapp.leave.model;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name= "LeaveApplication")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Leave Type cannot be blank.")
    @NotBlank(message = "LeaveType cannot be blank.")
    private String leaveType;
    @NotNull(message = "Start Date cannot be blank.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotBlank(message = "Reason cannot be blank.")
    private String reason;
    private String dissemination;
    private Long contactNumber;
    private Double numberOfDays;
    private String halfdayIndicator;

    @Column(name = "status", columnDefinition = "ENUM('APPROVED', 'REJECTED', 'PENDING', 'CANCELLED', 'DELETED')")
	@Enumerated (EnumType.STRING)
	private LeaveEventEnum status;
	
	private LocalDate dateOfApplication;
	
	private LocalDate dateOfStatus;

    private String managerComment;

    public LeaveApplication(String leaveType, LocalDate startDate, LocalDate endDate, String reason, String dissemination, Long contactNumber){
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.dissemination = dissemination;
        this.contactNumber = contactNumber;
    }

    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.REMOVE)
    private User user;
    //remove leaveapplication and leave balance when user is deleted.
    
}
