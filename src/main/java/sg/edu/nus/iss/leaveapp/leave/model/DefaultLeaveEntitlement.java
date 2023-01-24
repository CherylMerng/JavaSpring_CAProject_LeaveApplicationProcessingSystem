package sg.edu.nus.iss.leaveapp.leave.model;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table
public class DefaultLeaveEntitlement {
    @Id
    private String jobGrade;

    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Double annualLeave;
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private Double medicalLeave;

    public DefaultLeaveEntitlement (String jobGrade){
        this.jobGrade = jobGrade;
        if (jobGrade.equals("ISS02")){
            this.annualLeave = 18.0;
            this.medicalLeave = 60.0;
        }
        if (jobGrade.equals("ISS01")){
            this.annualLeave = 14.0;
            this.medicalLeave = 60.0;
        }
        if (jobGrade.equals("ISS03")){
            this.annualLeave = 0.0;
            this.medicalLeave = 0.0;
        }
    }

    @OneToMany(mappedBy="defaultLeaveEntitlement")
    private List<User> users;

}