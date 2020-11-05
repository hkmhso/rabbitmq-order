package com.sunnsoft.rabbitmq.order.codegener;

import cn.hutool.core.text.StrSpliter;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 代码生成器父类
 * </p>
 *
 * @author YZC
 */
public class SuperGenerator {

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 获取TemplateConfig
     * 模板配置,自定义代码模板
     *
     * @return
     */
    protected TemplateConfig getTemplateConfig() {
        //自定义了xml文件的输出目录,此设为空则不生成在mapper/xml目录
        return new TemplateConfig().setXml("");
    }

    /**
     * 获取InjectionConfig
     * 自定义属性注入
     *
     * @return
     */
    protected InjectionConfig getInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                this.setMap(map);
            }
        }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig(
                "/templates/mapper.xml.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return getGenResourcePathTest() + "/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        }));
    }

    /**
     * 获取GlobalConfig
     *
     * @param author 作者名
     * @return
     */
    protected GlobalConfig getGlobalConfig(String author) {
        return new GlobalConfig()
                // 输出目录
                .setOutputDir(getGenTestPath())
                // XMLResultMap
//                .setBaseResultMap(true)
                // XMLcolumList
//                .setBaseColumnList(true)
                // 是否打开输出目录
                .setOpen(false)
                // 作者
                .setAuthor(author);
    }

    /**
     * 获取TableFill策略
     *
     * @return
     */
    protected List<TableFill> getTableFills() {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("create_time" , FieldFill.INSERT));
        tableFillList.add(new TableFill("update_time" , FieldFill.INSERT_UPDATE));
        return tableFillList;
    }

    /**
     * 获取DataSourceConfig
     *
     * @return
     */
    protected DataSourceConfig getDataSourceConfig() {
        String url = "jdbc:mysql://localhost:3306/order?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String driverName = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "root";

        return new DataSourceConfig()
                .setDriverName(driverName)
                .setUsername(username)
                .setPassword(password)
                .setUrl(url);
    }

    /**
     * 获取PackageConfig
     * 包名配置
     *
     * @param parentPackage 父包名
     * @return
     */
    protected PackageConfig getPackageConfig(String parentPackage) {
        return new PackageConfig()
                // 父包名
                .setParent(parentPackage)
                .setController("controller")
                .setEntity("model.entity")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl");
    }

    /**
     * 获取StrategyConfig
     * 数据库表配置
     *
     * @param tableName
     * @return
     */
    protected StrategyConfig getStrategyConfig(String tableName, String tablePrefix) {
        List<TableFill> tableFillList = getTableFills();
        String[] tableNames = StrSpliter.splitToArray(tableName, "," , -1, true, true);
        return new StrategyConfig()
                // 去除前缀
                .setTablePrefix(tablePrefix)
                // 表名生成策略
                .setNaming(NamingStrategy.underline_to_camel)
                // 表填充字段
                .setTableFillList(tableFillList)
                // 实体是否为lombok模型
                .setEntityLombokModel(true)
                // 生成RestController 控制器
                .setRestControllerStyle(true)
                // 需要包含的表名，允许正则表达式
                .setInclude(tableNames)
                // 逻辑删除字段名
                .setLogicDeleteFieldName("del_flag");

    }

    /**
     * 获取AutoGenerator
     *
     * @param tableName
     * @return
     */
    protected AutoGenerator getAutoGenerator(String projectName, String tableName, String tablePrefix, String author, String parentPackage) {
        this.setProjectName(projectName);
        return new AutoGenerator()
                // 全局配置
                .setGlobalConfig(getGlobalConfig(author))
                // 数据源配置
                .setDataSource(getDataSourceConfig())
                // 策略配置
                .setStrategy(getStrategyConfig(tableName, tablePrefix))
                // 包配置
                .setPackageInfo(getPackageConfig(parentPackage))
                .setCfg(getInjectionConfig())
                // 模板配置
                .setTemplate(getTemplateConfig());
    }

    /**
     * 获取根目录
     *
     * @return
     */
    private String getRootPath() {
        String file = Objects.requireNonNull(SuperGenerator.class.getClassLoader().getResource("")).getFile();
        return new File(file).getParentFile().getParentFile().getParent();
    }

    /**
     * 获取JAVA目录
     *
     * @return
     */
    protected String getJavaPath() {
        return getRootPath() + "/src/main/java";
    }

    /**
     * 获取Resource目录
     *
     * @return
     */
    protected String getResourcePath() {
        return getRootPath() + "/src/main/resources";
    }

    /**
     * 获取生成代码模块main/java目录
     **/
    protected String getGenJavaPath() {
        return getRootPath() + "/" + getProjectName() + "/src/main/java";
    }

    /**
     * 获取生成代码模块main目录
     **/
    protected String getGenResourcePath() {
        return getRootPath() + "/" + getProjectName() + "/src/main/resources";
    }

    /**
     * 获取生成代码模块test/java目录
     **/
    protected String getGenTestPath() {
        return getRootPath() + "/" + getProjectName() + "/src/test/java";
    }

    /**
     * 获取生成代码模块test目录
     **/
    protected String getGenResourcePathTest() {
        return getRootPath() + "/" + getProjectName() + "/src/test/resources";
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
