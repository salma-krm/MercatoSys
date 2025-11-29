package com.mercatosys.mapper;


import com.mercatosys.dto.user.LoginResponseDTO;
import com.mercatosys.entity.User;
import com.mercatosys.mapper.UserMapper;
import com.mercatosys.service.impl.SessionManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class AuthMapper {

    @Autowired
    protected SessionManager sessionManager;

    @Mapping(target = "user", expression = "java(userMapper.toUserResponse(user))")
    @Mapping(target = "sessionId", expression = "java(sessionManager.createSession(user))")
    @Mapping(target = "message", constant = "Operation successful")
    public static LoginResponseDTO toAuthResponse(User user) {
        return null;
    }

    @Autowired
    protected UserMapper userMapper;
}
