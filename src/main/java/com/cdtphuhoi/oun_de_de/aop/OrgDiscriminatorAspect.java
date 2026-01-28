package com.cdtphuhoi.oun_de_de.aop;

import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_FILTER_NAME;
import static com.cdtphuhoi.oun_de_de.common.Constants.ORG_FILTER_PARAM;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrgDiscriminatorAspect {

    @PersistenceContext
    private final EntityManager entityManager;

    @Pointcut("execution(* com.cdtphuhoi.oun_de_de.services..OrgManagementService+.find*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
            authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            var session = entityManager.unwrap(Session.class);
            session.enableFilter(ORG_FILTER_NAME)
                .setParameter(ORG_FILTER_PARAM, principal.getOrgId());
        } else {
            log.error("Could not determine user session");
        }
    }

    @After("pointcut()")
    public void after() {
        entityManager.unwrap(Session.class).disableFilter(ORG_FILTER_NAME);
    }
}
