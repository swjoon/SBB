package org.sbb.sbb.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String userid, int questionId) {
        redisTemplate.opsForSet().add(userid, questionId);
        redisTemplate.expire(userid, 1, TimeUnit.DAYS);
    }

    public boolean isVisited(String userid, int questionId) {
        Boolean isMember = redisTemplate.opsForSet().isMember(userid, questionId);
        return Boolean.TRUE.equals(isMember);
    }
}
