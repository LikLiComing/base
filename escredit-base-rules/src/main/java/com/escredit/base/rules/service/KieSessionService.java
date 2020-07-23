package com.escredit.base.rules.service;

import com.escredit.base.rules.config.DroolsProperties;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * KieSession服务类
 * 用于设置监听器
 * Created by liyongping on 2020/7/23 8:58 AM
 */
@Service
public class KieSessionService {

    private KieContainer kieContainer;

    private DroolsProperties droolsProperties;

    public KieContainer getKieContainer() {
        return kieContainer;
    }

    public void setKieContainer(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public DroolsProperties getDroolsProperties() {
        return droolsProperties;
    }

    public void setDroolsProperties(DroolsProperties droolsProperties) {
        this.droolsProperties = droolsProperties;
    }

    /**
     * 创建有状态session
     * @param kSessionName
     * @return
     */
    public KieSession newKieSession(String kSessionName){
        KieSession kieSession = kieContainer.newKieSession(kSessionName);
        if(droolsProperties.isDebug()){
            kieSession.addEventListener(new DebugRuleRuntimeEventListener());
            kieSession.addEventListener(new DebugAgendaEventListener());
            kieSession.addEventListener(new DebugProcessEventListener());
        }
        return kieSession;
    }

    /**
     * 创建有状态session，并执行
     * @param kSessionName
     * @param objects
     */
    public void execute(String kSessionName,Object... objects) {
        KieSession kieSession = newKieSession(kSessionName);
        execute(kieSession,objects);
    }

    /**
     * 执行有状态session
     * @param kieSession
     * @param objects
     */
    public void execute(KieSession kieSession,Object... objects) {
        try {
            for ( Object object : objects ) {
                kieSession.insert( object );
            }
            kieSession.fireAllRules();
        } finally {
            dispose(kieSession);
        }
    }

    private void dispose(KieSession ksession) {
        ksession.dispose();
    }
}
