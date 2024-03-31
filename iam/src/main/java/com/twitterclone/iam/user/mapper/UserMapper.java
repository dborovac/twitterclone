package com.twitterclone.iam.user.mapper;

import com.twitterclone.iam.common.mapping.CycleAvoidingMappingContext;
import com.twitterclone.iam.user.model.domain.User;
import com.twitterclone.nodes.iam.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toDomain(UserEntity userEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
