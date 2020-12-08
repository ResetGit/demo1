package com.example.demo.config;

import com.example.demo.controller.UserController;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String usercookie = "";
        response.setHeader("Access-Control-Allow-Origin", usercookie);
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers","Authorization,usercookie,"+ UserController.loginid +",X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");
        Cookie[] cookies = request.getCookies();
        if(null !=cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(UserController.loginid)){
                    usercookie = cookie.getValue();
                    return true;
                }
            }
        }
        if(null == cookies){
            request.getRequestDispatcher("login.html").forward(request,response);
            return false;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("--------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("---------------视图渲染之后的操作-------------------------");
    }

}
