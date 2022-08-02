package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.user.dto.UserPaginationModelDto;
import co.uk.bank.usercrud.user.dto.UserPaginationModelDtoAssembler;
import co.uk.bank.usercrud.user.dto.UserRequestDto;
import co.uk.bank.usercrud.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static co.uk.bank.usercrud.user.dto.UserDtoAssembler.toUserResponseDto;
import static co.uk.bank.usercrud.user.dto.UserDtoAssembler.toUserResponseDtos;

@RestController
@RequestMapping("/api")
public class UserEndpoint {

    @Autowired
    UserService userService;

    @Autowired
    private UserPaginationModelDtoAssembler userPaginationModelDtoAssembler;

    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    /**
     * @param idFilter Filter for the Id if required - like match
     * @param firstNameFilter Filter for the First Name if required - like match
     * @param lastNameFilter  Filter for the Last Name if required - like match
     * @return List of filtered users
     */
    @Operation(summary = "Simple API - Find User by first name or last name or id", description = "Simple API - Find user by firstNameFilter or lastNameFilter or idFilter", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @GetMapping("/v1/users")
    public List<UserResponseDto> fetchUsersAsFilteredList(@Parameter(description="First Name Filter, default empty") @RequestParam(defaultValue = "") String firstNameFilter,
                                                          @Parameter(description="Last Name Filter, default empty") @RequestParam(defaultValue = "") String lastNameFilter,
                                                          @Parameter(description="ID Filter, default empty") @RequestParam(defaultValue = "") UUID idFilter) throws ResourceNotFoundException {
        List<User> users = userService.fetchFilteredUserDataAsList(firstNameFilter, lastNameFilter, idFilter);
        if (users.isEmpty())  throw new ResourceNotFoundException("User not found");

        return toUserResponseDtos(users);
    }

    @Operation(summary = "Add a new User", description = "", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input") })
    @PostMapping("/v1/user")
    public Map<String, UUID> createUser(@Parameter(description="User to add. Cannot be null or empty.",required=true, schema=@Schema(implementation = UserRequestDto.class))
                                        @Valid @RequestBody UserRequestDto userDetails) {
        Map <String, UUID> response = new HashMap<>();
        response.put("id", userService.saveUser(userDetails).getId());

        return response;
    }

    @Operation(summary = "Update an existing user", description = "", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @PutMapping("/v1/user/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Parameter(description="Id of the user to be update. Cannot be empty.", required=true) @PathVariable(value = "id") UUID id,
                                                      @Valid @RequestBody UserRequestDto userDetails) throws ResourceNotFoundException {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        return ResponseEntity.ok(toUserResponseDto(userService.updateUser(user, userDetails)));
    }

    @Operation(summary = "Soft Delete an existing user", description = "", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @DeleteMapping("/v1/user/{id}")
    public Map < String, Boolean > deleteUser(@Parameter(description="Id of the user to be deleted. Cannot be empty.", required=true) @PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        userService.deleteUser(user.getId());
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    /**
     * @param firstNameFilter Filter for the First Name if required - like match
     * @param lastNameFilter  Filter for the Last Name if required - like match
     * @param idFilter        Filter for the id if required - like match
     * @param page            number of the page returned
     * @param size            number of entries in each page
     * @param sortList        list of columns to sort on
     * @param sortOrder       sort order. Can be ASC or DESC
     * @return PagedModel object in Hateoas with customers after filtering and sorting
     */
    @GetMapping("/v2/users")
    @Operation(summary = "Find User by first name or last name in Hateoas Representation", description = "Paginated results with sort order and sort list elements available", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found") })
    public PagedModel<UserPaginationModelDto> fetchUsersWithPagination(
            @Parameter(description="First Name Filter, default empty")@RequestParam(defaultValue = "") String firstNameFilter,
            @Parameter(description="Last Name Filter, default empty") @RequestParam(defaultValue = "") String lastNameFilter,
            @Parameter(description="ID Filter, default empty") @RequestParam(defaultValue = "") UUID idFilter,
            @Parameter(description="Page number, default 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description="Results number on the page, default 30") @RequestParam(defaultValue = "30") int size,
            @Parameter(description="number of fields sorted by, default empty") @RequestParam(defaultValue = "") List<String> sortList,
            @Parameter(description="Sorting, default DESC") @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) throws ResourceNotFoundException {
        Page<User> customerPage = userService.fetchUserDataAsPageWithFilteringAndSorting(firstNameFilter, lastNameFilter, idFilter, page, size, sortList, sortOrder.toString());
        if (!customerPage.hasContent()) { throw new ResourceNotFoundException("User not found"); }
        // Use the pagedResourcesAssembler and userPaginationModelDtoAssembler to convert data to PagedModel format
        return pagedResourcesAssembler.toModel(customerPage, userPaginationModelDtoAssembler);
    }

}
