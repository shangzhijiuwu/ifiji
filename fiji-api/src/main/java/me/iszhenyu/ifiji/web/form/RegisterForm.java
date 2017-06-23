package me.iszhenyu.ifiji.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author zhen.yu
 * @since 2017/6/13
 */
@Setter
@Getter
@NoArgsConstructor
public class RegisterForm {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "重复密码不能为空")
    private String rePassword;
}
