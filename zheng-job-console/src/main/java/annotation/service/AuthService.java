package annotation.service;

import annotation.Auth;
import annotation.exception.LoginException;
import common.AuthUser;
import common.Constants;
import common.PermissionUtility;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by XR on 2016/8/29.
 */
@Aspect
@Component
public class AuthService {
    @Pointcut("@annotation(annotation.Auth)")
    public void methodPointcut(){

    }

    @Before("@annotation(auth)")
    public void before(Auth auth) {
        System.out.println("before方法");
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request= requestAttributes.getRequest();
        AuthUser authUser=(AuthUser) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
        if (authUser==null){
            throw new LoginException("未登陆");
        }
    }
}
