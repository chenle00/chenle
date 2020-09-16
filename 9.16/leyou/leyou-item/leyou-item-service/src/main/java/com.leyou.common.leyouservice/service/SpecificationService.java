package com.leyou.common.leyouservice.service;

import com.leyou.common.leyouservice.mapper.SpecGroupMapper;
import com.leyou.common.leyouservice.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.groupMapper.select(specGroup);
    }
    @Autowired
    private SpecParamMapper paramMapper;

    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid,Long cid,Boolean generic, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setGeneric(generic);
        param.setSearching(searching);
        return this.paramMapper.select(param);
    }

    public void add(SpecGroup specGroup) {
        groupMapper.insert(specGroup);
    }


    public void update(SpecGroup specGroup) {
        groupMapper.updateByPrimaryKey(specGroup);
    }

    public void delete(Long gid) {
        groupMapper.deleteByPrimaryKey(gid);
    }

    public void addParam(SpecParam specParam) {
        paramMapper.insert(specParam);
    }

    public void updateParam(SpecParam specParam) {
        paramMapper.updateByPrimaryKey(specParam);
    }

    public void deleteParam(Long pid) {
        paramMapper.deleteByPrimaryKey(pid);
    }


}