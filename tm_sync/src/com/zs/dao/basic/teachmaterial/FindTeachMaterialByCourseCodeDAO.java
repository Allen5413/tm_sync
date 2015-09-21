package com.zs.dao.basic.teachmaterial;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.TeachMaterial;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/5/9.
 */
public interface FindTeachMaterialByCourseCodeDAO extends EntityJpaDao<TeachMaterial, Long> {
    @Query(nativeQuery = true, value="select tm.* from teach_material tm, teach_material_course tmc where tm.id = tmc.teach_material_id and tm.state = 0 and tmc.course_code = ?1")
    public List<TeachMaterial> getTeachMaterialByCourseCode(String courseCode);
}
