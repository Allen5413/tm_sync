package com.zs.service.basic.teachmaterial.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.teachmaterial.TeachMaterialDAO;
import com.zs.domain.basic.TeachMaterial;
import com.zs.service.basic.teachmaterial.FindTeachMaterialService;
import org.springframework.stereotype.Service;

/**
 * Created by Allen on 2015/4/29.
 */
@Service("findTeachMaterialService")
public class FindTeachMaterialServiceImpl extends EntityServiceImpl<TeachMaterial, TeachMaterialDAO> implements FindTeachMaterialService {
}
