package cn.chenshaotong.controller;

import cn.chenshaotong.dto.UserDto;
import cn.chenshaotong.entity.User;
import cn.chenshaotong.mapper.UserMapper;
import cn.chenshaotong.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@PropertySource({"classpath:application.yml"})
class UserControllerTest {
    @Autowired
    RedisTemplate redisTemplate;

//    @Test
    void test1() {
        redisTemplate.opsForValue().set("name", "cst");
        System.out.println(redisTemplate.opsForValue().get("name"));
        System.out.println("哈哈哈哈");
    }

    @Test
    void test2() {
        UserDto userDto = UserDto.builder().id("").name("老王吧").build();
        User user = UserMapper.mapToUser(userDto);
        System.out.println(user.toString());
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void test3() {
        UserDto userDto = UserDto.builder().name("CST").build();
        User user = UserMapper.mapToUser(userDto);
        User save = userRepository.save(user);
        System.out.println(save.toString());
    }








}