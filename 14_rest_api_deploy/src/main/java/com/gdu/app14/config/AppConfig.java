package com.gdu.app14.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableTransactionManagement    // @Transaction 동작을 허용한다.
@EnableAspectJAutoProxy         // @Aspect 허용 - AOP 동작을 허용한다. (Configuration이 있는 클래스에서 사용)
@EnableScheduling               // @Scheduled를 허용한다.
@MapperScan(basePackages="com.gdu.app14.dao")     // @Mapper를 찾을 패키지
@PropertySource(value="classpath:application.properties")
@Configuration
public class AppConfig {
  
  @Autowired
  private Environment env;

  // HikariConfig : HikariCP를 이용해 DB에 접속할 때 필요한 정보를 처리하는 Hikari 클래스     (얘가 다 해주고)
  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(env.getProperty("spring.datasource.hikari.driver-class-name"));
    hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.hikari.jdbc-url"));
    hikariConfig.setUsername(env.getProperty("spring.datasource.hikari.username"));
    hikariConfig.setPassword(env.getProperty("spring.datasource.hikari.password"));
    return hikariConfig;
  }
  
  // HikariDataSource : CP(Connection Pool)을 처리하는 Hikari 클래스                          (얘는 불러들이기만 하면 된다.)
  @Bean
  public HikariDataSource hikariDataSource() {
    return new HikariDataSource(hikariConfig());
  }

  // SqlSessionFactory : SqlSessionTemplate를 만들기 위한 mybatis 인터페이스
  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {      // application.properties를 기반으로 작성, 오류 처리 필요
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(hikariDataSource());
    sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(env.getProperty("mybatis.config-location")));
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapper-locations")));
    return sqlSessionFactoryBean.getObject();
  }
  
  
  
  // SqlSessionTemplate : 쿼리 실행을 담당하는 mybatis 클래스
  @Bean
  public SqlSessionTemplate sqlSessionTemplate() throws Exception {   // sqlSessionFactory()가 exception을 던지기 때문에 여기서도 Exception을 처리해주어야 한다.
    return new SqlSessionTemplate(sqlSessionFactory());
  }
  
  
  
  // TransactionManager : 트랜잭션을 처리하는 스프링 인터페이스
  @Bean
  public TransactionManager transactionManager() {
    return new DataSourceTransactionManager(hikariDataSource());
  }
  
  
}
