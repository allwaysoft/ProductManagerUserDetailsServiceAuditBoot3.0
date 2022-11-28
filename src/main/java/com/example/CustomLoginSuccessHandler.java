package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginLogRepository loginLogRepository;

    private static final long PASSWORD_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;    // 30 days

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.getByUsername(username);
        if (user.getFailedAttempt() > 0) {
            userLoginService.resetFailedAttempts(user.getUsername());
        }
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getSession().getId());
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setDescription("登录成功");
        loginLog.setIp(request.getRemoteAddr());
        loginLog.setEventtime(new Date());
        loginLog.setSessionid(request.getSession().getId());
        loginLogRepository.save(loginLog);

        if (user.getPasswordChangedTime() == null) {
            String redirectURL = request.getContextPath() + "/change/password";
            response.sendRedirect(redirectURL);

        } else {
            long currentTime = System.currentTimeMillis();
            long lastChangedTime = user.getPasswordChangedTime().getTime();

            if (currentTime > lastChangedTime + PASSWORD_EXPIRATION_TIME) {
                System.out.println("User:" + user.getUsername() + ":password expired");
                System.out.println("Last Time password changed:" + user.getPasswordChangedTime());
                System.out.println("Current Time:" + new Date());

                String redirectURL = request.getContextPath() + "/change/password";
                response.sendRedirect(redirectURL);
            } else {

                System.out.println(request.getContextPath() + user.getHomepage());
                response.sendRedirect(request.getContextPath() + user.getHomepage());
//        super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }
}
