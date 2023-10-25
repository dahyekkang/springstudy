DROP SEQUENCE BBS_SEQ;
CREATE SEQUENCE BBS_SEQ NOCACHE;


DROP TABLE BBS_T;
CREATE TABLE BBS_T (
    BBS_NO      NUMBER              NOT NULL,
    EDITOR      VARCHAR2(50 BYTE)           ,
    TITLE       VARCHAR2(50 BYTE)   NOT NULL,
    CONTENTS    VARCHAR2(1000 BYTE)         ,
    CREATED_AT  DATE                        ,
    MODIFIED_AT DATE                        ,
    CONSTRAINT PK_BBS PRIMARY KEY(BBS_NO)
);

INSERT INTO BBS_T VALUES(BBS_SEQ.NEXTVAL, '작성자', '제목', '내용', SYSDATE, STSDATE);

SELECT * FROM BBS_T;