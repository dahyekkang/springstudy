package com.gdu.staff.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.staff.dto.StaffDto;

@Mapper
public interface StaffMapper {

  // 전체 조회
  public List<StaffDto> getStaffList();
  
  // 사원 조회
  public StaffDto getStaff(String sno);
  
  // 사원 등록
  public int insertStaff(StaffDto staff);
  
}
