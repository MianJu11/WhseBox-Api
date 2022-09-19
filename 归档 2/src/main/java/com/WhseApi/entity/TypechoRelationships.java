package com.WhseApi.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * TypechoRelationships
 * @author iOSWki 2021-11-29
 */
@Data
public class TypechoRelationships implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * cid  
     */
    private Integer cid;

    /**
     * mid  
     */
    private Integer mid;

    private TypechoContents contents;
}