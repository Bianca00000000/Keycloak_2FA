package spring.framework._2fa_application_.totp.users;

import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDataDTO userDataToUserDataDto(UserData userData);
    UserData userDataDtoToUserData(UserDataDTO userDataDTO);
}
