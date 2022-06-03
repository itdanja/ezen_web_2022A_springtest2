package Springtest2.Entitiy;

import javax.persistence.*;

@Entity // 해당 클래스를 엔티티[ DB내 테이블과 매핑 ] 로 사용
@Table( name = "test") // DB 사용될 테이블 이름 정하기
public class TestEntity { // 클래스
    @Id // 기본키 설정  pk
    @GeneratedValue( strategy = GenerationType.IDENTITY) // 자동번호 auto
    public Integer no;                  // 필드
    @Column( name = "content") // DB에서 사용할 필드명 정하기
    public String content;      // 필드
}
// jpa : 클래스를 DB테이블 처럼 사용하자 !!!!
    // 1. JAVA클래스 를 엔티티 화 하면  DB테이블이 자동 생성된다 !!!

    //      java  <-----------JPA(매핑) JpaRepository : 조작 ------------>   DB
    //     entity클래스                                        table
    //         필드                                                     필드
    //     예)   게시물번호                                       게시물번호
    //             게시물내용                                        게시물내용
