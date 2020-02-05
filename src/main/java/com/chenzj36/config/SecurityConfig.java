package com.chenzj36.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author Danny Lyons
 * @Email chenzj36@live.cn
 * @Time 2020/2/5 17:52
 * @Description SpringSecurity的配置文档
 */

//AOP
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人可以访问，其他功能页根据角色权限
        //请求授权的规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        //开启登录页面，没有权限跳到登录页/toLogin,自定义登录页,最后的/login与提交用户名密码的表单提交地址要一致
        http.formLogin().loginPage("/toLogin").loginProcessingUrl("/login");

        //开启注销功能
        http.logout();

        //关闭跨站脚本攻击防御，注销使用Get，不安全
        http.csrf().disable();

        //开启记住我功能,后为所识别的name
        http.rememberMe().rememberMeParameter("remember");
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //从内存中读取用户信息，从数据库中读取同理，点开继承的类，下载源码，该方法上的注释中有详细说明
        auth.inMemoryAuthentication()
                //高版本Security必须设置加密
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("chenzj").password(new BCryptPasswordEncoder().encode("3")).roles("vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("3")).roles("vip1","vip2","vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("3")).roles("vip1");
    }
}
