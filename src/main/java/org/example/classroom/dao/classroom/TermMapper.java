package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.classroom.model.classroom.Term;

@Mapper
public interface TermMapper extends BaseMapper<Term> {


    @Update("UPDATE term SET week_now = #{weekNow} WHERE is_active = 1")
    void updateActiveTermWeekNow(@Param("weekNow") int weekNow);


    default Boolean deleteByTermId (String TermId){
        return delete(Wrappers.<Term>lambdaQuery()
                .eq(Term::getTermId,TermId))>0;
    }

    default Boolean updateByTermId(Term term) {
        return update(term, Wrappers.<Term>lambdaUpdate()
                .eq(Term::getTermId, term.getTermId())) > 0;
    }

    @Select("SELECT * FROM term WHERE is_active = 1 LIMIT 1")
    Term getActiveTerm();
}
