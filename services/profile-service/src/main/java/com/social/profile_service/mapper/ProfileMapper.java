package com.social.profile_service.mapper;

import com.social.profile_service.dto.ProfileRequest;
import com.social.profile_service.dto.ProfileResponse;
import com.social.profile_service.entity.Profile;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = {CustomMapperUtil.class}
)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Profile toEntity(ProfileRequest request);

    @Mapping(target = "formattedCreatedAt", source = "createdAt", qualifiedByName = "formatDateTime")
    @Mapping(target = "formattedUpdatedAt", source = "updatedAt", qualifiedByName = "formatDateTime")
    ProfileResponse toResponse(Profile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfileFromEntity(Profile source, @MappingTarget Profile target);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
