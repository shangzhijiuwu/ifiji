package me.iszhenyu.ifiji.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BaseModel {
	protected Long id;
	protected Boolean deleted; // 删除标记
	protected LocalDateTime createdAt; // 创建时间
	protected LocalDateTime updatedAt; // 修改时间
}
