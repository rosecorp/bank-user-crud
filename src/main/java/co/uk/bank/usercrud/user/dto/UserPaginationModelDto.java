package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.UserTitle;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * The UserPaginationModelDto class extends the Hateoas Representation Model and is required if we want to convert the User
 * Entity to a pagination format
 */
public class UserPaginationModelDto extends RepresentationModel<UserPaginationModelDto> {

    private UUID id;
    private UserTitle title;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String jobTitle;
    private Date createdStamp;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserPaginationModelDto userModel = (UserPaginationModelDto) o;
        return Objects.equals(getId(), userModel.getId()) &&
                getTitle() == userModel.getTitle() &&
                Objects.equals(getFirstName(), userModel.getFirstName()) &&
                Objects.equals(getLastName(), userModel.getLastName()) &&
                Objects.equals(getDateOfBirth(), userModel.getDateOfBirth()) &&
                Objects.equals(getJobTitle(), userModel.getJobTitle()) &&
                Objects.equals(getCreatedStamp(), userModel.getCreatedStamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getTitle(), getFirstName(), getLastName(), getDateOfBirth(), getJobTitle(), getCreatedStamp());
    }

    @Override
    public String toString() {
        return "UserPaginationModelDto{" +
                "id=" + id +
                ", title=" + title +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", createdStamp=" + createdStamp +
                '}';
    }
}
