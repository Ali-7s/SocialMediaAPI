package dev.ali.socialmediaapi.dto;

import dev.ali.socialmediaapi.model.Post;
import dev.ali.socialmediaapi.model.User;
import dev.ali.socialmediaapi.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class PostMapper {
    @Autowired
    @Lazy
    protected UserService userService;


    @Mapping(target = "user", qualifiedByName = "toUserSummaryDTO")
    public abstract PostDTO toPostDTO(Post post);

    public abstract Post toPost(PostDTO postDTO);

    @Named("toUserSummaryDTO")
    UserSummaryDTO toUserSummaryDTO(User user) {
        return new UserSummaryDTO(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                userService.getImgUrl(user.getId())
        );
    }


}

