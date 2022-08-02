package co.uk.bank.usercrud.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.util.UUID;

public class UserSearchDto {

    @Schema(description = "id", example = "ca24755d-91e5-4c56-82af-507bf2461754", required = false)
    @Size(max = 36)
    private UUID id;

    @Schema(description = "First name.", example = "John", required = false)
    @Size(max = 100)
    private String firstName;

    @Schema(description = "Last name.", example = "Smith", required = false)
    @Size(max = 100)
    private String lastName;

    public UserSearchDto() {
        this.id = null;
        this.firstName = "";
        this.lastName = "";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
