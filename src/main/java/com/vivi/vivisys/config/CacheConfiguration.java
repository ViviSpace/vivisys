package com.vivi.vivisys.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.vivi.vivisys.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Serv.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Serv.class.getName() + ".resources", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Serv.class.getName() + ".serviceProviders", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Serv.class.getName() + ".agents", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.ServiceProvider.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.ServiceProvider.class.getName() + ".servs", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Resource.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Agent.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Agent.class.getName() + ".servs", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Ord.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Ord.class.getName() + ".resourceDeploys", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Ord.class.getName() + ".spDeploys", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.ResourceDeploy.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.SpDeploy.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.CustomerIncome.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.AgentCost.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.ResourceCost.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.SpCost.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Problem.class.getName(), jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.Problem.class.getName() + ".problemOrders", jcacheConfiguration);
            cm.createCache(com.vivi.vivisys.domain.ProblemOrder.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
