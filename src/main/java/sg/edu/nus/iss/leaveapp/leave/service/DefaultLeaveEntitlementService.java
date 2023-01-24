package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;

public interface DefaultLeaveEntitlementService {

   Optional<DefaultLeaveEntitlement> findByJobGrade(String jobGrade);

   DefaultLeaveEntitlement saveDefaultLeaveEntitlement(DefaultLeaveEntitlement leaveEntitlement);

   List<DefaultLeaveEntitlement> getAll();
  
}