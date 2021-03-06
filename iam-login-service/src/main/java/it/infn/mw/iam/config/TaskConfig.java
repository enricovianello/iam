package it.infn.mw.iam.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.mitre.oauth2.service.OAuth2TokenEntityService;
import org.mitre.openid.connect.service.ApprovedSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {

  @Autowired
  OAuth2TokenEntityService tokenEntityService;

  @Autowired
  ApprovedSiteService approvedSiteService;

  @Bean(destroyMethod = "shutdown")
  public ScheduledExecutorService taskScheduler() {

    // Do we need more than one executor here?
    return Executors.newSingleThreadScheduledExecutor();
  }

  @Scheduled(fixedDelay = 30000, initialDelay = 60000)
  public void clearExpiredTokens() {

    tokenEntityService.clearExpiredTokens();
  }

  @Scheduled(fixedDelay = 30000, initialDelay = 60000)
  public void clearExpiredSites() {

    approvedSiteService.clearExpiredSites();
  }

  @Override
  public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {

    taskRegistrar.setScheduler(taskScheduler());
  }

}
