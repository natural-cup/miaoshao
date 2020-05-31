package com.qsh.miaosha.controller;

import com.qsh.miaosha.pojo.User;
import com.qsh.miaosha.redis.RedisService;
import com.qsh.miaosha.result.Result;
import com.qsh.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }

    @RequestMapping("/db/get/{id}")
    @ResponseBody
    public Result<User> dbGet(@PathVariable("id") int id){
        System.out.println(id);
        User user=userService.getById(id);
        return Result.success(user);
    }

//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public String redisGet(){
//        User v1=redisService.get(MiaoshaUserKey.getById,""+1,User.class);
//        return v1.toString();
//    }
//
//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public boolean redisSet(){
//        User user = new User(1, "1111");
//        boolean b = redisService.set(MiaoshaUserKey.getById, "" + 1, user);
//
//        return b;
//    }
}
