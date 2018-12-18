package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RStroProDAO;
import com.psit.struts.entity.RStroPro;

/**
 * 
 * �ֿ���ϸDAOʵ���� <br>
 *
 * create_date: Aug 17, 2010,3:20:41 PM<br>
 * @author zjr
 */
@SuppressWarnings("unchecked")
public class RStroProDAOImpl extends HibernateDaoSupport implements RStroProDAO {
	private static final Log log = LogFactory.getLog(RStroProDAOImpl.class);
	// property constants
	public static final String RSPPRONUM = "rspProNum";
//	protected void initDao() {
		// do nothing
//	}
	/**
	 * 
	 * ����ֿ���ϸ <br>
	 * create_date: Aug 11, 2010,2:45:31 PM <br>
	 * @param transientInstance �ֿ���ϸʵ��
	 */
	public void save(RStroPro transientInstance) {
		log.debug("saving RStroPro instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
//	public void delete(RStroPro persistentInstance) {
//		log.debug("deleting RStroPro instance");
//		try {
//			getHibernateTemplate().delete(persistentInstance);
//			log.debug("delete successful");
//		} catch (RuntimeException re) {
//			log.error("delete failed", re);
//			throw re;
//		}
//	}
//
//	public RStroPro findById(java.lang.Long id) {
//		log.debug("getting RStroPro instance with id: " + id);
//		try {
//			RStroPro instance = (RStroPro) getHibernateTemplate().get(
//					"com.psit.struts.entity.RStroPro", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(RStroPro instance) {
//		log.debug("finding RStroPro instance by example");
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
//		log.debug("finding RStroPro instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RStroPro as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByrspProNum(Object rspProNum) {
//		return findByProperty(RSPPRONUM, rspProNum);
//	}
	/**
	 * 
	 * ��ѯ�ֿ�������Ʒ <br>
	 * create_date: Aug 11, 2010,2:55:12 PM <br>
	 * @return List ���زֿ���ϸ�б�
	 */
	public List findAll() {
		log.debug("finding all RStroPro instances");
		try {
			String queryString = "from RStroPro";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ����Ʒ���ƺͲֿ��Ų�ѯ�ֿ���ϸ�б����� <br>
	 * create_date: Aug 11, 2010,5:44:14 PM <br>
	 * @param wmsCode �ֿ���
	 * @param wprName ��Ʒ����
	 * @return int ���زֿ���ϸ�б�����
	 */
	public int getCount(String wmsCode,String wprName) {
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql="";
	     if(wmsCode!=null&&!wmsCode.equals("")){
	    	 if(sql.length()>0)
	    		 sql+="and wmsStro.wmsCode='"+wmsCode+"'";
	    	 else
	    		 sql="where wmsStro.wmsCode='"+wmsCode+"'";
	     }
	     if(wprName!=null&&!wprName.equals("")){
	    	 if(sql.length()>0)
	    		 sql+="and wmsProduct.wprName like '%"+wprName+"%'";
	    	 else
	    		 sql="where wmsProduct.wprName like '%"+wprName+"%'";
	     }
	     	String queryString ="select count(*) from RStroPro "+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * ����Ʒ���ƺͲֿ��Ų�ѯ�ֿ���ϸ�б� <br>
	 * create_date: Aug 11, 2010,5:49:43 PM <br>
	 * @param wmsCode �ֿ���
	 * @param wprName ��Ʒ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���زֿ���ϸ�б�
	 */
	public List findProByWms(String wmsCode,String wprName,int currentPage,int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		 String sql="";
	     if(wmsCode!=null&&!wmsCode.equals("")){
	    	 if(sql.length()>0)
	    		 sql+="and wmsStro.wmsCode='"+wmsCode+"'";
	    	 else
	    		 sql="where wmsStro.wmsCode='"+wmsCode+"'";
	     }
	     if(wprName!=null&&!wprName.equals("")){
	    	 if(sql.length()>0)
	    		 sql+="and wmsProduct.wprName like '%"+wprName+"%'";
	    	 else
	    		 sql="where wmsProduct.wprName like '%"+wprName+"%'";
	     }
		String queryString ="from RStroPro "+sql+"order by rspId desc";
		query=session.createQuery(queryString);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ���ݲֿ��Ų�ѯ�ֿ���Ʒ <br>
	 * create_date: Aug 11, 2010,2:56:36 PM <br>
	 * @param wmsCode �ֿ���
	 * @return List ���زֿ���ϸ�б�
	 */
	public List findByWmsCode(String wmsCode) {
		log.debug("finding all RStroPro instances");
		try {
			String queryString = "from RStroPro as rsp where rsp.wmsStro.wmsCode='"+wmsCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
//	public RStroPro merge(RStroPro detachedInstance) {
//		log.debug("merging RStroPro instance");
//		try {
//			RStroPro result = (RStroPro) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RStroPro instance) {
//		log.debug("attaching dirty RStroPro instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RStroPro instance) {
//		log.debug("attaching clean RStroPro instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static RStroProDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RStroProDAOImpl) ctx.getBean("RStroProDAO");
//	}
	/**
	 * 
	 * ���²ֿ���ϸ�� <br>
	 * create_date: Aug 11, 2010,2:56:10 PM <br>
	 * @param rsp �ֿ���ϸʵ��
	 */
	public void updatePsp(RStroPro rsp){
		super.getHibernateTemplate().update(rsp);
	}
	/**
	 * 
	 * ����Ʒid,�ֿ��Ų�ѯ�����ϸ�б����� <br>
	 * create_date: Aug 11, 2010,3:01:40 PM <br>
	 * @param wprCode ��Ʒid
	 * @param wmsCode �ֿ���
	 * @return int ���ؿ����ϸ�б�����
	 */
	public int getCountRwp(String wprCode,String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     Long wprId=null;
	     String sql="";
	     if(wprCode!=null&&!wprCode.equals("")){
	    	  wprId=Long.parseLong(wprCode);
	    	  if(sql.length()>0)
	    		  sql+=" and wmsProduct.wprId ="+wprId;
	    	  else
	    		  sql="where wmsProduct.wprId ="+wprId;
	     }
	     if(wmsCode!=null&&!wmsCode.equals("")){
	    	 if(sql.length()>0)
	    		  sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
	    	  else
	    		  sql="where wmsStro.wmsCode='"+wmsCode+"'";
	     }
	     	String queryString ="select count(*) from RStroPro "+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * ����Ʒid,�ֿ��Ų�ѯ�����ϸ�б� <br>
	 * create_date: Aug 11, 2010,3:03:58 PM <br>
	 * @param wprCode ��Ʒid
	 * @param wmsCode �ֿ���
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���ؿ����ϸ�б�
	 */
	public List rwpSearch(String wprCode,String wmsCode,int currentPage,int pageSize){
		Query query;
		Session session;
		session = (Session) super.getSession();
	    Long wprId=null;
	    String sql="";
	     if(wprCode!=null&&!wprCode.equals("")){
	    	  wprId=Long.parseLong(wprCode);
	    	  if(sql.length()>0)
	    		  sql+=" and wmsProduct.wprId ="+wprId;
	    	  else
	    		  sql="where wmsProduct.wprId ="+wprId;
	     }
	     if(wmsCode!=null&&!wmsCode.equals("")){
	    	 if(sql.length()>0)
	    		  sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
	    	  else
	    		  sql="where wmsStro.wmsCode='"+wmsCode+"'";
	     }
			String queryString ="from RStroPro "+sql+"order by rspId desc";
			query=session.createQuery(queryString);
			query.setFirstResult((currentPage-1)*pageSize);
			query.setMaxResults(pageSize);
			List list=(List)query.list();
			return list;
	}
	/**
	 * 
	 * ��ѯ���вֿ���ϸ�б����� <br>
	 * create_date: Aug 11, 2010,3:05:19 PM <br>
	 * @return int ���ؿ����ϸ�б�����
	 */
	public int getCountRStroPro(){
		Query query;
		Session session;
		session = (Session) super.getSession();
     	String sql ="select count(*) from RStroPro as rStroPro";
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * ��ѯ���вֿ���ϸ�б� <br>
	 * create_date: Aug 11, 2010,3:05:38 PM <br>
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���ؿ����ϸ�б�
	 */
	public List rStroProSearch(int currentPage,int pageSize){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="select rStroPro from RStroPro as rStroPro order by rspId desc";
		query=session.createQuery(sql);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ������Ϊ0����Ʒ���� <br>
	 * create_date: Aug 12, 2010,10:20:05 AM <br>
	 * @param wmsCode �ֿ���
	 */
	public void delData(String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	     String sql="";
	     if(wmsCode!=null&&!wmsCode.equals("")){
	    	 sql=" and wmsStro.wmsCode='"+wmsCode+"'";
	     }
		String queryString ="delete RStroPro where rspProNum =0"+sql;
		int i=session.createQuery(queryString).executeUpdate();
	}
	/**
	 * 
	 * ��ѯĳ�ֿ���Ʒ�Ŀ���� <br>
	 * create_date: Aug 12, 2010,9:49:04 AM <br>
	 * @param wmsCode �ֿ���
	 * @param wprId ��Ʒid
	 * @return List<RStroPro> ���ؿ����ϸ�б�
	 */
	public List<RStroPro> findProNum(String wmsCode,Long wprId) {
		log.debug("finding all RStroPro instances");
		try {
			String queryString ="from RStroPro as rsp where rsp.wmsStro.wmsCode='"+wmsCode+"' and rsp.wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ������Ʒid��ѯ��Ʒ�Ŀ���� <br>
	 * create_date: Aug 11, 2010,3:12:57 PM <br>
	 * @param wprId ��Ʒid
	 * @return List<RStroPro> ���ؿ����ϸ�б�
	 */
	public List<RStroPro> rwpSearch(Long wprId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="select rStroPro from RStroPro as rStroPro where rStroPro.wmsProduct.wprId ='"+wprId+"'";
		query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ��ѯ��Ʒ�Ŀ������ <br>
	 * create_date: Aug 11, 2010,3:14:12 PM <br>
	 * @param wprId ��Ʒid
	 * @return Double ���ؿ������
	 */
	public Double getCountPro(Long wprId){
		Query query;
		Session session;
	    session = (Session) super.getSession();
     	String sql ="select sum(rsp.rspProNum) from RStroPro as rsp where rsp.wmsProduct.wprId ='"+wprId+"'";
     	query = session.createQuery(sql);
     	List li = query.list();
     	if (li == null || li.isEmpty()) 
     	{
     		return 0.0;
     	} 
     	else 
     	{
     		if (li.get(0) == null) 
     		{
     			return 0.0;
     		}
     	return (Double)li.get(0);
     	}
	}
	/**
	 * 
	 * ��ѯ�ֿ�Ŀ������ <br>
	 * create_date: Aug 11, 2010,3:20:00 PM <br>
	 * @param wmsCode �ֿ���
	 * @return int ���ؿ������
	 */
	public int getCountStro(String wmsCode){
			Query query;
			Session session;
		    session = (Session) super.getSession();
	     	String sql ="select sum(rsp.rspProNum) from RStroPro as rsp where rsp.wmsStro.wmsCode ='"+wmsCode+"'";
	     	query = session.createQuery(sql);
	     	List li = query.list();
	     	if (li == null || li.isEmpty()) 
	     	{
	     		return 0;
	     	} 
	     	else 
	     	{
	     		if (li.get(0) == null) 
	     		{
	     			return 0;
	     		}
	     	return (int)((Double)li.get(0)).intValue();
	     	}
	}
}