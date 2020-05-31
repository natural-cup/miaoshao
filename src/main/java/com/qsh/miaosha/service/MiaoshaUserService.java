package com.qsh.miaosha.service;

import com.qsh.miaosha.dao.MiaoshaUserDao;
import com.qsh.miaosha.exception.GlobalException;
import com.qsh.miaosha.pojo.MiaoshaUser;
import com.qsh.miaosha.redis.MiaoshaUserKey;
import com.qsh.miaosha.redis.RedisService;
import com.qsh.miaosha.result.CodeMsg;
import com.qsh.miaosha.util.MD5Util;
import com.qsh.miaosha.util.UUIDUtil;
import com.qsh.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKI_NAME_TOKEN="token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean login(HttpServletResponse response,LoginVo loginVo){
        if (loginVo==null)
            throw new GlobalException(CodeMsg.SERVER_ERROR) ;
        String mobile = loginVo.getMobile();
        String formpassword = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user==null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formpassword, saltDB);
        if(!calcPass.equals(dbPass))
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        //生成cookie
        addCookie(response,user);
//        String token= UUIDUtil.uuid();
//        redisService.set(MiaoshaUserKey.token,token,user);
//        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//        cookie.setPath("/");
//        response.addCookie(cookie);

        return true;
    }

    //生成cookie
    private void addCookie(HttpServletResponse response,MiaoshaUser user){
        String token= UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token))
            return null;
        MiaoshaUser user= redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //延长有效期
        //生成cookie
        if (user!=null){
            addCookie(response,user);
        }
        return user;
    }
}
