package com.cdtphuhoi.oun_de_de.utils.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.mappers.EmployeeMapper;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import org.junit.jupiter.api.Test;
import java.util.List;

class EmployeeMapperTest {
    private final EmployeeMapper mapper = MapperHelpers.getEmployeeMapper();

    @Test
    void testToEmployeeResponse() {
        var employee = new User();
        employee.setId("1L");
        employee.setUsername("john.doe");
        var response = mapper.toEmployeeResponse(employee);
        assertNotNull(response);
        assertEquals(employee.getId(), response.getId());
        assertEquals(employee.getUsername(), response.getUsername());
    }

    @Test
    void testToListEmployeeResponse() {
        var employee1 = new User();
        employee1.setId("1L");
        employee1.setUsername("john.doe");
        var employee2 = new User();
        employee2.setId("2L");
        employee2.setUsername("jane.smith");
        var responses = mapper.toListEmployeeResponse(List.of(employee1, employee2));
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(employee1.getId(), responses.get(0).getId());
        assertEquals(employee2.getUsername(), responses.get(1).getUsername());
    }
}