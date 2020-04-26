package app.messages.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityChecker {

    private static final Logger logger = LoggerFactory.getLogger(SecurityCheck.class);

    //    @Pointcut("execution(* app.messages..*.*(..))")
    @Pointcut("@annotation(app.messages.security.SecurityCheck)")
    public void checkMethodSecurity() {
    }

    @Around("checkMethodSecurity()")
    public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Checking method security...");
        // TOOD 여기에 보안 검사 로직 구현하기
        Object result = joinPoint.proceed();
        return result;
    }
}
