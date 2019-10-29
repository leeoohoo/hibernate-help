package com.learn.hibernate.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class PostProcessingFilter implements WebFilter {
    private final RedisComponent redisComponent;

    public PostProcessingFilter(RedisComponent redisComponent) {
        this.redisComponent = redisComponent;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        log.info("---------------------------------------------------开始抓取上下文-------------------------------------");
        ServerHttpRequest request = serverWebExchange.getRequest();
        setCurrentUser(request);
        return webFilterChain.filter(serverWebExchange).doFinally(signalType -> {
            CurrentUser.clearCurrentUser();
        });

    }

    private void setCurrentUser(ServerHttpRequest request){
        var headers = request.getHeaders();
        if(headers.containsKey("Authorization")) {
            var token = headers.get("Authorization").get(0);
            token = token.substring(7);
            var userStr = redisComponent.sentinelGet(token);
            log.info("------------------------------------------->>>>>>>>>>>抓取到上下文，userStr:{}",userStr);
            if(null != userStr && !"".equals(userStr)){
                CurrentUserDto user = JSONObject.parseObject(userStr, CurrentUserDto.class);
                if(null != user) {
                    user.setPassword(token);
                    CurrentUser.setCurrentUser(user);
                }
            }
        }
    }
}
