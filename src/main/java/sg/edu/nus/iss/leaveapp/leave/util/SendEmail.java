package sg.edu.nus.iss.leaveapp.leave.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;

@Component
public class SendEmail{
    @Autowired
    private JavaMailSender emailSender;

    public String address = "shizuyl328@gmail.com";

    public void sendLeaveMessage(String from, String to, String subject, LeaveApplication leaveApplication) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            
            String header = String.format("<html><b>%s has applied for leave.</b>\n", leaveApplication.getUser().getFullName());
            String body = String.format(
                "<br><br>Start Date: %s<br>End Date: %s<br><br>Leave Type: %s<br><br>Reason: %s<br><br>Approve/reject <a href=\"http://localhost:8083/mgr/decideleaveoutcome?id="+leaveApplication.getId()+"\">here</a>", 
                leaveApplication.getStartDate().toString(), leaveApplication.getEndDate().toString(), leaveApplication.getLeaveType(), leaveApplication.getReason());
            
                String footer = String.format("</html>");

            String text = header + body + footer;

            helper.setText(text, true);
        }
        catch(Exception e){}

        emailSender.send(message);
    }

    public void sendProcessedMessage(String from, String to, String subject, LeaveApplication leaveApplication) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            
            String header = String.format("<html><b>Your leave application %d has been processed.</b>\n", leaveApplication.getId());
            String body = String.format(
                "<br><br>Start Date: %s<br>End Date: %s<br><br>Leave Type: %s<br><br>Status: %s<br>Manager comment: %s<br><br>View details <a href=\"http://localhost:8083/emp/viewleavedetails?id="+leaveApplication.getId()+"\">here</a>", 
                leaveApplication.getStartDate().toString(), leaveApplication.getEndDate().toString(), leaveApplication.getLeaveType(), leaveApplication.getStatus(), leaveApplication.getManagerComment());
            
                String footer = String.format("</html>");

            String text = header + body + footer;

            helper.setText(text, true);
        }
        catch(Exception e){}

        emailSender.send(message);
    }
}
