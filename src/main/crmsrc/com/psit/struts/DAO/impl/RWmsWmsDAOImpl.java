package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RWmsWmsDAO;
import com.psit.struts.entity.RWmsWms;
/**
 * 
 * ������ϸDAOʵ���� <br>
 *
 * create_date: Aug 17, 2010,4:52:25 PM<br>
 * @author zjr
 */
public class RWmsWmsDAOImpl extends HibernateDaoSupport implements RWmsWmsDAO{
	private static final Log log = LogFactory.getLog(RWmsWmsDAOImpl.class);
	// property constants
	public static final String RWW_NUM = "rwwNum";
	public static final String RWW_REMARK = "rwwRemark";
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * ���������ϸ <br>
	 * create_date: Aug 11, 2010,5:36:06 PM <br>
	 * @param transientInstance ������ϸʵ��
	 */
	public void save(RWmsWms transientInstance) {
		log.debug("saving RWmsWms instance");
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
	 * ɾ���ֿ������ϸ <br>
	 * create_date: Aug 11, 2010,5:53:46 PM <br>
	 * @param persistentInstance ������ϸʵ��
	 */
	public void delete(RWmsWms persistentInstance) {
		log.debug("deleting RWmsWms instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
//	public RWmsWms findById(java.lang.Long id) {
//		log.debug("getting RWmsWms instance with id: " + id);
//		try {
//			RWmsWms instance = (RWmsWms) getHibernateTemplate().get(
//					"com.psit.struts.DAO.impl.RWmsWms", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}
//
//	public List findByExample(RWmsWms instance) {
//		log.debug("finding RWmsWms instance by example");
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
//		log.debug("finding RWmsWms instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from RWmsWms as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
//
//	public List findByRwwNum(Object rwwNum) {
//		return findByProperty(RWW_NUM, rwwNum);
//	}
//
//	public List findByRwwRemark(Object rwwRemark) {
//		return findByProperty(RWW_REMARK, rwwRemark);
//	}
//
//
//	public List findAll() {
//		log.debug("finding all RWmsWms instances");
//		try {
//			String queryString = "from RWmsWms";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
//
//	public RWmsWms merge(RWmsWms detachedInstance) {
//		log.debug("merging RWmsWms instance");
//		try {
//			RWmsWms result = (RWmsWms) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(RWmsWms instance) {
//		log.debug("attaching dirty RWmsWms instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(RWmsWms instance) {
//		log.debug("attaching clean RWmsWms instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//	public static RWmsWmsDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (RWmsWmsDAOImpl) ctx.getBean("RWmsWmsDAO");
//	}
	/**
	 * 
	 * ���ݲֿ������id��ѯ������ϸ <br>
	 * create_date: Aug 11, 2010,5:38:21 PM <br>
	 * @param wchId �ֿ������id
	 * @return List ���ص�����ϸ�б�
	 */
	public List getRww(Long wchId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="from RWmsWms where wmsChange.wchId = "+wchId+"order by rwwId desc";
		query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ���²ֿ������Ʒ <br>
	 * create_date: Aug 11, 2010,5:39:12 PM <br>
	 * @param rwmsWms ������ϸʵ��
	 */
	public void updateRww(RWmsWms rwmsWms){
		super.getHibernateTemplate().update(rwmsWms);
	}
	/**
	 * 
	 * ����Id��ѯ������ϸ <br>
	 * create_date: Aug 11, 2010,5:39:46 PM <br>
	 * @param rwwId ������ϸid
	 * @return RWmsWms ���ص�����ϸʵ��
	 */
	public RWmsWms searchRww(Long rwwId){
		return (RWmsWms) super.getHibernateTemplate().get(RWmsWms.class, rwwId);
	}
	/**
	 * 
	 * ������Ʒid��ѯ������ϸ <br>
	 * create_date: Aug 12, 2010,12:02:30 PM <br>
	 * @param wprId ��Ʒid
	 * @return List ���ص�����ϸ�б�
	 */
	public List findByWpr(Long wprId) {
		log.debug("finding all RWmsWms instances");
		try {
			String queryString = "from RWmsWms where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ���ݵ�����id����Ʒid��ѯ������ϸ <br>
	 * create_date: Aug 12, 2010,12:05:59 PM <br>
	 * @param wprId ��Ʒid
	 * @param wchId ������id
	 * @return List ���س�������ϸ�б�
	 */
	public List findByWch(Long wprId,Long wchId) {
		log.debug("finding all RWmsWms instances");
		try {
			String queryString = "from RWmsWms where wmsProduct.wprId = "+wprId+" and wmsChange.wchId ="+wchId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}