package spring.framework._2fa_application_.totp.secret;

import org.mapstruct.Mapper;

@Mapper
public interface SecretMapper {
    SecretDTO secretDataToSecretDto(SecretData secretData);
    SecretData secretDtoToSecretData(SecretDTO secretDTO);
}
