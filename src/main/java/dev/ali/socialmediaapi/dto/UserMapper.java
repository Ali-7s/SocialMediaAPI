package dev.ali.socialmediaapi.dto;

import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    @Lazy
    protected UserService userService;

    @Mapping(target = "photoUrl", expression = "java(userService.getImgUrl(user.getId()))")
    @Mapping(target = "followers", qualifiedByName = "toUserSummarySet")
    @Mapping(target = "following", qualifiedByName = "toUserSummarySet")
    public abstract UserProfileDTO toUserProfileDTO(User user);

    @Named("toUserSummarySet")
    Set<UserSummaryDTO> toUserSummarySet(Set<User> users) {
        return users.stream()
                .map(this::toUserSummaryDTO)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "photoUrl", expression = "java(userService.getImgUrl(user.getId()))")
    public abstract UserSummaryDTO toUserSummaryDTO(User user);
}