package com.leyou.common.leyouservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.leyouservice.mapper.*;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    SpuDetailMapper spuDetailMapper;
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insert(spuBo);

        // 新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insert(spuDetail);

        saveSkuAndStock(spuBo);
    }

    @Autowired
    SkuMapper skuMapper;
    @Autowired
    StockMapper stockMapper;
    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        });
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku=new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(s.getId());
            s.setStock(stock.getStock());
        });
        return skus;

    }

    public void updateGoods(SpuBo spuBo) {
        //查询以前sku
        List<Sku> skus = this.querySkusBySpuId(spuBo.getId());
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)){
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId",ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku sku = new Sku();
            sku.setSpuId(spuBo.getId());
            this.skuMapper.delete(sku);
        }
        // 新增sku和库存
        saveSkuAndStock(spuBo);
        //更新spu
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKey(spuBo);
        this.spuDetailMapper.updateByPrimaryKey(spuBo.getSpuDetail());
    }
}
