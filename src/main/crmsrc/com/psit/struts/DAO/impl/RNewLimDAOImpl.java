package com.psit.struts.DAO.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RNewLimDAO;
import com.psit.struts.entity.RNewLim;
/**
 * ���Ź��������DAOʵ���� <br>
 */
public class RNewLimDAOImpl extends HibernateDaoSupport implements RNewLimDAO{
	/**
	 * �������Ź�������� <br>
	 * @param rnewLim ���Ž�����ʵ��
	 */
	public void save(RNewLim rnewLim) {
		super.getHibernateTemplate().save(rnewLim);
		
	}
}