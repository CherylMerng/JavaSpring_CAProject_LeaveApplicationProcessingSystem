package sg.edu.nus.iss.leaveapp.leave.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Double annualLeave;
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Double medicalLeave;
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Double compensationLeave;
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Long overtimeHours;

    public LeaveBalance(User user){
        DefaultLeaveEntitlement defaultLeaveEntitlement = user.getDefaultLeaveEntitlement();
        this.annualLeave = defaultLeaveEntitlement.getAnnualLeave();
        this.medicalLeave = defaultLeaveEntitlement.getMedicalLeave();
        this.compensationLeave = 0.0;
        this.overtimeHours = (long) (0);
        this.user = user;

    }

    @OneToOne (cascade=CascadeType.REMOVE, orphanRemoval=true)
    private User user;

    @Override
    public String toString(){
        return "";
    }

}