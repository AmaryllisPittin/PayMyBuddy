package com.PayMyBuddy.pay_my_buddy.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionDTO;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class UserConnectionController {

    private final UserConnectionService service;

    @PostMapping
    public void addConnection(@RequestBody UserConnectionDTO dto) {
        service.addConnection(dto);
    }

    @GetMapping("/{userId}")
    public List<UserConnectionDTO> getConnections(@PathVariable Integer userId) {
        return service.getConnections(userId);
    }

    @DeleteMapping
    public void removeConnection(
            @RequestParam Integer userId,
            @RequestParam Integer connectedUserId) {
        service.removeConnection(userId, connectedUserId);
    }
}
