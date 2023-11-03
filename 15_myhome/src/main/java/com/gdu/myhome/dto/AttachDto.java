package com.gdu.myhome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttachDto {
  private int attachNo;
  private String path;
  private String originalFilename;
  private String filesystemName;
  private int downloadCount;
  private int hasThumbnail;     // 썸네일을 만들어주는 dependency를 사용할 것이다.
  private int uploadNo;
}
