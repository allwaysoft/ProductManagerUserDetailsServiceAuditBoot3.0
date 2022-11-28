package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.authentication.CredentialsExpiredException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private LoginLogRepository loginLogRepository;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof CredentialsExpiredException) {
            // 保存异常信息到会话属性，供页面显示
            saveException(request, exception);
            String userName = request.getParameter("username");
            // 跳转到修改密码页面
            response.sendRedirect("/changepassword?error&username=" + userName);
        }

        String username = request.getParameter("username");
        User user = userRepository.getByUsername(username);
        if (user != null) {
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setDescription("登录失败");
            loginLog.setIp(request.getRemoteAddr());
            loginLog.setEventtime(new Date());
            loginLog.setSessionid(request.getSession().getId());
            loginLogRepository.save(loginLog);
            if (user.getEnabled() && user.getAccountNonLocked()) {
                if (user.getFailedAttempt() < userLoginService.MAX_FAILED_ATTEMPTS - 1) {
                    System.out.println("user.getFailedAttempt()=" + user.getFailedAttempt());
                    userLoginService.increaseFailedAttempts(user);
                } else {

                    userLoginService.lock(user);
                    exception = new LockedException("your account has been locked due to 3 failed attempt"
                            + " It will be unclocked after 15 minutes");
                    System.out.println(exception);
                    System.out.println("userLoginService.lock(user)");
                }
            } else if (!user.getAccountNonLocked()) {
                if (userLoginService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("your account has been unclock ."
                            + " please try to login again");
                    System.out.println(exception);
                }
            }
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
