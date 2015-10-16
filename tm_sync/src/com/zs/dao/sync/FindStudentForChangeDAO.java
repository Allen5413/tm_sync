package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Student;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询学生信息有变动的学生
 * Created by Allen on 2015/10/16.
 */
public interface FindStudentForChangeDAO extends EntityJpaDao<Student,Long> {

    @Query(nativeQuery = true, value = "SELECT " +
            "s.id, s.code, s.name, s.sex, s.idcard_type, s.idcard_no, s.postal_code, s.address, s.mobile, s.home_tel, " +
            "s.company_tel, s.email, s.spot_code, s.spec_code, s.level_code, s.type, s.state, s.study_enter_year, s.study_quarter, s.operate_time, s.change_spot_detail," +
            "st.id id_t, st.code code_t, st.name name_t, st.sex sex_t, st.idcard_type idcard_type_t, st.idcard_no idcard_no_t, st.postal_code postal_code_t, st.address address_t, " +
            "st.mobile mobile_t, st.home_tel home_tel_t, st.company_tel company_tel_t, st.email email_t, st.spot_code spot_code_t, st.spec_code spec_code_t, st.level_code level_code_t, " +
            "st.type type_t, st.state state_t, st.enter_year, st.quarter, st.study_enter_year study_enter_year_t, st.study_quarter study_quarter_t, st.operate_time operate_time_t " +
            "FROM sync_student s, sync_student_temp st " +
            "where s.code = st.code and (s.name != st.name " +
            "or s.sex != case when st.sex = 12 then 0 when st.sex = 11 then 1 else st.sex end " +
            "or s.idcard_type != case when st.idcard_type = 180 then 1 when st.idcard_type = 181 then 2 when st.idcard_type = 182 then 3 when st.idcard_type = 183 then 4 when st.idcard_type = 184 then 0 else st.idcard_type end " +
            "or s.idcard_no != st.idcard_no or s.postal_code != st.postal_code or s.address != st.address or s.mobile != st.mobile or s.home_tel != st.home_tel or s.company_tel != st.company_tel or s.email != st.email " +
            "or s.spot_code != st.spot_code or s.spec_code != st.spec_code or s.level_code != st.level_code " +
            "or s.type != case when st.type = 1 then 3 when st.type = 2 then 4 when st.type = 26 then 0 when st.type = 27 then 1 when st.type = 67 then 2 else st.type end " +
            "or s.state != case when st.state = 53 then 1 when st.state = 77 then 2 when st.state = 78 then 3 when st.state = 54 then 0 when st.state = 55 then 4 else st.state end " +
            "or s.study_enter_year != case when st.study_enter_year is null then st.enter_year else st.study_enter_year end " +
            "or s.study_quarter != case when st.study_quarter is null then case when st.quarter = 1 then 0 else 1 end else case when st.study_quarter = 1 then 0 else 1 end end)")
    public List<Object[]> find();
}
