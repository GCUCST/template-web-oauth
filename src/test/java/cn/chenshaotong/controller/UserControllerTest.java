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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

//    @Test
//    void test3() {
//        UserDto userDto = UserDto.builder().name("CST").build();
//        User user = UserMapper.mapToUser(userDto);
//        User save = userRepository.save(user);
//        System.out.println(save.toString());
//    }

    @Test
    void test4() {
        List<User> all = userRepository.findAll();
        all.forEach(e->{
            UserDto userDto  = UserMapper.mapToUserDto(e);
            System.out.println(userDto.toString());
        });

        List<UserDto> cst = all.stream().map(e -> {
            return UserMapper.mapToUserDto(e);
        }).filter(e -> e.getName().equals("CST")).collect(Collectors.toList());

        int size = cst.size();
        System.out.println(size);


    }







}