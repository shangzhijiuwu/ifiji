package me.iszhenyu.ifiji.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@NoArgsConstructor
public class LoginForm {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
