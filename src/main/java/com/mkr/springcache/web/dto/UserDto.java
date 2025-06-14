package com.mkr.springcache.web.dto;

import com.mkr.springcache.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    private String name;

    public static UserDto fromDto(User user) {
        return new UserDto(user.getId(), user.getName());
    }
}
