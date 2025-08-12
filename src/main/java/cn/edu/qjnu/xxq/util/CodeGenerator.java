package cn.edu.qjnu.xxq.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/xxq?serverTimezone=Asia/Shanghai", "root", "114514")
                .globalConfig(builder -> {
                    builder.author("yourname") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                            .disableOpenDir(); // 生成后不打开目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.edu.qjnu.xxq") // 父包名
                            .moduleName("") // 模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // mapper.xml路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user_role_merged") // 表名
                            .entityBuilder().enableLombok() // 启用Lombok
                            .controllerBuilder().enableRestStyle(); // 启用REST风格控制器
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用Velocity引擎
                .execute();
    }
}