package com.gdu.app10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.gdu.app10.aop.BeforeAop;

@EnableAspectJAutoProxy   // AOP 동작을 허용한다. (Configuration이 있는 클래스에서 사용)
@Configuration
public class AppConfig {

  // DriverManagerDataSource : CP(Connection Pool)을 처리하는 스프링 클래스
  @Bean
  public DriverManagerDataSource driverManagerDataSource() {
    DriverManagerDataSource d = new DriverManagerDataSource();
    d.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
    d.setUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:xe");
    d.setUsername("GD");
    d.setPassword("1111");
    return d;
  }
  
  // JdbcTemplate : Jdbc를 처리하는 스프링 클래스(Connection, PreparedStatement, ResultSet 처리 담당)
  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(driverManagerDataSource());
  }
  
  @Bean
  public BeforeAop beforeAop() {
    return new BeforeAop();
  }
  
}
