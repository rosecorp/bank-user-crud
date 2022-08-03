package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.UserTitle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDto {

    @Schema(description = "User title.", example = "MR", required = true)
    @NotNull(message = "User title is mandatory")
    private UserTitle title;

    @Schema(description = "First name.", example = "John", required = true)
    @Size(max = 100)
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Schema(description = "Last name.", example = "Smith", required = true)
    @Size(max = 100)
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Schema(description = "Date of birth.", example = "1980-08-01", required = true)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotBlank(message = "DOB is mandatory")
    private String dateOfBirth;

    @Schema(description = "Job title.", example = "IT Developer", required = true)
    @NotBlank(message = "Job title is mandatory")
    private String jobTitle;

    public UserRequestDto() { }

    public UserRequestDto(UserTitle title, String firstName, String lastName, String dateOfBirth, String jobTitle) {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
