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
public class TypechoStandardFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不可为空", groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer id;

    /**
     * 类型：1-置顶；2-轮播；3-火急；4-刷新次数
     */
    @NotNull(message = "类型不可为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer type;

    /**
     * 天数/次数
     */
    @NotNull(message = "天数/次数不可为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer times;

    /**
     * 价格
     */
    @NotNull(message = "价格不可为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer price;
}