package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import com.example.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TokenResponseDto {
    private String value;
    private String name;
    private String email;
    private List<Role> roles;
    private long expiry;

    public static TokenResponseDto from(Token token) {
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setValue(token.getValue());
        tokenResponseDto.setName(token.getUser().getName());
        tokenResponseDto.setEmail(token.getUser().getName());
        tokenResponseDto.setRoles(token.getUser().getRoles());
        tokenResponseDto.setExpiry(token.getExpiry());

        return tokenResponseDto;
    }
}
