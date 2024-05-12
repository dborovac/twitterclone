package com.twitterclone.iam.mapper;

import com.twitterclone.iam.common.mapping.CycleAvoidingMappingContext;
import com.twitterclone.iam.domain.User;
import com.twitterclone.nodes.iam.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User toDomain(UserEntity userEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<User> toDomain(List<UserEntity> userEntities, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
