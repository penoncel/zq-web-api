//package com.mer.framework.generator;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.rules.DbType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SuppressWarnings("all")
//public class generator {
//
//    /**文件生成路径**/
//    private static final String dirPath = "G:/zzzzzzztest/";
//
//    /**数据库类型**/
//    private static final DbType type = DbType.MYSQL;
//
//    /**数据库配置**/
//    private static final String Driver = "com.mysql.jdbc.Driver";
//    private static final String url = "jdbc:mysql://127.0.0.1:3306/boot-web?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC";
//    private static final String user = "root";
//    private static final String pwd = "root";
//
//    public static void main(String[] args) {
//        //代码生成器
//        AutoGenerator generator = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gcf = new GlobalConfig();
//        gcf.setOutputDir(dirPath);   // 生成文件的输出目录
//        gcf.setFileOverride(true);   // 是否覆盖已有文件
//        gcf.setEnableCache(true);    // 是否在xml中添加二级缓存配置
//        gcf.setAuthor("zhaoqi");     // 开发人员
//        gcf.setActiveRecord(true);   // 开启 ActiveRecord 模式 , 不需要ActiveRecord特性的请改为false
//        gcf.setBaseResultMap(true);  // 开启 BaseResultMap
//        gcf.setBaseColumnList(true); // 开启 baseColumnList
//
//        //自定义文件命名，注意 %s 会自动填充表实体属性！
//        gcf.setMapperName("%sDao");
//        gcf.setServiceName("%sService");
//        gcf.setXmlName("%sMapper");
//        generator.setGlobalConfig(gcf);
//
//        // 数据源配置
//        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setDbType(DbType.MYSQL);
////        dataSourceConfig.setTypeConvert(new MySqlTypeConvert(){
////            // 自定义数据库表字段类型转换【可选】
////            @Override
////            public DbColumnType processTypeConvert(String fieldType) {
////                System.out.println("转换类型：" + fieldType);
////                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
////                return super.processTypeConvert(fieldType);
////            }
////        });
//        dataSourceConfig.setDriverName(Driver);
//        dataSourceConfig.setUrl(url);
//        dataSourceConfig.setUsername(user);
//        dataSourceConfig.setPassword(pwd);
//
//        generator.setDataSource(dataSourceConfig);
//
//        // 数据库表配置
//        StrategyConfig strategy = new StrategyConfig();
//        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
////        strategy.setTablePrefix(new String[] { "tb_", "tsys_" });// 此处可以修改为您的表前缀
//        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//        strategy.setInclude(new String[] { "sys_user" }); // 需要生成的表
//        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
//        // 自定义实体父类
//        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
//        // 自定义实体，公共字段
//        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
//        // 自定义 mapper 父类
//        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
//        // 自定义 service 父类
//        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
//        // 自定义 service 实现类父类
//        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
//        // 自定义 controller 父类
//        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
//        // 【实体】是否生成字段常量（默认 false）
//        // public static final String ID = "test_id";
//        // strategy.setEntityColumnConstant(true);
//        // 【实体】是否为构建者模型（默认 false）
//        // public User setName(String name) {this.name = name; return this;}
//        strategy.setEntityBuilderModel(true);
//        generator.setStrategy(strategy);
//
//        // 包名配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com");
//        pc.setModuleName("mer");
//        pc.setController("controller");
//        pc.setEntity("pojo");
//        pc.setMapper("dao");
//        pc.setService("service");
//        pc.setServiceImpl("serviceImpl");
//        pc.setXml("mybatis-mappers");
//
//        generator.setPackageInfo(pc);
//
//        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
//            }
//        };
//
//        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
///*        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        generator.setCfg(cfg);*/
//
//        // 调整 xml 生成目录演示
///*        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return dirPath + tableInfo.getEntityName() + "Mapper.xml";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        */
//        generator.setCfg(cfg);
//
//        // 关闭默认 xml 生成，调整生成 至 根目录
///*        TemplateConfig tc = new TemplateConfig();
//        tc.setXml(null);
//        generator.setTemplate(tc);*/
//
//        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
//        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
//        TemplateConfig tc = new TemplateConfig();
//        tc.setController("generator/controller.java.vm");
//        tc.setMapper("generator/dao.java.vm");
//        tc.setEntity("generator/entity.java.vm");
//        tc.setService("generator/service.java.vm");
//        tc.setServiceImpl("generator/serviceImpl.java.vm");
//        tc.setXml("generator/mapper.xml.vm");
//
//        generator.setTemplate(tc);
//
//        // 执行生成
//        generator.execute();
//
//        // 打印注入设置【可无】
//        System.err.println(generator.getCfg().getMap().get("abc"));
//    }
//
//
//
//
//
//
//
//}
