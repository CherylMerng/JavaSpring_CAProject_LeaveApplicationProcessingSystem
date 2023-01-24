package sg.edu.nus.iss.leaveapp.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;

@EnableJpaRepositories
@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long>  {
    
}