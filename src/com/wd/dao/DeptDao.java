package com.wd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.wd.dao.provider.DeptDynaSqlProvider;
import com.wd.domain.Dept;
import static com.wd.util.common.HrmConstants.DEPTTABLE;

public interface DeptDao {

	//查询所有部门
	@Select("select * from "+DEPTTABLE+" ")
	List<Dept> selectAllDept();
	
	//根据id查询部门
	@Select("select * from "+DEPTTABLE+" where id = #{id}")
	Dept selectById(Integer id);

	// 根据id删除部门
	@Delete("delete from "+DEPTTABLE+" where id = #{id} ")
	void deleteById(Integer id);
	
	// 动态查询
	@SelectProvider(type=DeptDynaSqlProvider.class,method="selectWithParam")
	List<Dept> selectByPage(Map<String, Object> params);
	
	// 根据参数查询部门总数
	@SelectProvider(type=DeptDynaSqlProvider.class,method="count")
	Integer count(Map<String, Object> params);
		
	// 动态插入部门
	@SelectProvider(type=DeptDynaSqlProvider.class,method="insertDept")
	void save(Dept dept);
	
	// 动态修改部门
	@SelectProvider(type=DeptDynaSqlProvider.class,method="updateDept")
	void update(Dept dept);
}
