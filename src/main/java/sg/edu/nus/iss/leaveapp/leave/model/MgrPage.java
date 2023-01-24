package sg.edu.nus.iss.leaveapp.leave.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MgrPage {
    private String name;
    
    private int page;
    private int size;

    private String startDate;
    private String endDate;
    private String leaveType;
}