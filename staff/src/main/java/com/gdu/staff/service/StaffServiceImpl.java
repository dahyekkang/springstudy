package com.gdu.staff.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gdu.staff.dao.StaffMapper;
import com.gdu.staff.dto.StaffDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StaffServiceImpl implements StaffService {

  private final StaffMapper staffMapper;
  
  @Override
  public ResponseEntity<Map<String, Object>> registerStaff(StaffDto staff) {
    int addResult = 0;
    Map<String, Object> map = new HashMap<>();
    try {
      // insert하다가 exception발생하는 경우(사원 번호 중복/not null인데 입력값이 null) Map.of사용하면 예외 처리 불가
      // -> 예외 발생했을 때와 발생하지 않았을 때를 구분하기 위해 Map을 responseEntity에 넣어서 성공인지 실패인지 함께 반환하겠다.
      // responseEntity는 HttpStatus를 저장할 수 있다.
      addResult = staffMapper.insertStaff(staff);
      map.put("addResult", addResult);
      return new ResponseEntity<>(map, HttpStatus.OK);
    } catch(Exception e) {
      map.put("addResult", addResult);
      return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  

  @Override
  public ResponseEntity<Map<String, Object>> getStaffs() {
    List<StaffDto> staffList = staffMapper.getStaffList();
    Map<String, Object> map = Map.of("staffList", staffList);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  

  @Override
  public ResponseEntity<Map<String, Object>> getStaff(String sno) {
    Map<String, Object> map = Map.of("staff", staffMapper.getStaff(sno));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
}
