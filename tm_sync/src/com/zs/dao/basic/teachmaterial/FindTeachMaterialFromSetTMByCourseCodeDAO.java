package com.zs.dao.basic.teachmaterial;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.TeachMaterial;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询一个课程关联的套教材所包含的教材
 * Created by Allen on 2015/5/11.
 */
public interface FindTeachMaterialFromSetTMByCourseCodeDAO extends EntityJpaDao<TeachMaterial, Long> {
    @Query(nativeQuery = true, value="select tm.* from set_teach_material stm, set_teach_material_tm stmtm, teach_material tm " +
            "where tm.id = stmtm.teach_material_id and stmtm.set_teach_material_id = stm.id " +
            "and tm.state = 0 and stm.buy_course_code = ?1")
    public List<TeachMaterial> getTeachMaterialFromSetTMByCourseCode(String courseCode);
}
