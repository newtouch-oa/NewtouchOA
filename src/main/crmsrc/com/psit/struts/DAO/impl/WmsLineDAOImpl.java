package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsLineDAO;
import com.psit.struts.entity.WmsLine;
/**
 * 
 * �����ˮDAOʵ���� <br>
 *
 * create_date: Aug 18, 2010,4:37:13 PM<br>
 * @author zjr
 */
@SuppressWarnings("unchecked")
public class WmsLineDAOImpl extends HibernateDaoSupport implements WmsLineDAO{
	private static final Log log = LogFactory.getLog(WmsLineDAOImpl.class);
	// property constants
	public static final String WLI_TYPE_CODE = "wliTypeCode";
	public static final String WLI_TYPE = "wliType";
	public static final String WLI_IN_NUM = "wliInNum";
	public static final String WLI_OUT_NUM = "wliOutNum";
	public static final String WLI_STATE = "wliState";
	public static final String WLI_MAN = "wliMan";

//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * ��������ˮ <br>
	 * create_date: Aug 12, 2010,10:43:10 AM <br>
	 * @param transientInstance �����ˮʵ��
	 */
	public void save(WmsLine transientInstance) {
		log.debug("saving WmsLine instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ɾ�������ˮ <br>
	 * create_date: Aug 12, 2010,10:56:27 AM <br>
	 * @param persistentInstance �����ˮʵ��
	 */
	public void delete(WmsLine persistentInstance) {
		log.debug("deleting WmsLine instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ����Id��ÿ����ˮ <br>
	 * create_date: Aug 12, 2010,10:56:46 AM <br>
	 * @param id �����ˮid
	 * @return WmsLine ���ؿ����ˮʵ��
	 */
	public WmsLine findById(java.lang.Long id) {
		log.debug("getting WmsLine instance with id: " + id);
		try {
			WmsLine instance = (WmsLine) getHibernateTemplate().get(
					"com.psit.struts.DAO.impl.WmsLine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

//	public List findByExample(WmsLine instance) {
//		log.debug("finding WmsLine instance by example");
//		try {
//			List results = getHibernateTemplate().findByExample(instance);
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
//		} catch (RuntimeException re) {
//			log.error("find by example failed", re);
//			throw re;
//		}
//	}
//
//	public List findByProperty(String propertyName, Object value) {
//		log.debug("finding WmsLine instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsLine as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByWliTypeCode(Object wliTypeCode) {
//		return findByProperty(WLI_TYPE_CODE, wliTypeCode);
//	}
//
//	public List findByWliType(Object wliType) {
//		return findByProperty(WLI_TYPE, wliType);
//	}
//
//	public List findByWliInNum(Object wliInNum) {
//		return findByProperty(WLI_IN_NUM, wliInNum);
//	}
//
//	public List findByWliOutNum(Object wliOutNum) {
//		return findByProperty(WLI_OUT_NUM, wliOutNum);
//	}
//
//	public List findByWliState(Object wliState) {
//		return findByProperty(WLI_STATE, wliState);
//	}
//
//	public List findByWliMan(Object wliMan) {
//		return findByProperty(WLI_MAN, wliMan);
//	}
//
//	public List findAll() {
//		log.debug("finding all WmsLine instances");
//		try {
//			String queryString = "from WmsLine";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public WmsLine merge(WmsLine detachedInstance) {
//		log.debug("merging WmsLine instance");
//		try {
//			WmsLine result = (WmsLine) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsLine instance) {
//		log.debug("attaching dirty WmsLine instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsLine instance) {
//		log.debug("attaching clean WmsLine instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsLineDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsLineDAOImpl) ctx.getBean("WmsLineDAO");
//	}
	/**
	 * 
	 * ���ݵ�������,��ϸ��Ų�ѯ����ˮ <br>
	 * create_date: Aug 12, 2010,10:43:26 AM <br>
	 * @param wliType ��ˮ����
	 * @param wliWmsId �����ϸid
	 * @return List ���ؿ����ˮ�б�
	 */
	public List findByWmsId( String wliType,Long wliWmsId){
		log.debug("finding all WmsLine instances");
		try {
			String queryString = "from WmsLine where wliIsdel='1' and wliType='"+wliType+"' and wliWmsId="+wliWmsId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ���ݵ��ݱ�Ų�ѯ����ˮ <br>
	 * create_date: Aug 12, 2010,10:59:39 AM <br>
	 * @param wliType ��������
	 * @param wliTypeCode ���ݱ��
	 * @return List ���ؿ����ˮ�б�
	 */
	public List findByTypeCode( String wliType,String wliTypeCode){
		log.debug("finding all WmsLine instances");
		try {
			String queryString = "from WmsLine where wliIsdel='1' and wliType='"+wliType+"' and wliTypeCode='"+wliTypeCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * �����ˮ��ѯ�б�����(����������,�ֿ���,��Ʒ����,��������) <br>
	 * create_date: Aug 12, 2010,10:50:45 AM <br>
	 * @param wliType ��������
	 * @param wmsCode �ֿ���
	 * @param wprName ��Ʒ����
	 * @param startTime ��������(��ʼ)
	 * @param endTime ��������(����)
	 * @return int ���ؿ����ˮ�б�����
	 */
	public int getWliCount(String wliType,String wmsCode,String wprName,String startTime,String endTime){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="";
	    	 if(wmsCode!=null&&!wmsCode.equals("")){
	    		 if(sql.length()>0)
	    			 sql +=" and wmsStro.wmsCode='"+wmsCode+"'";
	    		 else
	    			 sql =" and wmsStro.wmsCode='"+wmsCode+"'";
	    	 } 
	    	if(wliType!=null&&!wliType.equals("")){
	    		if(sql.length()>0)
	    			sql +=" and wliType='"+wliType+"'";
	    		else
	    			sql =" and wliType='"+wliType+"'";
	    	 }
	    	if(wprName!=null&&!wprName.equals("")){
	    		if(sql.length()>0)
	    			sql +=" and wmsProduct.wprName like '%"+wprName+"%'";
	    		else
	    			sql =" and wmsProduct.wprName like '%"+wprName+"%'";
	    	 }
	    	if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wliDate <= '"+endTime+"'";
	    		else
	    			sql =" and wliDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wliDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wliDate between '"+startTime+"' and '"+endTime+"'";
			}
	    	 String queryString="select count(*) from WmsLine where wliIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * �����ˮ��ѯ�б�(����������,�ֿ���,��Ʒ����,��������) <br>
	 * create_date: Aug 12, 2010,10:54:36 AM <br>
	 * @param wliType ��������
	 * @param wmsCode �ֿ���
	 * @param wprName ��Ʒ����
	 * @param startTime ��������(��ʼ)
	 * @param endTime ��������(����)
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���ؿ����ˮ�б�
	 */
	public List WliSearch(String wliType,String wmsCode,String wprName,String startTime,String endTime,int currentPage,int pageSize){
			Query query;
			Session session;
		    session = (Session) super.getSession();
		     String sql="";
		     if(wmsCode!=null&&!wmsCode.equals("")){
	    		 if(sql.length()>0)
	    			 sql +=" and wmsStro.wmsCode='"+wmsCode+"'";
	    		 else
	    			 sql =" and wmsStro.wmsCode='"+wmsCode+"'";
	    	 } 
	    	if(wliType!=null&&!wliType.equals("")){
	    		if(sql.length()>0)
	    			sql +=" and wliType='"+wliType+"'";
	    		else
	    			sql =" and wliType='"+wliType+"'";
	    	 }
	    	if(wprName!=null&&!wprName.equals("")){
	    		if(sql.length()>0)
	    			sql +=" and wmsProduct.wprName like '%"+wprName+"%'";
	    		else
	    			sql =" and wmsProduct.wprName like '%"+wprName+"%'";
	    	 }
	    	if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
	    		if(sql.length()>0)
	    			sql +=" and wliDate <= '"+endTime+"'";
	    		else
	    			sql =" and wliDate <= '"+endTime+"'";
			}
			else if(startTime!=null&&!startTime.equals("")){
				if(sql.length()>0)
	    			sql +=" and wliDate between '"+startTime+"' and '"+endTime+"'";
	    		else
	    			sql =" and wliDate between '"+startTime+"' and '"+endTime+"'";
			}
		    String queryString="from WmsLine where wliIsdel='1'"+sql+"order by wliId desc";
			query=session.createQuery(queryString);
			query.setFirstResult((currentPage-1)*pageSize);
			query.setMaxResults(pageSize);
			List list=(List)query.list();
			return list;
	}
	/**
	 * 
	 * ���¿����ˮ <br>
	 * create_date: Aug 12, 2010,10:56:11 AM <br>
	 * @param instance �����ˮʵ��
	 */
	public void updateWli(WmsLine instance) {
		log.debug("attaching dirty WmsLine instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * �鿴��Ʒ�Ŀ����ˮ�б����� <br>
	 * create_date: Aug 12, 2010,11:02:06 AM <br>
	 * @param wprCode ��Ʒ���
	 * @param isAll ״̬��ʶ
	 * @return int ���ؿ����ˮ�б�����
	 */
	public int getWliCountByWpr(Long wprCode,String isAll){
		Query query;
		Session session;
		session = (Session) super.getSession();
     	String sql="";
		if(isAll==null||isAll.equals(""))
			sql=" and wliState='1'";
		else
			sql="";
    	 String queryString="select count(*) from WmsLine where wliIsdel='1'and wmsProduct.wprId="+wprCode+sql;
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * �鿴��Ʒ�Ŀ����ˮ�б� <br>
	 * create_date: Aug 12, 2010,11:06:50 AM <br>
	 * @param wprCode ��Ʒ���
	 * @param isAll ״̬��ʶ
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���ؿ����ˮ�б�
	 */
	public List findByWpr(Long wprCode,String isAll,int currentPage,int pageSize){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		String sql="";
		if(isAll==null||isAll.equals(""))
			sql=" and wliState='1'";
		else
			sql="";
		String queryString = "from WmsLine where wliIsdel='1' and wmsProduct.wprId="+wprCode+sql+"order by wliDate desc";
		query=session.createQuery(queryString);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List<WmsLine> list=(List)query.list();
		Double number=null;
		if(list.size()>0){
			for(int i=list.size()-1;i>=0;i--){
				WmsLine wli=(WmsLine)list.get(i);
				Double num=null;
				if(wli.getWliInNum()!=null){
					num=wli.getWliInNum();
				}
				if(wli.getWliOutNum()!=null){
					num=-wli.getWliOutNum();
				}
				if(i==list.size()-1){
					
					number=num;
				}
				else{
					number=number+num;
				}
				wli.setWmsAllNum(number);
			}
		}
		return list;
	}
	/**
	 * 
	 * ������Ʒ�Ŀ����ˮ��XML <br>
	 * create_date: Aug 12, 2010,11:08:37 AM <br>
	 * @param wprCode ��Ʒid
	 * @param isAll ״̬��ʶ
	 * @return List ���ؿ����ˮ�б�
	 */
	public List findByWpr(Long wprCode,String isAll){
		log.debug("finding all WmsLine instances");
		try {
			String sql="";
			if(isAll==null||isAll.equals(""))
				sql=" and wliState='1' order by wliDate desc";
			else
				sql=" order by wliDate desc";
			String queryString = "from WmsLine where wliIsdel='1' and wmsProduct.wprId="+wprCode+sql;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ��ѯĳ���ֿ����ˮ <br>
	 * create_date: Aug 12, 2010,11:24:28 AM <br>
	 * @param wmsCode �ֿ���
	 * @return List ������ˮ�б�
	 */
	public List wliSearch(String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql ="select wli from WmsLine as wli where wli.wliIsdel='1' and wli.wmsStro.wmsCode='"+wmsCode+"' order by wli.wliId desc";
	     query=session.createQuery(sql);
		 List list=(List)query.list();
		 return list;
	}
	/**
	 * 
	 * ���ݵ�������ɾ��ָ���Ŀ����ˮ <br>
	 * create_date: Aug 12, 2010,11:26:12 AM <br>
	 * @param wliType ��������
	 * @param wliTypeCode ����id
	 */
	public void updateByCode(String wliType,String wliTypeCode){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		log.debug("finding all TaLim instances");
		try {
			//String queryString = "update WmsLine set wliIsdel='0' where wliType='"+wliType+"' and wliTypeCode='"+wliTypeCode+"'";
			String queryString = "delete from WmsLine  where wliType='"+wliType+"' and wliTypeCode='"+wliTypeCode+"'";
			session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ��ô�ɾ�������п����ˮ <br>
	 * create_date: Aug 12, 2010,11:52:33 AM <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���ؿ����ˮ�б�
	 */
	public List findDelWmsLine(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsLine where  wliIsdel='0' order by wliId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ��ô�ɾ�������п����ˮ���� <br>
	 * create_date: Aug 12, 2010,11:53:09 AM <br>
	 * @return int ���ؿ����ˮ�б�����
	 */
	public int findDelWmsLineCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsLine where  wliIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * ������Ʒid��ѯ�����ˮ <br>
	 * create_date: Aug 12, 2010,12:04:08 PM <br>
	 * @param wprId ��Ʒid
	 * @return List ������ˮ�б�
	 */
	public List findByWpr(Long wprId) {
		log.debug("finding all WmsLine instances");
		try {
			String queryString = "from WmsLine where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}