package com.leyou.common.leyouservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.leyouservice.mapper.BrandMapper;
import com.leyou.common.leyouservice.mapper.SpuMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Spu;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    SpuMapper spuMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandMapper brandMapper;

    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if (saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        //分页条件
        PageHelper.startPage(page,rows);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        List<SpuBo> spuBos =new ArrayList<>();
        spus.forEach(spu -> {
            SpuBo spuBo = new SpuBo();
            //copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu,spuBo);
           List<String> names= this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            spuBo.setCname(StringUtils.join(names,"/"));
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            spuBos.add(spuBo);
                });

        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }
}
