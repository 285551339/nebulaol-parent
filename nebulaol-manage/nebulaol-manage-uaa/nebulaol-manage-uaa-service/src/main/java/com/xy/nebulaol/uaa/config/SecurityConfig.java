package com.xy.nebulaol.uaa.config;import com.xy.nebulaol.uaa.Service.SysUserServiceImpl;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.data.redis.connection.RedisConnectionFactory;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.oauth2.provider.token.TokenStore;import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;@Configuration@EnableWebSecurity()public class SecurityConfig extends WebSecurityConfigurerAdapter {    @Autowired    private SysUserServiceImpl userService;    @Autowired    private RedisConnectionFactory redisConnectionFactory;    @Bean    public PasswordEncoder passwordEncoder(){        return new BCryptPasswordEncoder();    }    @Override    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {        // 获取用户信息        authBuilder.userDetailsService(userService);    }    /**     * 1:     * 请求授权:     * spring security 使用以下匹配器来匹配请求路劲：     *      antMatchers:使用ant风格的路劲匹配     *      regexMatchers:使用正则表达式匹配路劲     * anyRequest:匹配所有请求路劲     * 在匹配了请求路劲后，需要针对当前用户的信息对请求路劲进行安全处理。     * 2:定制登录行为。     *      formLogin()方法定制登录操作     *      loginPage()方法定制登录页面访问地址     *      defaultSuccessUrl()登录成功后转向的页面     *      permitAll()     */    @Override    protected void configure(HttpSecurity http) throws Exception {        //拦截所有请求 通过httpBasic进行认证        http.authorizeRequests().antMatchers("/**").fullyAuthenticated().and().httpBasic();    }    /**     * 密码模式下必须注入的bean authenticationManagerBean     * 认证是由 AuthenticationManager 来管理的，     * 但是真正进行认证的是 AuthenticationManager 中定义的AuthenticationProvider。     *  AuthenticationManager 中可以定义有多个 AuthenticationProvider     */    @Bean    @Override    public AuthenticationManager authenticationManagerBean() throws Exception {        // oauth2 密码模式需要拿到这个bean        return super.authenticationManagerBean();    }    /**     * @Title: tokenStore     * @Description: 用户验证信息的保存策略，可以存储在内存中，关系型数据库中，redis中     * @param     * @return TokenStore     * @throws     */    @Bean    public TokenStore tokenStore(){        return new RedisTokenStore(redisConnectionFactory);    }}