package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import sg.edu.nus.iss.leaveapp.leave.model.Role;

public interface RoleService {
    Role saveRole(Role role);

    List<Role> findRoleByRoleName(String roleName);

}