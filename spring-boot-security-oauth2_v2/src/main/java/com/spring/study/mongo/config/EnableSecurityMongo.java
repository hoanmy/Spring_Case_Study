package com.spring.study.mongo.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ MongoConfiguration.class })
public @interface EnableSecurityMongo {
}
