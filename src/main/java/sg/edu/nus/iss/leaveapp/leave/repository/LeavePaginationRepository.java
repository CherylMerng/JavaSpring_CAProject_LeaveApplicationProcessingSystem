package sg.edu.nus.iss.leaveapp.leave.repository;

import java.util.Date;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;

@EnableJpaRepositories
@Repository
public interface LeavePaginationRepository extends PagingAndSortingRepository<LeaveApplication, Long> {
    @Query(value = "SELECT * FROM Leave_Application WHERE user_staffid = :staffid AND leave_type LIKE :leaveType", nativeQuery = true)
    public Page<LeaveApplication> findByUserStaffId(@Param("staffid") String staffid, @Param("leaveType") String leaveType, Pageable pagespecs);

    @Query(value = "SELECT * FROM Leave_Application WHERE user_staffid = :staffid AND leave_type LIKE :leaveType AND end_date >= :startdate", nativeQuery = true)
    public Page<LeaveApplication> findByUserStaffIdAndStartDate(@Param("staffid") String staffid, @Param("leaveType") String leaveType, @Param("startdate") Date startdate, Pageable pagespecs);

    @Query(value = "SELECT * FROM Leave_Application WHERE user_staffid = :staffid AND leave_type LIKE :leaveType AND start_date <= :enddate", nativeQuery = true)
    public Page<LeaveApplication> findByUserStaffIdAndEndDate(@Param("staffid") String staffid, @Param("leaveType") String leaveType, @Param("enddate") Date enddate, Pageable pagespecs);

    @Query(value = "SELECT * FROM Leave_Application WHERE user_staffid = :staffid AND leave_type LIKE :leaveType AND ((start_date >= :startdate AND end_date <= :enddate) OR (start_date <= :startdate AND end_date >= :enddate) OR (start_date <= :enddate AND end_date >= :enddate) OR (start_date <= :startdate AND end_date >= :startdate))", nativeQuery = true)
    public Page<LeaveApplication> findByUserStaffIdAndStartEndDate(@Param("staffid") String staffid, @Param("leaveType") String leaveType, @Param("startdate") Date startdate, @Param("enddate") Date enddate, Pageable pagespecs);
}