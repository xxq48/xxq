package cn.edu.qjnu.xxq.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
//代码生成工具类
public class CodeGenerator {
    public static void main(String[] args) {
//        数据库的连接信息
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/xxq?serverTimezone=Asia/Shanghai", "root", "114514")
                .globalConfig(builder -> builder
                                .author("xxq")
//                        项目坐在目录地址（抄项目名后面显示的地址）
                                .outputDir("c:/Users/X/IdeaProjects/xxq/src/main/java")
                )
                .packageConfig(builder -> builder
                        .parent("cn.edu.qjnu.xxq")
                )
                .strategyConfig(builder -> {
                    builder.addInclude("product_base"); // 设置需要生成的表名
                })
                .execute();
    }
}