package com.example;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private PermissionRepository permissionRepository;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        //获取用户认证信息
        System.out.println(authentication.get().getAuthorities());
        Object principal = authentication.get().getPrincipal();
        System.out.println(principal.getClass());
        //判断数据是否为空 以及类型是否正确
        if (null != principal && principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            System.out.println(username);

        }

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        String method = request.getMethod();
        System.out.println(method);

        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
        boolean hasPermission = false;
        for (GrantedAuthority authority : authorities) {
            String authorityname = authority.getAuthority();
            System.out.println(authority.getAuthority());
            Permission permission = permissionRepository.findByName(authorityname);
            System.out.println(permissionRepository.findByName(authorityname));
            if (null != permission && permission.getMethod().equals(request.getMethod()) && antPathMatcher.match(permission.getUri(), request.getRequestURI())) {
                hasPermission = true;

                break;
            }
        }
        System.out.println(hasPermission);
        return new AuthorizationDecision(hasPermission);
    }

}
