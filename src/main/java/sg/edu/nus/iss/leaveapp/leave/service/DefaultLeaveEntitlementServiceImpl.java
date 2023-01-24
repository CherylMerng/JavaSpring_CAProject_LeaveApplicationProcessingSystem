package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.repository.DefaultLeaveEntitlementRepository;

@Service
public class DefaultLeaveEntitlementServiceImpl implements DefaultLeaveEntitlementService {

    @Autowired
    private DefaultLeaveEntitlementRepository defaultLeaveEntitlementRepository;

    @Override
    public  Optional<DefaultLeaveEntitlement> findByJobGrade(String jobGrade){
        return defaultLeaveEntitlementRepository.findById(jobGrade);
    }

    @Override
    public DefaultLeaveEntitlement saveDefaultLeaveEntitlement(DefaultLeaveEntitlement leaveEntitlement){
        return defaultLeaveEntitlementRepository.save(leaveEntitlement);
    }

    @Override
    public List<DefaultLeaveEntitlement> getAll(){
        return defaultLeaveEntitlementRepository.findAll();
    }
    
}