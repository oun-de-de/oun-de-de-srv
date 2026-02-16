package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.employee.dto.EmployeeResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    builder = @Builder(disableBuilder = true)
)
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeResponse toEmployeeResponse(User employee);

    List<EmployeeResponse> toListEmployeeResponse(List<User> employee);

    EmployeeResult toEmployeeResult(User employee);
}
