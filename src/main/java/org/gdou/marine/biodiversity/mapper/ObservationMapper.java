package org.gdou.marine.biodiversity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.gdou.marine.biodiversity.entity.Observation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ObservationMapper extends BaseMapper<Observation> {

    @Select("SELECT COUNT(*) FROM observation_species WHERE observation_id = #{observationId}")
    int countSpeciesByObservationId(@Param("observationId") Long observationId);

    @Select("<script>" +
            "SELECT o.* FROM observation o " +
            "INNER JOIN observation_species os ON o.id = os.observation_id " +
            "WHERE os.species_id = #{speciesId} " +
            "GROUP BY o.id" +
            "</script>")
    List<Observation> selectBySpeciesId(@Param("speciesId") Long speciesId);
}
