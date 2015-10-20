package com.zs.dao.basic.teachmaterial;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.TeachMaterial;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/20.
 */
public interface FindTeachMaterialByNameAndAuthorDAO extends EntityJpaDao<TeachMaterial, Long> {
    @Query("from TeachMaterial where name = ?1 and author = ?2")
    public List<TeachMaterial> find(String name, String author);
}
