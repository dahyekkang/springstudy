-- 시퀀스
DROP SEQUENCE USER_SEQ;
DROP SEQUENCE FREE_SEQ;
DROP SEQUENCE BLOG_SEQ;
DROP SEQUENCE COMMENT_SEQ;

CREATE SEQUENCE USER_SEQ NOCACHE;
CREATE SEQUENCE FREE_SEQ NOCACHE;
CREATE SEQUENCE BLOG_SEQ NOCACHE;
CREATE SEQUENCE COMMENT_SEQ NOCACHE;


-- 테이블
DROP TABLE COMMENT_T;
DROP TABLE BLOG_T;
DROP TABLE FREE_T;
DROP TABLE INACTIVE_USER_T;
DROP TABLE LEAVE_USER_T;
DROP TABLE ACCESS_T;
DROP TABLE USER_T;


-- 가입한 사용자
CREATE TABLE USER_T (
    USER_NO         NUMBER              NOT NULL        ,       -- PK
    EMAIL           VARCHAR2(100 BYTE)  NOT NULL UNIQUE ,       -- 이메일을 아이디로 사용
    PW              VARCHAR2(64 BYTE)                   ,       -- SHA-256 암호화 방식 사용
    NAME            VARCHAR2(50 BYTE)                   ,       -- 이름
    GENDER          VARCHAR2(2 BYTE)                    ,       -- M, F, NO
    MOBILE          VARCHAR2(15 BYTE)                   ,       -- 하이픈 제거 후 저장
    POSTCODE        VARCHAR2(5 BYTE)                    ,       -- 우편번호
    ROAD_ADDRESS    VARCHAR2(100 BYTE)                  ,       -- 도로명주소
    JIBUN_ADDRESS   VARCHAR2(100 BYTE)                  ,       -- 지번주소
    DETAIL_ADDRESS  VARCHAR2(100 BYTE)                  ,       -- 상세주소
    AGREE           NUMBER              NOT NULL        ,       -- 서비스 동의 여부(0:필수, 1:이벤트)
    STATE           NUMBER                              ,       -- 가입형태(0:정상, 1:네이버)
    PW_MODIFIED_AT  DATE                                ,       -- 비밀번호 수정일
    JOINED_AT       DATE                                ,       -- 가입일
    CONSTRAINT PK_USER PRIMARY KEY(USER_NO)
);

-- 접속 기록
CREATE TABLE ACCESS_T (
    EMAIL       VARCHAR2(100 BYTE)      NOT NULL        ,       -- 접속한 사용자
    LOGIN_AT    DATE                                    ,       -- 로그인 일시
    CONSTRAINT FK_USER_ACCESS FOREIGN KEY(EMAIL) REFERENCES USER_T(EMAIL) ON DELETE CASCADE
);

-- 탈퇴한 사용자
CREATE TABLE LEAVE_USER_T (
    EMAIL           VARCHAR2(50 BYTE)   NOT NULL UNIQUE ,       -- 탈퇴한 사용자 이메일
    JOINED_AT       DATE                                ,       -- 가입일
    LEAVED_AT       DATE                                        -- 탈퇴일
);

-- 휴면 사용자 (1년 이상 접속 기록이 없으면 휴면 처리)
CREATE TABLE INACTIVE_USER_T (
    USER_NO         NUMBER              NOT NULL        ,       -- PK
    EMAIL           VARCHAR2(100 BYTE)  NOT NULL UNIQUE ,       -- 이메일을 아이디로 사용
    PW              VARCHAR2(64 BYTE)                   ,       -- SHA-256 암호화 방식 사용
    NAME            VARCHAR2(50 BYTE)                   ,       -- 이름
    GENDER          VARCHAR2(2 BYTE)                    ,       -- M, F, NO
    MOBILE          VARCHAR2(15 BYTE)                   ,       -- 하이픈 제거 후 저장
    POSTCODE        VARCHAR2(5 BYTE)                    ,       -- 우편번호
    ROAD_ADDRESS    VARCHAR2(100 BYTE)                  ,       -- 도로명주소
    JIBUN_ADDRESS   VARCHAR2(100 BYTE)                  ,       -- 지번주소
    DETAIL_ADDRESS  VARCHAR2(100 BYTE)                  ,       -- 상세주소
    AGREE           NUMBER              NOT NULL        ,       -- 서비스 동의 여부(0:필수, 1:이벤트)
    STATE           NUMBER                              ,       -- 가입형태(0:정상, 1:네이버)
    PW_MODIFIED_AT  DATE                                ,       -- 비밀번호 수정일
    JOINED_AT       DATE                                ,       -- 가입일
    INACTIVED_AT    DATE                                ,       -- 휴면처리일
    CONSTRAINT PK_INACTIVE_USER PRIMARY KEY(USER_NO)
);

-- 자유게시판(계층형-N차, 댓글/대댓글 작성 가능)
CREATE TABLE FREE_T (
    FREE_NO         NUMBER              NOT NULL        ,
    EMAIL           VARCHAR2(100 BYTE)  NULL            ,       -- 작성자가 없어져도 게시글은 박제
    CONTENTS        VARCHAR2(4000 BYTE) NOT NULL        ,
    CREATED_AT      TIMESTAMP           NULL            ,
    STATUS          NUMBER              NOT NULL        ,       -- 1:정상, 0:삭제 (실제로 삭제되지 않는 게시판)
    DEPTH           NUMBER              NOT NULL        ,       -- 0:원글, 1:댓글, 2:대댓글, ...
    GROUP_NO        NUMBER              NOT NULL        ,       -- 원글과 모든 댓글(댓글, 대댓글)은 동일한 GROUP_NO를 가져야 함
    GROUP_ORDER     NUMBER              NOT NULL        ,       -- 같은 그룹 내 정렬 순서 : 초깃값 0
    CONSTRAINT PK_FREE PRIMARY KEY(FREE_NO),
    CONSTRAINT FK_USER_FREE FOREIGN KEY(EMAIL) REFERENCES USER_T(EMAIL) ON DELETE SET NULL
);      

-- 블로그(댓글형)
CREATE TABLE BLOG_T (
    BLOG_NO         NUMBER              NOT NULL        ,       -- 블로그 번호
    TITLE           VARCHAR2(500 BYTE)  NOT NULL        ,       -- 제목
    CONTENTS        CLOB                NULL            ,       -- 내용
    USER_NO         NUMBER              NOT NULL        ,       -- 작성자 번호(NULL인 경우 ON DELETE SET NULL 처리가 가능하고, NOT NULL인 경우 ON DELETE CASCDE 처리가 가능하다.)
    HIT             NUMBER              DEFAULT 0       ,       -- 조회수
    IP              VARCHAR2(30 BYTE)   NULL            ,       -- IP 주소
    CREATED_AT      VARCHAR2(30 BYTE)   NULL            ,       -- 작성일
    MODIFIED_AT     VARCHAR2(30 BYTE)   NULL            ,       -- 수정일
    CONSTRAINT PK_BLOG PRIMARY KEY(BLOG_NO),
    CONSTRAINT FK_USER_BLOG FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE   -- 작성자가 삭제되면 블로그도 함께 삭제된다.
);

-- 블로그 댓글(계층형-1차, 댓글 작성 가능/대댓글 작성 불가능)
CREATE TABLE COMMENT_T (
    COMMENT_NO      NUMBER              NOT NULL        ,
    CONTENTS        VARCHAR2(4000 BYTE) NULL            ,
    USER_NO         NUMBER              NULL            ,
    BLOG_NO         NUMBER              NOT NULL        ,
    CREATED_AT      VARCHAR2(30 BYTE)   NULL            ,
    STATUS          NUMBER              NOT NULL        ,       -- 1:정상, 0:삭제 (실제로 삭제되지 않는 게시판)
    DEPTH           NUMBER              NOT NULL        ,       -- 0:원글, 1:댓글, 2:대댓글, ...
    GROUP_NO        NUMBER              NOT NULL        ,       -- 원글과 모든 댓글(댓글, 대댓글)은 동일한 GROUP_NO를 가져야 함
    CONSTRAINT PK_COMMENT PRIMARY KEY(COMMENT_NO),
    CONSTRAINT FK_USER_COMMENT FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE SET NULL,
    CONSTRAINT FK_BLOG_COMMENT FOREIGN KEY(BLOG_NO) REFERENCES BLOG_T(BLOG_NO) ON DELETE CASCADE
);


-- 테스트용 INSERT
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user1@naver.com', STANDARD_HASH('1111', 'SHA256'), '사용자1', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 101호', 0, 0, TO_DATE('20231001', 'YYYYMMDD'), TO_DATE('20220101', 'YYYYMMDD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user2@naver.com', STANDARD_HASH('2222', 'SHA256'), '사용자2', 'F', '01022222222', '22222', '디지털로', '가산동', '101동 101호', 0, 0, TO_DATE('20230801', 'YYYYMMDD'), TO_DATE('20220101', 'YYYYMMDD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user3@naver.com', STANDARD_HASH('3333', 'SHA256'), '사용자3', 'NO', '01033333333', '33333', '디지털로', '가산동', '101동 101호', 0, 0, TO_DATE('20230601', 'YYYYMMDD'), TO_DATE('20220101', 'YYYYMMDD'));

INSERT INTO ACCESS_T VALUES('user1@naver.com', TO_DATE('20231018', 'YYYYMMDD'));        -- 정상 회원 (user1)
INSERT INTO ACCESS_T VALUES('user2@naver.com', TO_DATE('20220201', 'YYYYMMDD'));        -- 휴면 회원 (user2)
                                                                                        -- 휴면 회원 (user3)
COMMIT;


-- CURRVAL은 현재 사용이 끝난 가장 최근 번호
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용1', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);   
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용2', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용3', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용4', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용5', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);   
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용6', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용7', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용8', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용9', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);   
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용10', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용11', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용12', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용13', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);   
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용14', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용15', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용16', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user2@naver.com', '내용17', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);   
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user3@naver.com', '내용18', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용19', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
INSERT INTO FREE_T VALUES (FREE_SEQ.NEXTVAL, 'user1@naver.com', '내용20', SYSTIMESTAMP, 1, 0, FREE_SEQ.CURRVAL, 0);
COMMIT;

-- 쿼리 테스트

-- 1. 목록 (??? 순으로 5 ~ 10)

-- 1) ROWNUM 가상 칼럼
SELECT FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
  FROM (SELECT ROWNUM AS RN, FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
          FROM (SELECT FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
                  FROM FREE_T
                 ORDER BY FREE_NO DESC))
 WHERE RN BETWEEN 5 AND 10;
 
-- 2) ROW_NUMBER() 함수
SELECT FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
  FROM (SELECT ROW_NUMBER() OVER(ORDER BY FREE_NO DESC) AS RN, FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER -- 3단 서브쿼리문이 2단 서브쿼리문으로 바뀔 수 있다. ROW_NUMBER() 함수 사용
          FROM FREE_T)
 WHERE RN BETWEEN 5 AND 10;

SELECT * FROM FREE_T;

-- SELECT STANDARD_HASH('1111', 'SHA256') FROM DUAL;        -- 암호화된 비밀번호 보기


-- 2. 검색

-- 1) 결과 개수
SELECT COUNT(*)
  FROM FREE_T
 WHERE EMAIL LIKE '%' || 'user1' || '%';    -- 오라클의 ||는 자바의 +와 같다. '또는'이 아니라 '문자 연결'임

-- 2) 결과 목록
SELECT FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
  FROM (SELECT ROW_NUMBER() OVER(ORDER BY GROUP_NO DESC, GROUP_ORDER ASC) AS RN, FREE_NO, EMAIL, CONTENTS, CREATED_AT, STATUS, DEPTH, GROUP_NO, GROUP_ORDER
          FROM FREE_T
         WHERE EMAIL LIKE '%' || 'user1' || '%')
 WHERE RN BETWEEN 1 AND 10;


-- 쿼리 테스트

-- 1. 로그인 할 때(이메일, 비밀번호 입력)
-- 1) 정상 회원
SELECT USER_NO, EMAIL, PW, NAME, GENDER, MOBILE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, AGREE, PW_MODIFIED_AT, JOINED_AT
  FROM USER_T
 WHERE EMAIL = 'user1@naver.com'
   AND PW = '0FFE1ABD1A08215353C233D6E009613E95EEC4253832A761AF28FF37AC5A150C';

INSERT INTO ACCESS_T VALUES('user1@naver.com', SYSDATE);
COMMIT;

-- 2) 휴면 회원
SELECT USER_NO, EMAIL, PW, NAME, GENDER, MOBILE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, AGREE, PW_MODIFIED_AT, JOINED_AT
  FROM INACTIVE_USER_T
 WHERE EMAIL = 'user1@naver.com'
   AND PW = '0FFE1ABD1A08215353C233D6E009613E95EEC4253832A761AF28FF37AC5A150C';
-- 이후 휴면 복원으로 이동

-- 2. 이메일 중복 체크
-- 셋 다 NULL 일 때 이메일 사용 가능
SELECT EMAIL
  FROM USER_T
 WHERE EMAIL = 'user4@naver.com';
 
 SELECT EMAIL
  FROM LEAVE_USER_T
 WHERE EMAIL = 'user4@naver.com';
 
 SELECT EMAIL
  FROM INACTIVE_USER_T
 WHERE EMAIL = 'user4@naver.com';


-- 3. 휴면 처리 할 때 (12개월 이상 로그인 이력이 없다. 로그인 이력이 전혀 없는 사용자 중에서 가입일이 12개월 이상 지났다.)
INSERT INTO INACTIVE_USER_T
(
SELECT USER_NO, U.EMAIL, PW, NAME, GENDER, MOBILE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, AGREE, PW_MODIFIED_AT, JOINED_AT, SYSDATE
   FROM USER_T U LEFT OUTER JOIN ACCESS_T A
     ON U.EMAIL = A.EMAIL
  WHERE MONTHS_BETWEEN(SYSDATE, LOGIN_AT) >= 12
     OR (LOGIN_AT IS NULL AND MONTHS_BETWEEN(SYSDATE, JOINED_AT) >= 12)
);

DELETE
  FROM USER_T
 WHERE EMAIL IN(SELECT U.EMAIL 
                  FROM USER_T U LEFT OUTER JOIN ACCESS_T A
                    ON U.EMAIL = A.EMAIL
                 WHERE MONTHS_BETWEEN(SYSDATE, LOGIN_AT) >= 12
                    OR (LOGIN_AT IS NULL AND MONTHS_BETWEEN(SYSDATE, JOINED_AT) >= 12));
COMMIT;

-- 4. 휴면 복원 할 때
-- 복원
INSERT INTO USER_T
(
SELECT USER_NO, EMAIL, PW, NAME, GENDER, MOBILE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, AGREE, PW_MODIFIED_AT, JOINED_AT
  FROM INACTIVE_USER_T
 WHERE EMAIL = 'user2@naver.com'
);

DELETE
  FROM INACTIVE_USER_T
 WHERE EMAIL = 'user2@naver.com';

-- 로그인
SELECT USER_NO, EMAIL, PW, NAME, GENDER, MOBILE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, AGREE, PW_MODIFIED_AT, JOINED_AT
  FROM INACTIVE_USER_T
 WHERE EMAIL = 'user2@naver.com';

-- 접속 기록남기지 않으면 다음 날 다시 휴면이 되므로 접속 기록 남기기
INSERT INTO ACCESS_T VALUES('user2@naver.com', SYSDATE);

COMMIT;
 

-- 5. 탈퇴 할 때
INSERT INTO LEAVE_USER_T VALUES('user1@naver.com', TO_DATE('20220101', 'YYYYMMDD'), SYSDATE);
DELETE FROM USER_T WHERE USER_NO = 1;
COMMIT;


SELECT * FROM USER_T;
SELECT * FROM INACTIVE_USER_T;
SELECT * FROM LEAVE_USER_T;
SELECT * FROM FREE_T;