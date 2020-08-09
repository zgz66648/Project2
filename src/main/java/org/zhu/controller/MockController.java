package org.zhu.controller;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@ResponseBody
@RequestMapping(value = "/mock")
public class MockController {
    private final static Logger LOGGER = LogManager.getLogger("AllLogger");
    private final Lock lock = new ReentrantLock();
    Map<String, Object> map = new ConcurrentHashMap<>(1001);

    @RequestMapping(value = "/set/{key}")
    public String setMock(@PathVariable("key") String key, @RequestBody String body){
        lock.lock();
        if (map.size() == 1000) {
            map.clear();
        }
        if (key.trim().length() > 1000) {
            key = key.trim().substring(0, 1000);
        }
        if (body.trim().length() > 1000) {
            body = body.trim().substring(0, 1000);
        }
        map.put(key.trim(), body.trim());
        lock.unlock();

        LOGGER.info("SetMock: key = " + key.trim());

        String result = "{\"returnCode\":\"0\"}";
        return result;
    }

    @RequestMapping(value = "/get/{key}")
    public String getMock(@PathVariable("key") String key){
        lock.lock();
        if (key.trim().length() > 1000) {
            key = key.trim().substring(0, 1000);
        }
        String result = (String) map.get(key.trim());
        lock.unlock();

        LOGGER.info("GetMock: key = " + key.trim() + ", result = " + result);

        return result;
    }
}
