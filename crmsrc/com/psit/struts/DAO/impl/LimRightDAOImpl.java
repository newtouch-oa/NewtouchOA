package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.psit.struts.DAO.LimRightDAO;
import com.psit.struts.entity.LimRight;
/**
 * Ȩ��DAOʵ���� <br>
 */
public class LimRightDAOImpl extends HibernateDaoSupport implements LimRightDAO{
	/**
	 * ���ĳһ����ģ������в���<br>
	 * @param funType ��������
	 * @return List Ȩ���б�
	 */
	public List getLimRight(String funType) {
		Session session = super.getSession();
		String sql ="from LimRight where limFunction.funType=:funType order by rigCode";
		Query query=session.createQuery(sql).setString("funType", funType);
		return query.list();
	}
	/**
	 * ɾ���ֿ��Ӧ��Ȩ�� <br>
	 * @param rigCode Ȩ�ޱ��
	 */
	public void delLimRight(String rigCode) {
		Session session = super.getSession();
		String sql ="delete from LimRight where rigCode =:rigCode";
		Query query=session.createQuery(sql).setParameter("rigCode", rigCode);
		query.executeUpdate();
	}
	/**
	 * ����ֿ��Ȩ���� <br>
	 * @param limRight Ȩ��ʵ��
	 */
	public void savLimRight(LimRight limRight) {
		super.getHibernateTemplate().save(limRight);
	}
	/**
	 * ����Ȩ�����òֿ� <br>
	 * @param rigCode Ȩ�ޱ��
	 * @return LimRight ����Ȩ��ʵ��
	 */
	public LimRight findLimRight(String rigCode) {
		return (LimRight) super.getHibernateTemplate().get("com.psit.struts.entity.LimRight", rigCode);
	}
	/**
	 * �޸�Ȩ��ʵ�� <br>
	 * @param limRight Ȩ��ʵ��
	 */
	public void updLimRight(LimRight limRight) {
		super.getHibernateTemplate().update(limRight);
	}

}