package cn.chenshaotong.mapper;

import cn.chenshaotong.dto.UserDto;
import cn.chenshaotong.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public User mapToUser(UserDto userDto){
        return objectMapper.convertValue(userDto,User.class);
    }
    public UserDto mapToUserDto(User user){
        return objectMapper.convertValue(user,UserDto.class);
    }
}
