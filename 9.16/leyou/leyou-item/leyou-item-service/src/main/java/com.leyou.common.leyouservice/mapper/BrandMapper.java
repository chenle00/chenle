package com.leyou.common.leyouservice.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertBrandAndCategory(@Param("cid") Long cid, @Param("bid") Long bid);
    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid,@Param("bid") Long id);

    @Select("select b.* from tb_brand b inner join tb_category_brand cb on" +
            " b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> queryBrandsByCid(Long cid);
}
