package co.uk.bank.usercrud.user.dto;

import co.uk.bank.usercrud.user.User;
import co.uk.bank.usercrud.user.UserEndpoint;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for Pagination.
 * It converts the User Entity to the User Model and has the code for it
 */
@Component
public class UserPaginationModelDtoAssembler extends RepresentationModelAssemblerSupport<User, UserPaginationModelDto> {

    public UserPaginationModelDtoAssembler() {
        super(UserEndpoint.class, UserPaginationModelDto.class);
    }

    @Override
    public UserPaginationModelDto toModel(User entity) {
        UserPaginationModelDto model = new UserPaginationModelDto();
        // Both UserPaginationModelDto and User have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
