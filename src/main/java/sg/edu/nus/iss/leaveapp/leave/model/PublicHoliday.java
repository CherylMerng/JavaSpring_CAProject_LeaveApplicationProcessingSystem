package sg.edu.nus.iss.leaveapp.leave.model;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class PublicHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Date of Public Holiday cannot be blank.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfPublicHol;
    @NotBlank(message = "Name of Public Holiday cannot be blank.")
    private String nameOfPublicHol;

    public PublicHoliday(LocalDate dateOfPublicHol, String nameOfPublicHol){
        this.dateOfPublicHol = dateOfPublicHol;
        this.nameOfPublicHol = nameOfPublicHol;
    }
    @Override
    public String toString(){
        String description = "Date of Public Hol:" + dateOfPublicHol + "Name of Public Hol:" + nameOfPublicHol;
        return description;
    }
}