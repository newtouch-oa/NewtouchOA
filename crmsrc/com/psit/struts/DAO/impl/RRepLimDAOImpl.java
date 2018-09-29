package com.psit.struts.DAO.impl;


import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RRepLimDAO;
import com.psit.struts.entity.RRepLim;
/**
 * ���������DAOʵ���� <br>
 */
public class RRepLimDAOImpl extends HibernateDaoSupport implements RRepLimDAO
{
	/**
	 * ���汨������� <br>
	 * @param rrepLim ���������ʵ��
	 */
	public void save(RRepLim rrepLim) 
	{
		super.getHibernateTemplate().save(rrepLim);
	}
	/**
	 * ���������ʵ�� <br>
	 * @param id ������id
	 * @return RRepLim ���ر��������ʵ��
	 */
	public RRepLim getRRepLim(Long id) {
		return (RRepLim) super.getHibernateTemplate().get(RRepLim.class, id);
	}
	/**
	 * ������������ <br>
	 * @param rrepLim ���������ʵ��
	 */
	public void update(RRepLim rrepLim) {
		super.getHibernateTemplate().update(rrepLim);
	}
	/**
	 * �鿴ĳһ�����Ƿ������һ�������� <br>
	 * @param appOrder ��������
	 * @param repCode ����id
	 * @return List ���ر����б�
	 */
	public List getRRepLim(Integer appOrder, String repCode) {
		String sql="from RRepLim where rrlAppOrder="+appOrder+" and report.repCode='"+repCode+"' and rrlIsdel='1' order by rrlId desc";
		return super.getHibernateTemplate().find(sql);
	}
	/**
	 * �鿴ĳһ�����Ƿ��������� <br>
	 * @param repCode ����id
	 * @return List ���ر����б�
	 */
	public List getRRepLim(String repCode) {
		String sql="from RRepLim where report.repCode='"+repCode+"' and rrlIsdel='1' order by rrlId desc";
		return super.getHibernateTemplate().find(sql);
	}

//	public void delRRepLim(RRepLim rrepLim) {
//		super.getHibernateTemplate().delete(rrepLim);
//		
//	}
	
}