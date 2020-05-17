package com.qintingfm.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RediesTest {
    @Autowired
    RedisTemplate<String,String> template;
    @Test
    public void testRedisTemplate(){
        template.opsForValue().append("abc","def");
        template.delete("abc");

    }

}
