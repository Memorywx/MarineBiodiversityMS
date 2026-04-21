package org.gdou.marine.biodiversity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.gdou.marine.biodiversity.entity.Ecosystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EcosystemMapper extends BaseMapper<Ecosystem> {

    @Select("SELECT id, name FROM ecosystem ORDER BY id")
    List<Ecosystem> selectAllSimple();
}
