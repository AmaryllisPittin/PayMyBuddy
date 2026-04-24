package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserMapper;
import com.PayMyBuddy.pay_my_buddy.Service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDTO dto) {

        System.out.println("DTO username = " + dto.username);
        System.out.println("DTO email = " + dto.email);
        System.out.println("DTO password = " + dto.password);

        UserEntity user = UserMapper.toEntity(dto);
        userService.register(user);
    }

}
