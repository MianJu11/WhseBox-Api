package com.WhseApi.entity;

import com.WhseApi.validateGroup.AddGroup;
import com.WhseApi.validateGroup.DeleteGroup;
import com.WhseApi.validateGroup.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class TypechoCashBackConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不可为空", groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer id;

    /**
     * 等级差
     */
    @NotNull(message = "等级差不可为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer levelGap;

    /**
     * 百分比返利
     */
    @NotNull(message = "百分比返利不可为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer ratio;
}