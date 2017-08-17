package com.wd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import com.wd.dao.provider.NoticeDynaSqlProvider;
import com.wd.domain.Notice;
import static com.wd.util.common.HrmConstants.NOTICETABLE;;

public interface NoticeDao {


	//根据id查询公告	
	@Select("select * from "+NOTICETABLE+" where ID = #{id}")
	Notice selectById(int id);
	
	// 根据id删除公告
	@Delete(" delete from "+NOTICETABLE+" where id = #{id} ")
	void deleteById(Integer id);
		
	// 动态查询公告
	@SelectProvider(type=NoticeDynaSqlProvider.class,method="selectWithParam")
	@Results({
		@Result(id=true,column="id",property="id"),
		@Result(column="CREATE_DATE",property="createDate",javaType=java.util.Date.class),
		@Result(column="USER_ID",property="user",
			one=@One(select="com.wd.dao.UserDao.selectById",
		fetchType=FetchType.EAGER))
	})
	List<Notice> selectByPage(Map<String, Object> params);
	
	//动态查询公告总数
	@SelectProvider(type=NoticeDynaSqlProvider.class,method="count")
	Integer count(Map<String, Object> params);
	
	// 动态插入公告
	@SelectProvider(type=NoticeDynaSqlProvider.class,method="insertNotice")
	void save(Notice notice);
		
	// 动态修改公告
	@SelectProvider(type=NoticeDynaSqlProvider.class,method="updateNotice")
	void update(Notice notice);
	
}
