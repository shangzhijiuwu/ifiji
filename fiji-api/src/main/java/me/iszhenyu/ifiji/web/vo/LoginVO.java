package me.iszhenyu.ifiji.web.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.iszhenyu.ifiji.model.UserDO;

/**
 * @author zhen.yu
 * @since 2017/6/15
 */
@Setter
@Getter
@NoArgsConstructor
public class LoginVO {
    private String token;
    private UserDO user;
}
