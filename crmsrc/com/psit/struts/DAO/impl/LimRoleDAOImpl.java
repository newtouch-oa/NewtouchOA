package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.LimRoleDAO;
import com.psit.struts.entity.LimRole;
/**
 * ְ��DAOʵ���� <br>
 */
public class LimRoleDAOImpl extends HibernateDaoSupport implements LimRoleDAO{
	private static final Log log = LogFactory.getLog(LimRoleDAO.class);
	// property constants
	public static final String ROL_LEV = "rolLev";
	public static final String ROL_NAME = "rolName";
	protected void initDao() {
		// do nothing
	}
	/**
	 * ���ְ��<br>
	 * @param transientInstance ְ��ʵ��
	 */
	public void save(LimRole transientInstance) {
		log.debug("saving LimRole instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * ɾ��ְ�� <br>
	 * @param persistentInstance ְ��ʵ��
	 */
	public void delete(LimRole persistentInstance) {
		log.debug("deleting LimRole instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/**
	 * �������ְ�� <br>
	 * @return List ����ְ���б� 
	 */
	public List findAll() {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole order by rolLev";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ���ְ������upRolLevС�ڵ���curRolLev��ְ���б� <br>
	 * @return List ����ְ���б� 
	 */
	public List getRoleList(int upRolLev,int curRolLev){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql = "select limRole from LimRole as limRole where rolLev>"
				+ upRolLev + " and rolLev<=" + curRolLev + " order by rolLev";
		query = session.createQuery(sql);
		List list = (List) query.list();
		return list;
	}
	/**
	 * 
	 * ��ѯ�¼��û���ɫ <br>
	 * create_date: Aug 11, 2010,11:03:55 AM <br>
	 * @param rolLev ְ��
	 * @return List ����ְ���б�
	 */
	public List findRole(int rolLev) {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev > "+rolLev;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ��ѯ�����ڶ�����ְ�� <br>
	 * create_date: Aug 11, 2010,11:16:11 AM <br>
	 * @return List ����ְ���б�
	 */
	public List isNotBoss() {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev !=1";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ��ѯͬ�����ְ�� <br>
	 * @param rolLev ְ��
	 * @return List ����ְ���б�
	 */
	public List findSameRole(int rolLev) {
		log.debug("finding all LimRole instances");
		try {
			String queryString = "from LimRole where rolLev = "+rolLev;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ����ְ��������ѯ <br>
	 * @return int ����ְ������
	 */
	public int getCountRole(){
		Query query;
		Session session;
		session = (Session) super.getSession();
		 String sql1 ="select count(*) from LimRole";
			query = session.createQuery(sql1);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * ����ְ���б��ѯ  <br>
	 * @return List ����ְ���б�
	 */
	public List roleSerach(){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql1 ="from LimRole order by rolLev";
		query=session.createQuery(sql1);
//		query.setFirstResult((currentPage-1)*pageSize);
//		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * ����Id�õ�ְ��ʵ�� <br>
	 * @param roleId ְ��id
	 * @return LimRole ����ְ��ʵ��
	 */
	public LimRole getRole(Long roleId){
		return (LimRole) super.getHibernateTemplate().get(LimRole.class, roleId);
	}
	/**
	 * ����ְ�� <br>
	 * @param limRole ְ��ʵ��
	 */
	public void updateRole(LimRole limRole){
		 super.getHibernateTemplate().update(limRole);
	}
	/**
	 * ��ѯ����ְ�� <br>
	 * @return String ���ض���ְ�� 
	 */
	public String maxRole(){
		Query query;
		Session session;
		session = (Session) super.getSession();
			String sql1 ="select max(rolLev) from LimRole";
			query=session.createQuery(sql1);
			String maxrole="";
			if(query.uniqueResult()!=null)
				maxrole=query.uniqueResult().toString();
			return maxrole;
	}
	/**
	 * ����ְ�����Ʋ�ѯ����ְ�� <br>
	 * @param rolName ְ������
	 * @return List ����ְ���б�
	 */
	public List getRoleByName(String rolName) {
		Session session = (Session) super.getSession();
		String sql ="select rolName from LimRole where rolName=:rName";
		Query query=session.createQuery(sql).setString("rName", rolName);
		List list =(List)query.list();
		return list;
	}
	
}