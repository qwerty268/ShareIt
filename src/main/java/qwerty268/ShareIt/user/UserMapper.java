package qwerty268.ShareIt.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public static User fromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail());
    }


    public static void updateUser(User notUpdatedUser, User updatedUser) {

        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(notUpdatedUser.getEmail());
        }

        if (updatedUser.getName() == null) {
            updatedUser.setName(notUpdatedUser.getName());
        }

        updatedUser.setId(notUpdatedUser.getId());
    }
}
