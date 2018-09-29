package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsProTypeDAO;
import com.psit.struts.entity.WmsProType;

/**
 * ��Ʒ���DAOʵ���� <br>
 */
public class WmsProTypeDAOImpl extends HibernateDaoSupport implements WmsProTypeDAO{
	private static final Log log = LogFactory.getLog(WmsProTypeDAOImpl.class);
	// property constants
	public static final String WPT_NAME = "wptName";
	public static final String WPT_DESC = "wptDesc";
	public static final String WPT_ISENABLED = "wptIsenabled";

	protected void initDao() {
		// do nothing
	}
	/**
	 * �����Ʒ��� <br>
	 * @param transientInstance ��Ʒ���ʵ��
	 */
	public void save(WmsProType transientInstance) {
		log.debug("saving WmsProType instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * ɾ����Ʒ��� <br>
	 * @param persistentInstance ��Ʒ���ʵ��
	 */
	public void delete(WmsProType persistentInstance) {
		log.debug("deleting WmsProType instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * ������Ʒ���Id��ѯ���ʵ�� <br>
	 * @param id ��Ʒ���Id
	 * @return WmsProType ������Ʒ���ʵ��
	 */
	public WmsProType findById(java.lang.Long id) {
		log.debug("getting WmsProType instance with id: " + id);
		try {
			WmsProType instance = (WmsProType) getHibernateTemplate().get(
					"com.psit.struts.entity.WmsProType", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * ��ѯ������ʹ�õ���Ʒ��� <br>
	 * @return List ����б�
	 */
	public List<WmsProType> findAll() {
		log.debug("finding all WmsProType instances");
		try {
			String queryString = "from WmsProType";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ��ѯ������ʹ�õ���Ʒ��� <br>
	 * @return List ����б�
	 */
	public List findAllWptType() {
		log.debug("finding all WmsProType instances");
		try {
			String queryString = "from WmsProType where wptIsenabled=1";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ������Ʒ��� <br>
	 * @param instance ��Ʒ���ʵ��
	 */
	public void updateProType(WmsProType instance){
		log.debug("attaching dirty WmsProType instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * ��ѯ��Ʒ�����¼���� <br>
	 * @param wptId ��Ʒ���id
	 * @return List ������Ʒ����б�
	 */
	public List findByUpId(Long wptId) {
		log.debug("finding WmsProType instance with property: ");
		try {
			String queryString = "from WmsProType as model where model.wmsProType.wptId="+wptId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * ���ݿͻ�Id��ѯ��Ʒ���<br>
	 * @param cusId �ͻ�Id
	 * @return List<WmsProType> ��Ʒ���<br>
	 */
	public List<WmsProType> findByCusId(String cusId){
		Session session = (Session) super.getSession();
		String sql = "from WmsProduct as wpd where wpd.wprId in ( select cp.wmsProduct.wprId from CusProd as cp where cp.cusCorCus.corCode ="+cusId+" )";
		Query query = session.createQuery(sql);
		List<WmsProType> list = query.list();
		return list;
	}
}