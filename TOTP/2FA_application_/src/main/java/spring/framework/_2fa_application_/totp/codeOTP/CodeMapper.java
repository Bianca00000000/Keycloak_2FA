package spring.framework._2fa_application_.totp.codeOTP;

import org.mapstruct.Mapper;

@Mapper
public interface CodeMapper {

    CodeData codeDataDtoToCodeData(CodeDataDTO dto);
    CodeDataDTO codeDataToCodeDataDto(CodeData codeData);
}
