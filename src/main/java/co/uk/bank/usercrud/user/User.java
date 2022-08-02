package co.uk.bank.usercrud.user;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", length = 50, nullable = false)
    private UserTitle title;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", length = 30, nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "job_title", length = 20, nullable = false)
    private String jobTitle;

    @Column(name = "created_timestamp", updatable = false)
    @CreationTimestamp
    private Date createdStamp;

    @Column(name = "deleted", length = 5, nullable = false, updatable = false)
    private boolean deleted;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        id = id;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getTitle() == user.getTitle() &&
                getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName()) &&
                getDateOfBirth().equals(user.getDateOfBirth()) &&
                getJobTitle().equals(user.getJobTitle()) &&
                getCreatedStamp().equals(user.getCreatedStamp()) &&
                getDeleted().equals(user.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getFirstName(), getLastName(), getDateOfBirth(), getJobTitle(), getCreatedStamp(), getDeleted());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", title=" + title +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", createdStamp=" + createdStamp +
                ", deleted=" + deleted +
                '}';
    }
}
