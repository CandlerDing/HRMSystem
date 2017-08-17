package com.wd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.wd.dao.provider.JobDynaSqlProvider;
import com.wd.domain.Job;
import static com.wd.util.common.HrmConstants.JOBTABLE;

public interface JobDao {

	// 查询所有职位
	@Select("select * from "+JOBTABLE+" ")
	List<Job> selectAllJob();
	
	// 根据id查询职位
	@Select("select * from "+JOBTABLE+" where id = #{id}")
	Job selectById(Integer id);
	
	// 根据id删除职位
	@Delete(" delete from "+JOBTABLE+" where id = #{id} ")
	void deleteById(Integer id);
	
	// 动态查询
	@SelectProvider(type=JobDynaSqlProvider.class,method="selectWithParam")
	List<Job> selectByPage(Map<String, Object> params);
	
	// 根据参数查询职位总数
	@SelectProvider(type=JobDynaSqlProvider.class,method="count")
	Integer count(Map<String, Object> params);
	
	// 动态插入职位
	@SelectProvider(type=JobDynaSqlProvider.class,method="insertJob")
	void save(Job job);
	
	// 动态修改职位
	@SelectProvider(type=JobDynaSqlProvider.class,method="updateJob")
	void update(Job job);
}
