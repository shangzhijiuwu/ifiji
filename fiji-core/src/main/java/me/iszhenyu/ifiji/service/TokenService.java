package me.iszhenyu.ifiji.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.iszhenyu.ifiji.model.UserDO;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhen.yu
 * @since 2017/6/20
 */
@Service
public class TokenService {

    public String generateJwtToken(UserDO user, String tokenKey, int expireDays) {
        if (expireDays <= 0) {
            expireDays = 10;
        }
        long mis = 3600 * 24 * 1000 * expireDays;
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(new Date(mis))
                .signWith(SignatureAlgorithm.HS512, tokenKey)
                .compact();
    }

    public boolean isValidJwtToken() {
        return true;
    }


}
