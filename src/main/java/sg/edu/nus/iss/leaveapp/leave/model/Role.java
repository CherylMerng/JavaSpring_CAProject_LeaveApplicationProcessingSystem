package sg.edu.nus.iss.leaveapp.leave.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name= "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name= "roleId")
    private Long id;
    private String roleName;

    public Role(String roleName){
        this.roleName = roleName;
    }
}