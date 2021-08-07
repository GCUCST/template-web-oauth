package cn.chenshaotong.mapper;

import cn.chenshaotong.dto.UserDto;
import cn.chenshaotong.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
    }
    public User mapToUser(UserDto userDto){
        return objectMapper.convertValue(userDto,User.class);
    }
    public UserDto mapToUserDto(User user){
        return objectMapper.convertValue(user,UserDto.class);
    }
}
