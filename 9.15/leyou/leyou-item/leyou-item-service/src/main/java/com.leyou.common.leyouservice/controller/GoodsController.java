package com.leyou.common.leyouservice.controller;

import com.leyou.common.leyouservice.service.GoodsService;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
        @RequestParam(value = "key",required = false)String key,
        @RequestParam(value = "saleable",required = false)Boolean saleable,
        @RequestParam(value = "page",defaultValue = "1")Integer page,
        @RequestParam(value = "rows",defaultValue = "5")Integer rows
    ){
        PageResult<SpuBo> pageResult=this.goodsService.querySpuBoByPage(key,saleable,page,rows);
        if(CollectionUtils.isEmpty(pageResult.getItems())){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }
}