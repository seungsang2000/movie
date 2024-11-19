package egovframework.kss.main.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.CacheManager;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager ehCacheManager() {
		return CacheManager.create(getClass().getResource("/egovframework/spring/ehcache.xml"));
	}

	@Bean
	public EhCacheCacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
}
