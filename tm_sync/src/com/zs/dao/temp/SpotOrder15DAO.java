package com.zs.dao.temp;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.domain.sync.Student;
import com.zs.domain.temp.SpotOrder15;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

/**
 * Created by Allen on 2015/10/29.
 */
public interface SpotOrder15DAO extends EntityJpaDao<Student, Long> {
    @Query(nativeQuery = true, value = "select * from spot_order15 where is_do = 0 and spot_code = ?1")
    public List<Object[]> find(String code);

    @Query(nativeQuery = true, value = "select sso.*, ifnull(sp.money, 0) money from student_order15 sso INNER JOIN sync_student s on sso.student_code = s.code and s.spot_code = ?1 " +
            "LEFT JOIN student_expense_pay sp on sso.student_code = sp.student_code " +
            "order by money desc, student_code")
    public List<Object[]> findStudent(String code);

    @Query(nativeQuery = true, value = "select so.spot_code, so.course_code, so.price, so.name, so.author, " +
            "sum(so.count) from spot_order15_2 so " +
            "group by so.spot_code, so.course_code, so.price, so.name, so.author order by spot_code")
    public List<Object[]> findSpotOrderForHANSHOU();

    @Modifying
    @Query(nativeQuery = true, value = "delete sso from student_order15 sso INNER JOIN (select t.* from " +
            "(select sso.* from student_order15 sso, sync_student s where sso.student_code = s.code and s.spot_code = ?1) t " +
            "where not exists(select * from spot_order15 soo where t.name = soo.name and soo.spot_code = ?1) ) ttt " +
            "on sso.student_code = ttt.student_code and sso.enter_year = ttt.enter_year and sso.quarter = ttt.quarter and sso.name = ttt.name and sso.isbn = ttt.isbn and sso.author = ttt.author " +
            "and sso.price = ttt.price")
    public void delNotExists(String code);

    @Query(nativeQuery = true, value = "SELECT DISTINCT so.student_code FROM student_order15_2 so where so.spot_code = ?1 ")
    public List<String> findStudent2(String spotCode);

    @Query(nativeQuery = true, value = "SELECT course_code, name, author, price FROM student_order15_2 where student_code = ?1")
    public List<Object[]> findStudent2ByStudentCode(String studentCode);

    @Query(nativeQuery = true, value ="SELECT DISTINCT tmpo.* FROM teach_material_place_order tmpo, place_order_teach_material potm where tmpo.id = potm.order_id and tmpo.semester_id = 2 and tmpo.order_status > '3' " +
            "and (potm.count is null or potm.tm_price is null)")
    public List<Object[]> getT();

    @Query(nativeQuery = true, value = "select t.* FROM " +
            "(SELECT potm.id, potm.order_id, c.code AS courseCode, c.name AS courseName, tm.id AS tmId, tm.name AS tmName, tm.price, potm.count, potm.creator, potm.create_time " +
            "FROM place_order_teach_material potm, teach_material tm, teach_material_course tmc, sync_course c " +
            "WHERE potm.course_code = tmc.course_code AND tmc.teach_material_id = tm.id AND tmc.course_code = c.code " +
            "AND tm.state = 0 AND potm.order_id = ?1 " +
            "UNION ALL " +
            "SELECT potm.id, potm.order_id, c.code AS courseCode, c.name AS courseName, tm.id AS tmId, tm.name AS tmName, tm.price, potm.count, potm.creator, potm.create_time " +
            "FROM place_order_teach_material potm, teach_material tm, set_teach_material stm, set_teach_material_tm stmtm, sync_course c " +
            "WHERE potm.course_code = stm.buy_course_code AND stm.id = stmtm.set_teach_material_id AND stmtm.teach_material_id = tm.id " +
            "AND stm.buy_course_code = c.code AND tm.state = 0 AND potm.order_id = ?1) t ")
    public List<Object[]> getT2(long orderId);
}
