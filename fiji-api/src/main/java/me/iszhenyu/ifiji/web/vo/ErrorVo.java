package me.iszhenyu.ifiji.web.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@NoArgsConstructor
public class ErrorVo {
    private int status;
    private String errorMessage;
}
