package org.gdou.marine.biodiversity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.gdou.marine.biodiversity.entity.Species;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpeciesMapper extends BaseMapper<Species> {

    @Select("SELECT DISTINCT ${column} FROM species WHERE ${column} IS NOT NULL AND ${column} != '' ORDER BY ${column}")
    List<String> selectDistinctColumn(@Param("column") String column);
}
