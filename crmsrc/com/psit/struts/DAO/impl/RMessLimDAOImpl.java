package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RMessLimDAO;
import com.psit.struts.entity.RMessLim;
/**
 * ��Ϣ������DAOʵ���� <br>
 */
public class RMessLimDAOImpl extends HibernateDaoSupport implements RMessLimDAO
{
	/**
	 * ��������� <br>
	 * @param rmessLim ������ʵ��
	 */
	public void save(RMessLim rmessLim) 
	{
		super.getHibernateTemplate().save(rmessLim);
	}
	/**
	 * �õ�������ʵ�� <br>
	 * @param id ������id
	 * @return RMessLim ���ؽ�����ʵ��  
	 */
	public RMessLim getRMessLim(Long id) {
		return (RMessLim) super.getHibernateTemplate().get(RMessLim.class, id);
	}
	/**
	 * ���½�������Ϣ <br>
	 * @param rmessLim ������ʵ��
	 */
	public void update(RMessLim rmessLim) {
		super.getHibernateTemplate().update(rmessLim);
		
	}
	/**
	 * ��ô�ɾ��������������Ϣ <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ������Ϣ�б� 
	 */
	public List findDelMail(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from RMessLim where  rmlIsdel='0' order by rmlId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * ��ô�ɾ��������������Ϣ���� <br>
	 * @return int ������Ϣ�б����� 
	 */
	public int findDelMailCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from RMessLim where  rmlIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * ɾ��������Ϣ <br>
	 * @param rmessLim ������ʵ��
	 */
	public void delRMessLim(RMessLim rmessLim) {
		super.getHibernateTemplate().delete(rmessLim);
	}
	
}