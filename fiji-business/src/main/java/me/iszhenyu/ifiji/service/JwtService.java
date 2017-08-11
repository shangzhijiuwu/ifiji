package me.iszhenyu.ifiji.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.shiro.RetryLimitHashedCredentialsMatcher;
import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhen.yu
 * @since 2017/6/20
 */
@Service
public class JwtService {

    private Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private RetryLimitHashedCredentialsMatcher credentialsMatcher;

    public String generateJwtToken(UserDO user, String tokenKey, int expireDays) {
        if (expireDays <= 0) {
            expireDays = 10;
        }
        long mis = System.currentTimeMillis() + 3600L * 24 * 1000 * expireDays;
        String finalToken = Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(new Date(mis))
                .signWith(SignatureAlgorithm.HS512, tokenKey)
                .compact();

        logger.info("generate jwt token: {}", finalToken);

        return finalToken;
    }

    public String parseJwtToken(String tokenKey, String tokenString) {
        if (StringUtils.isEmpty(tokenString)) {
            return null;
        }
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(tokenString).getBody();
        } catch (JwtException e) {
            logger.info("jwt parse error, details: {}", e.getMessage());
            return null;
        }
        return claims.getSubject();
    }

    public String encodePasswordWithSalt(String password, String salt) {
        SimpleHash hash = new SimpleHash(credentialsMatcher.getHashAlgorithmName(), password, salt, credentialsMatcher.getHashIterations());
        return hash.toHex();
    }
}
