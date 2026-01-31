package com.cdtphuhoi.oun_de_de.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
    imports = EmployeeMapper.class
)
public interface CouponMapper {
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

}
