package me.iszhenyu.ifiji.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
@RestController
public class IndexController extends BaseController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public String hello3(){
        logger.info("just a test!");
        return "hello3,Andy";
    }

    @RequestMapping("/hello")
    public String hello2() {
        return "hello2";
    }
}
