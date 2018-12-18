package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.TypeListDAO;
import com.psit.struts.entity.TypeList;
/**
 * ������DAOʵ���� <br>
 */
@SuppressWarnings("unchecked")
public class TypeListDAOImpl extends HibernateDaoSupport implements TypeListDAO{
	private static final Log log = LogFactory.getLog(TypeListDAOImpl.class);
	// property constants
	public static final String TYP_NAME = "typName";
	public static final String TYP_DESC = "typDesc";
	public static final String TYP_TYPE = "typType";
	public static final String TYP_ID = "typId";
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * �������<br>
	 * @param transientInstance ���ʵ��
	 */
	public void save(TypeList transientInstance) {
		log.debug("saving TypeList instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public TypeList findById(java.lang.Long id) {
		log.debug("getting TypeList instance with id: " + id);
		try {
			TypeList instance = (TypeList) getHibernateTemplate().get(
					"com.psit.struts.entity.TypeList", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * ��ѯĳ�������µ�������� <br>
	 * @param type ����
	 * @return List<TypeList> ��������б�
	 */
	public List<TypeList> findAll(String type) {
		Session session = (Session) super.getSession();
		String queryString = "from TypeList where typType=:type";
		Query query = session.createQuery(queryString).setString("type", type);
		return query.list();
	}
	/**
	 * ����������ƺ����͵õ���� <br>
	 * @param typeName �������
	 * @param typType ����
	 * @return TypeList �������ʵ��
	 */
	public TypeList getTypeByName(String typeName,String typType){
		Session session = (Session) super.getSession();
		String queryString = "from TypeList where typName=:tName and typType=:tType";
		Query query = session.createQuery(queryString)
					.setString("tName",typeName)
					.setString("tType", typType);
		List list = query.list();
		if (list.size() > 0)
			return (TypeList) list.get(0);
		else
			return null;
	}
	/**
	 * ���ĳ�������µ����������õ���� <br>
	 * @param type  ����
	 * @return List<TypeList> ��������б�
	 */
	public List<TypeList> getEnabledType(String type){
		Session session = (Session) super.getSession();
		String queryString = "from TypeList where typIsenabled='1' and typType=:type order by typId";
		Query query = session.createQuery(queryString).setString("type", type);
		return query.list();
	}
	
	/**
	 * �����������б� <br>
	 * @return List ���ʵ���б�<br>
	 */
	public List<TypeList> findAll() {
		log.debug("finding all TypeList instances");
		try {
			String queryString = "from TypeList";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}