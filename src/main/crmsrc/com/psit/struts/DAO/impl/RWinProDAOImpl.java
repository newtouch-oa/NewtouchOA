package com.psit.struts.DAO.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RWinProDAO;
import com.psit.struts.entity.RWinPro;
/**
 * 
 * �����ϸDAOʵ���� <br>
 *
 * create_date: Aug 17, 2010,4:20:35 PM<br>
 * @author zjr
 */
@SuppressWarnings("unchecked")
public class RWinProDAOImpl extends HibernateDaoSupport implements RWinProDAO  {
	private static final Log log = LogFactory.getLog(RWinProDAOImpl.class);
	/**
	 * 
	 * ���������ϸ <br>
	 * create_date: Aug 11, 2010,2:43:37 PM <br>
	 * @param rwinPro �����ϸʵ��
	 */
	public void save(RWinPro rwinPro){
		super.getHibernateTemplate().save(rwinPro);
	}
	//��ѯ���������Ʒ
//	public List<RWinPro> findAll(){
//		log.debug("finding all RWinPro instances");
//		try {
//			String queryString = "from RWinPro";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
	/**
	 * 
	 * ��ѯ���������Ʒ <br>
	 * create_date: Aug 11, 2010,2:44:10 PM <br>
	 * @param wwiId �����ϸid
	 * @return List<RWinPro> ���������ϸ�б�
	 */
	public List<RWinPro> getRwinPro(Long wwiId){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		String sql ="select rWinPro from RWinPro as rWinPro where rWinPro.wmsWarIn.wwiId = "+wwiId+"order by rwiId desc";
		query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ������ⵥ��ɾ�������ϸ <br>
	 * create_date: Aug 11, 2010,3:00:06 PM <br>
	 * @param wwiId ��ⵥid
	 */
	public void delRwinPro(Long wwiId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		log.debug("finding all RWinPro instances");
		try {
			String queryString ="delete from RWinPro as rWinPro where rWinPro.wmsWarIn.wwiId = "+wwiId;
			 int i=session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ����id��ѯ�����ϸʵ�� <br>
	 * create_date: Aug 11, 2010,2:50:18 PM <br>
	 * @param rwiId �����ϸid
	 * @return RWinPro ���������ϸʵ��
	 */
	public RWinPro getRWinPro(Long rwiId){
		return (RWinPro)super.getHibernateTemplate().get(RWinPro.class, rwiId);
	}
	/**
	 * 
	 * ���������Ʒ <br>
	 * create_date: Aug 11, 2010,2:51:24 PM <br>
	 * @param rwinPro �����ϸʵ��
	 */
	public void updateRwp(RWinPro rwinPro){
		super.getHibernateTemplate().update(rwinPro);
	}
	/**
	 * 
	 * ɾ�������Ʒ <br>
	 * create_date: Aug 11, 2010,2:53:45 PM <br>
	 * @param rwinPro �����ϸʵ��
	 */
	public void delRwp(RWinPro rwinPro){
		super.getHibernateTemplate().delete(rwinPro);
	}
	/**
	 * 
	 * ��ѯ�����ϸ�е���Ʒ <br>
	 * create_date: Aug 11, 2010,3:23:32 PM <br>
	 * @param wprId ��Ʒid
	 * @return List<RWinPro> ���������ϸ�б�
	 */
	public List<RWinPro> findByWpr(Long wprId) {
		log.debug("finding all RWinPro instances");
		try {
			String queryString = "from RWinPro where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ����ⵥ�ź���Ʒid��ѯ�����ϸ <br>
	 * create_date: Aug 11, 2010,3:25:55 PM <br>
	 * @param wprId ��Ʒid
	 * @param wwiCode ��ⵥ��
	 * @return List<RWinPro> ���������ϸ�б�
	 */
	public List<RWinPro> findByWpr(Long wprId,String wwiCode) {
		log.debug("finding all RWinPro instances");
		try {
			String queryString = "from RWinPro where wmsProduct.wprId = "+wprId+" and wmsWarIn.wwiCode = '"+wwiCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
