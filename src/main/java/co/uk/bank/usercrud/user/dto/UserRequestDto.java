package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.UserTitle;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.time.LocalDate;

public class UserRequestDto {

    @Schema(description = "User title.", example = "MR", required = true)
    @Size(max = 50)
    private UserTitle title;

    @Schema(description = "First name.", example = "John, ", required = true)
    @Size(max = 100)
    private String firstName;

    @Schema(description = "Last name.", example = "Smith", required = true)
    @Size(max = 100)
    private String lastName;

    @Schema(description = "Date of birth.", example = "1980-08-01", required = true)
    @Size(max = 30)
    private LocalDate dateOfBirth;

    @Schema(description = "Job title.", example = "IT Developer", required = true)
    @Size(max = 20)
    private String jobTitle;

    public UserRequestDto() { }

    public UserRequestDto(UserTitle title, String firstName, String lastName, LocalDate dateOfBirth, String jobTitle) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.jobTitle = jobTitle;
    }

    public UserTitle getTitle() {
        return title;
    }

    public void setTitle(UserTitle title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
