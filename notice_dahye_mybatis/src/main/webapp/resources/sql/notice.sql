DROP SEQUENCE NOTICE_SEQ;
CREATE SEQUENCE NOTICE_SEQ NOCACHE;

DROP TABLE NOTICE_T;
CREATE TABLE NOTICE_T (
    NOTICE_NO NUMBER NOT NULL,           /* 공지번호 */
    GUBUN NUMBER(1) NOT NULL,            /* 공지구분(1='긴급', 2='일반') */
    TITLE VARCHAR2(1000 BYTE) NOT NULL,  /* 제목 */
    CONTENT CLOB                         /* 내용 */
);

INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, 2, '오픈 이벤트', '푸짐한 상품을 증정합니다.');
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, 2, '화면 개편', '메인 화면이 개편됩니다.');
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, 1, '작업 공지', '00 ~ 03시 작업으로 시스템이 중단됩니다.');
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, 1, '점검 공지', '07 ~ 09시 엘리베이터 점검이 있습니다.');
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, 2, '할인 이벤트', '10주년 기념 30% 할인 이벤트를 실시합니다.');
COMMIT;