package com.WhseApi.query;

import com.WhseApi.validateGroup.BuyRenovateGroup;
import com.WhseApi.validateGroup.BuyStickGroup;
import com.WhseApi.validateGroup.ReadGroup;
import com.WhseApi.validateGroup.RenovateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lty
 * 购买轮播/火急/置顶 对象
 */
@Data
public class ContentQuery {
    /**
     * 文章ID
     */
    @NotNull(message = "文章ID不可为空", groups = {BuyStickGroup.class, RenovateGroup.class, ReadGroup.class})
    private Integer contentId;

    /**
     * 标准资费ID
     */
    @NotNull(message = "标准资费ID不可为空", groups = {BuyStickGroup.class, BuyRenovateGroup.class})
    private Integer standardFeeId;

    /**
     * 刷新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date renovateTime;

    /**
     * 间隔时间（分钟）
     */
    private Integer intervalTime;

    /**
     * 刷新次数
     */
    private Integer renovateTimes;
}
