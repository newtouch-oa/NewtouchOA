package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsStroDAO;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.entity.SalTask;
import com.psit.struts.entity.WmsStro;

/**
 * 
 * �ֿ�DAOʵ���� <br>
 *
 * create_date: Aug 19, 2010,1:44:01 PM<br>
 * @author zjr
 */
public class WmsStroDAOImpl extends HibernateDaoSupport implements WmsStroDAO {
	private static final Log log = LogFactory.getLog(WmsStroDAOImpl.class);
	// property constants
	public static final String WMS_NAME = "wmsName";
	public static final String WMS_LOC = "wmsLoc";
	public static final String WMS_REMARK = "wmsRemark";
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * ����ֿ� <br>
	 * create_date: Aug 11, 2010,2:16:14 PM <br>
	 * @param transientInstance �ֿ�ʵ��
	 */
	public void save(WmsStro transientInstance) {
		log.debug("saving WmsStro instance");
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
	 * ɾ���ֿ� <br>
	 * create_date: Aug 11, 2010,2:22:21 PM <br>
	 * @param persistentInstance �ֿ�ʵ��
	 */
	public void delete(WmsStro persistentInstance) {
		log.debug("deleting WmsStro instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

//	public WmsStro findById(java.lang.String id) {
//		log.debug("getting WmsStro instance with id: " + id);
//		try {
//			WmsStro instance = (WmsStro) getHibernateTemplate().get(
//					"com.psit.struts.entity.WmsStro", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(WmsStro instance) {
//		log.debug("finding WmsStro instance by example");
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
//	public List findByProperty(String propertyName, Object value) {
//		log.debug("finding WmsStro instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsStro as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//	public List findByWmsName(Object wmsName) {
//		return findByProperty(WMS_NAME, wmsName);
//	}
//
//	public List findByWmsLoc(Object wmsLoc) {
//		return findByProperty(WMS_LOC, wmsLoc);
//	}
//
//	public List findByWmsRemark(Object wmsRemark) {
//		return findByProperty(WMS_REMARK, wmsRemark);
//	}
	/**
	 * 
	 * ��ѯ�ֿ��б� <br>
	 * create_date: Aug 11, 2010,2:32:30 PM <br>
	 * @return List ���زֿ��б�
	 */
	public List findAll() {
		log.debug("finding all WmsStro instances");
		try {
			String queryString = "from WmsStro where wmsIsenabled='1'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

//	public WmsStro merge(WmsStro detachedInstance) {
//		log.debug("merging WmsStro instance");
//		try {
//			WmsStro result = (WmsStro) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsStro instance) {
//		log.debug("attaching dirty WmsStro instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsStro instance) {
//		log.debug("attaching clean WmsStro instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsStroDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsStroDAOImpl) ctx.getBean("WmsStroDAO");
//	}
	/**
	 * 
	 * ���ֿ����Ʋ�ѯ�ֿ��б����� <br>
	 * create_date: Aug 11, 2010,2:17:18 PM <br>
	 * @param wmsName �ֿ�����
	 * @return int ���زֿ��б�����
	 */
	public int getWmsCount(String wmsName){
		Query query;
		Session session;
	    session = (Session) super.getSession();
     	String sql ="select count(*) from WmsStro as wmsStro where wmsIsenabled='1'";
     	if(wmsName != null && !wmsName.equals("")){
			sql += " and wmsName like '%"+wmsName+"%'";
		}
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * ���ֿ����Ʋ�ѯ�ֿ��б� <br>
	 * create_date: Aug 11, 2010,2:18:09 PM <br>
	 * @param wmsName �ֿ�����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ���زֿ��б�
	 */
	public List<WmsStro> WmsSearch(String wmsName,int currentPage,int pageSize){
		Query query;
		Session session;
        session = (Session) super.getSession();
		String sql ="from WmsStro where wmsIsenabled='1'";
		if(wmsName != null && !wmsName.equals("")){
			sql += " and wmsName like '%"+wmsName+"%'";
		}
		sql += " order by wmsCode";
		query=session.createQuery(sql);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List<WmsStro> list = query.list();
		return list;
	}
	/**
	 * 
	 * �鿴�ֿ����� <br>
	 * create_date: Aug 11, 2010,2:20:52 PM <br>
	 * @param wmsCode �ֿ���
	 * @return WmsStro �ֿ�ʵ��
	 */
	public WmsStro findWmsStro(String wmsCode) {
		return (WmsStro)super.getHibernateTemplate().get(WmsStro.class, wmsCode);
	}
	/**
	 * 
	 * ���²ֿ� <br>
	 * create_date: Aug 11, 2010,2:21:58 PM <br>
	 * @param wmsStro �ֿ�ʵ��
	 */
	public void updateWmsStro(WmsStro wmsStro){
		super.getHibernateTemplate().update(wmsStro);
	}
	/**
	 * 
	 * �жϲֿ������Ƿ��ظ� <br>
	 * create_date: Aug 11, 2010,3:38:34 PM <br>
	 * @param wmsName �ֿ�����
	 * @return List ���زֿ��б�
	 */
	public List checkWmsName(String wmsName) {
		Session session = (Session) super.getSession();
		String sql="select wmsName from WmsStro where wmsName='"+wmsName+"'";
		Query query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
}