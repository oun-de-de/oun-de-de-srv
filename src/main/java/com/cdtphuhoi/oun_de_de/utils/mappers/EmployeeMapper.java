package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.employee.EmployeeResponse;
import com.cdtphuhoi.oun_de_de.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeResponse toEmployeeResponse(User employee);

     List<EmployeeResponse> toListEmployeeResponse(List<User> employee);
}
