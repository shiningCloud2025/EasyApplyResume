package com.zyh.easyapplyresume.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 双数据源配置：MySQL（主数据源） + PG（向量数据源）
 */
@Configuration
// 1. 扫描 MySQL 对应的 Mapper 包（让 MyBatis-Plus 识别 MySQL 的 Mapper）
@MapperScan(
        basePackages = "com.zyh.easyapplyresume.mapper.mysql", // 你的 MySQL Mapper 包路径（比如 AdminMapper 所在包）
        sqlSessionFactoryRef = "mysqlSqlSessionFactory", // 绑定 MySQL 的 SqlSessionFactory
        sqlSessionTemplateRef = "mysqlSqlSessionTemplate"
)
// 2. 扫描 PG 对应的 Mapper 包（如果有 PG 的 Mapper，没有则可删除这行）
@MapperScan(
        basePackages = "com.zyh.easyapplyresume.mapper.pgvector", // 若有 PG 的 Mapper 放这里
        sqlSessionFactoryRef = "pgSqlSessionFactory",
        sqlSessionTemplateRef = "pgSqlSessionTemplate"
)
public class DataSourceConfig {

    // ======================== MySQL 数据源配置（主数据源）========================
    /**
     * 1. 注册 MySQL 数据源 Bean
     * @Primary：标记为主数据源，Spring 默认优先使用
     * 名称：mysqlDataSource（需和 yml 中 spring.datasource.mysql 对应）
     */
    @Primary
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql") // 绑定 MySQL 的 yml 配置前缀
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 2. 注册 MySQL 的 SqlSessionFactory（MyBatis-Plus 核心）
     * 用于 MySQL 的 Mapper 操作（比如 AdminMapper）
     */
    @Primary
    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(
            @Qualifier("mysqlDataSource") DataSource dataSource,
            MybatisPlusProperties mybatisPlusProperties) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource); // 绑定 MySQL 数据源
        // 配置 MyBatis-Plus 全局属性（比如驼峰命名、逻辑删除等，复用项目原有配置）
        GlobalConfig globalConfig = mybatisPlusProperties.getGlobalConfig();
        factory.setGlobalConfig(globalConfig);
        // 配置 MyBatis 核心属性（比如日志、类型别名等）
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true); // 驼峰命名自动转换（默认开启，可根据需求调整）
        factory.setConfiguration(configuration);
        // 扫描 MySQL 的 Mapper XML 文件（如果你的 Mapper 用 XML 写法，指定路径）
        factory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:com/zyh/easyapplyresume/mapper/mysql/**/*.xml")); // 你的 MySQL Mapper XML 路径
        return factory.getObject();
    }

    /**
     * 3. 注册 MySQL 的 SqlSessionTemplate（MyBatis-Plus 操作模板）
     */
    @Primary
    @Bean(name = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // ======================== PG 数据源配置（向量数据源）========================
    /**
     * 1. 注册 PG 数据源 Bean
     * 名称：pgvectorDataSource（必须和 yml 中 spring.ai.vectorstore.pgvector.data-source-bean-name 一致！）
     */
    @Bean(name = "pgvectorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pgvector") // 绑定 PG 的 yml 配置前缀
    public DataSource pgDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 2. 注册 PG 的 SqlSessionFactory（如果需要用 MyBatis-Plus 操作 PG，否则可删除）
     * 若仅用 Spring AI 操作 PGVector，无需这步；若要手动写 Mapper 操作 PG，保留这步
     */
    @Bean(name = "pgSqlSessionFactory")
    public SqlSessionFactory pgSqlSessionFactory(
            @Qualifier("pgvectorDataSource") DataSource dataSource,
            MybatisPlusProperties mybatisPlusProperties) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource); // 绑定 PG 数据源
        // 复用 MyBatis-Plus 全局配置
        factory.setGlobalConfig(mybatisPlusProperties.getGlobalConfig());
        // PG 特有的配置（比如向量类型映射，Spring AI 已处理，这里无需额外配置）
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        // 扫描 PG 的 Mapper XML 文件（如有）
        factory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:com/zyh/easyapplyresume/mapper/pgvector/**/*.xml"));
        return factory.getObject();
    }

    /**
     * 3. 注册 PG 的 SqlSessionTemplate（如有 PG 的 Mapper，否则可删除）
     */
    @Bean(name = "pgSqlSessionTemplate")
    public SqlSessionTemplate pgSqlSessionTemplate(
            @Qualifier("pgSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 4. 注册 PG 的 JdbcTemplate（可选，手动操作 PG 时用，Spring AI 无需）
     */
    @Bean(name = "pgJdbcTemplate")
    public JdbcTemplate pgJdbcTemplate(
            @Qualifier("pgvectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}