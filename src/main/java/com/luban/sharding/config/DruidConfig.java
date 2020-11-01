package com.luban.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.luban.sharding.factorybean.DbFactoryBean;
import com.luban.sharding.utils.DbUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Import(DbConfig.class)  另外一种注册多数据源的方法
@Configuration
@MapperScan("com.luban.sharding.mapper")
public class DruidConfig {

    @ConfigurationProperties(prefix = "master.datasource")
    @Bean
    public DataSource masterDataSource(){
        return  new DruidDataSource();
    }

    //mysql的事务使用动态数据源
    @Bean
    public PlatformTransactionManager txManager(DynamicDataSource dynamicDataSource ) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }


    @ConfigurationProperties(prefix = "slave.datasource")
    @Bean
    public DataSource slaveDataSource(){
        return  new DruidDataSource();
    }


    //配置多数据源 将多个数据原注入到DynamicDataSource中
    @Bean
    public DynamicDataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource=new DynamicDataSource();
        Map<Object,Object> map=new HashMap<>();
        map.put(DbUtil.master,masterDataSource());
        map.put(DbUtil.slave,slaveDataSource());
        //设置默认的数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        //设置多数据源
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }




    //整合mybatis   使用动态数据源
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DynamicDataSource dynamicDataSource){
       SqlSessionFactoryBean sqlSessionFactory=new SqlSessionFactoryBean();
       sqlSessionFactory.setDataSource(dynamicDataSource);
       return sqlSessionFactory;
    }

   @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
       SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
       return sqlSessionTemplate;
   }






}
