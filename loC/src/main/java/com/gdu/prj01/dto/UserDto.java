package com.gdu.prj01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 사용자
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
  private int userNo;
  private String userId;
}
