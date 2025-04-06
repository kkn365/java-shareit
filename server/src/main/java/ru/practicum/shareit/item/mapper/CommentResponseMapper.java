package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentResponseMapper {
    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "itemId", source = "comment.item.id")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
