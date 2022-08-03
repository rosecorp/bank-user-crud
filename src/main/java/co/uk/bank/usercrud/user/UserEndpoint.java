package co.uk.bank.usercrud.user;

import co.uk.bank.usercrud.user.dto.*;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserEndpoint {

    @Autowired
    UserService userService;

    @Autowired
    private UserPaginationModelDtoAssembler userPaginationModelDtoAssembler;

    @Autowired
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Autowired
    private UserDtoAssembler userDtoAssembler;


    /**
     * @param  userSearchDto   Search criteria - firstName, lastName, id
     * @return List<UserResponseDto> users after search
     */
    @Operation(summary = "Find User by search criteria", description = "Simple API - Find user by firstNameFilter or lastNameFilter or idFilter", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @PostMapping("/v1/users")
    public List<UserResponseDto> fetchUsersAsFilteredList(@Parameter(description="Search criteria") @RequestBody UserSearchDto userSearchDto) throws ResourceNotFoundException {
        List<User> users = userService.fetchFilteredUserDataAsList(userSearchDto.getFirstName(), userSearchDto.getLastName(), userSearchDto.getId());
        if (users.isEmpty()) throw new ResourceNotFoundException("User not found");

        return userDtoAssembler.toUserResponseDtos(users);
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

        return ResponseEntity.ok(userDtoAssembler.toUserResponseDto(userService.updateUser(user, userDetails)));
    }

    @Operation(summary = "Soft Delete an existing user", description = "", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found") })
    @DeleteMapping("/v1/user/{id}")
    public Map <String, Boolean> deleteUser(@Parameter(description="Id of the user to be deleted. Cannot be empty.", required=true) @PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        userService.deleteUser(user.getId());
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    /**
     * @param userSearchDto   Search criteria - firstName, lastName, id
     * @param page            number of the page returned
     * @param size            number of entries in each page
     * @param sortList        list of columns to sort on
     * @param sortOrder       sort order. Can be ASC or DESC
     * @return PagedModel object in Hateoas with users after filtering and sorting
     */
    @PostMapping("/v2/users")
    @Operation(summary = "Find User by search criteria Hateoas Representation", description = "Paginated results with sort order and sort list elements available", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found") })
    public PagedModel<UserPaginationModelDto> fetchUsersWithPagination(
            @Parameter(description="Search criteria") @RequestBody UserSearchDto userSearchDto,
            @Parameter(description="Page number, default 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description="Results number on the page, default 30") @RequestParam(defaultValue = "30") int size,
            @Parameter(description="number of fields sorted by, default empty") @RequestParam(defaultValue = "") List<String> sortList,
            @Parameter(description="Sorting, default DESC") @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) throws ResourceNotFoundException {
        Page<User> userPage = userService.fetchUserDataAsPageWithFilteringAndSorting(userSearchDto.getFirstName(), userSearchDto.getLastName(), userSearchDto.getId(), page, size, sortList, sortOrder.toString());
        if (!userPage.hasContent()) { throw new ResourceNotFoundException("User not found"); }
        // Use the pagedResourcesAssembler and userPaginationModelDtoAssembler to convert data to PagedModel format
        return pagedResourcesAssembler.toModel(userPage, userPaginationModelDtoAssembler);
    }

}
