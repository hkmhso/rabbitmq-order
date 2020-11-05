package com.sunnsoft.rabbitmq.order;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.sunnsoft.rabbitmq.order.codegener.SuperGenerator;

/**
 * 代码生成
 *
 * @author yzc
 * @date 2019/10/28 14:31
 */
public class CodeGenerator extends SuperGenerator {

    /**
     * <p>
     * MySQL generator
     * </p>
     */
    public void generator(String projectName, String tableName, String tablePrefix, String author, String parentPackage) {

        // 代码生成器
        AutoGenerator mpg = getAutoGenerator(projectName, tableName, tablePrefix, author, parentPackage);
        mpg.execute();
        if (tableName == null) {
            System.err.println(" Generator Success !");
        } else {
            System.err.println(" TableName【 " + tableName + " 】" + "Generator Success !");

        }
    }

    /**
     * 数据库相关配置写死的,如有报错请修改
     * @param args
     */
    public static void main(String[] args) {
        // 作者名
        String author = "hkm";
        // 表名
        String tableName = "torder,torder_user";

        // 去除表前缀
        String tablePrefix = "";

        // 项目名
        String projectName = "rabbitmq-order-codegener";
        // 父包名
        String parentPackage = "com.sunnsoft.rabbitmq.order.codegener";

        new CodeGenerator().generator(projectName, tableName, tablePrefix, author, parentPackage);
    }

}
