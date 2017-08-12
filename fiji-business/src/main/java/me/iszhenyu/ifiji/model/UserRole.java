package me.iszhenyu.ifiji.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhen.yu
 * @since 2017/8/12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable {
    private static final long serialVersionUID = -555704234101488955L;

    private Long userId;
    private Long roleId;
}
