package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.controllers.dto.employee.UpdateEmployeeProfileRequest;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.employee.dto.EmployeeResult;
import com.cdtphuhoi.oun_de_de.services.employee.dto.UpdateEmployeeProfileData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeResponse toEmployeeResponse(User employee);

    List<EmployeeResponse> toListEmployeeResponse(List<User> employee);

    EmployeeResult toEmployeeResult(User employee);

    UpdateEmployeeProfileData toUpdateEmployeeData(UpdateEmployeeProfileRequest request);

    void updateEmployee(
        @MappingTarget User employee,
        UpdateEmployeeProfileData data
    );
}
