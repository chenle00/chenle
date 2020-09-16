package com.leyou.common.leyouservice.controller;

import com.leyou.common.leyouservice.service.SpecificationService;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;



    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }
    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean searching

    ){
        List<SpecParam>  params = this.specificationService.queryParams(gid,cid,generic,searching);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    @PostMapping("group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup specGroup){
        specificationService.add(specGroup);
        return  ResponseEntity.ok().build();
    }
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        specificationService.update(specGroup);
        return ResponseEntity.ok().build();
    }
    @RequestMapping("gruop/{gid}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("gid")Long gid){
        specificationService.delete(gid);
        return ResponseEntity.ok().build();

    }
    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam){
        specificationService.addParam(specParam);
        return  ResponseEntity.ok().build();
    }
    @PutMapping("param")
    public ResponseEntity<Void> updatePaaram(@RequestBody SpecParam specParam){
        specificationService.updateParam(specParam);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("param/{pid}")
    public ResponseEntity<Void> deleteParam(@PathVariable("pid") Long pid){
        specificationService.deleteParam(pid);
        return ResponseEntity.ok().build();
    }


}