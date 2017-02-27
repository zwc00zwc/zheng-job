package annotation.handler;

import annotation.exception.LoginException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XR on 2016/8/29.
 */
public class AuthExceptionHandler implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //LogUtility.insertLog(e.getClass().getName(),e.getMessage());
        String requestheader= httpServletRequest.getHeader("X-Requested-With");
        if (e instanceof LoginException){
            if (requestheader!=null){
                return new ModelAndView("redirect:/nologin");
            }
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/404");
    }
}
