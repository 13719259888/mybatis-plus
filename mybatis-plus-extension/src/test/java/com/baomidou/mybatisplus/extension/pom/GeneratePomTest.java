package com.baomidou.mybatisplus.extension.pom;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.LagartoDOMBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查pom依赖
 *
 * @author nieqiurong 2019/2/9.
 */
class GeneratePomTest {
    
    @Data
    @AllArgsConstructor
    private static class Dependency {
        private String artifactId;
        private String scope;
        private boolean optional;
    }
    
    @Test
    void test() throws IOException {
        try (InputStream inputStream = new FileInputStream("build/publications/mavenJava/pom-default.xml");) {
            Jerry.JerryParser jerryParser = new Jerry.JerryParser(new LagartoDOMBuilder().enableXmlMode());
            Jerry doc = jerryParser.parse(FileUtil.readUTFString(inputStream));
            Jerry dependencies = doc.$("dependencies dependency");
            Map<String, Dependency> dependenciesMap = new HashMap<>();
            dependencies.forEach($this -> {
                String artifactId = $this.$("artifactId").text();
                dependenciesMap.put(artifactId, new Dependency(artifactId, $this.$("scope").text(), Boolean.parseBoolean($this.$("optional").text())));
            });
            Dependency core = dependenciesMap.get("mybatis-plus-core");
            Assertions.assertEquals("compile", core.getScope());
            Assertions.assertFalse(core.isOptional());
            Dependency mybatisSpring = dependenciesMap.get("mybatis-spring");
            Assertions.assertEquals("compile", mybatisSpring.getScope());
            Assertions.assertFalse(mybatisSpring.isOptional());
            Dependency kotlinStdlib = dependenciesMap.get("kotlin-stdlib-jdk8");
            Assertions.assertEquals("compile", kotlinStdlib.getScope());
            Assertions.assertTrue(kotlinStdlib.isOptional());
            Dependency kotlinReflect = dependenciesMap.get("kotlin-reflect");
            Assertions.assertEquals("compile", kotlinReflect.getScope());
            Assertions.assertTrue(kotlinReflect.isOptional());
            Dependency support = dependenciesMap.get("spring-context-support");
            Assertions.assertEquals("compile", support.getScope());
            Assertions.assertTrue(support.isOptional());
            Dependency jdbc = dependenciesMap.get("spring-jdbc");
            Assertions.assertEquals("compile", jdbc.getScope());
            Assertions.assertTrue(jdbc.isOptional());
            Dependency slf4jApi = dependenciesMap.get("slf4j-api");
            Assertions.assertEquals("compile", slf4jApi.getScope());
            Assertions.assertTrue(slf4jApi.isOptional());
        }
    }
    
}
