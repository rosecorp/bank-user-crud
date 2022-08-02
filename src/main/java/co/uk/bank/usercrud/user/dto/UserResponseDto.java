package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.UserTitle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class UserResponseDto {

    private UUID id;
    private UserTitle title;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String jobTitle;
    private Date createdStamp;

    public UserResponseDto() { }

    public UserResponseDto(UUID id, UserTitle title, String firstName, String lastName, LocalDate dateOfBirth, String jobTitle, Date createdStamp) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.jobTitle = jobTitle;
        this.createdStamp = createdStamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Date getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(Date createdStamp) {
        this.createdStamp = createdStamp;
    }
}
