package com.psit.struts.DAO.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusAreaDAO;
import com.psit.struts.entity.CusArea;
/**
 * ����DAOʵ���� <br>
 */
public class CusAreaDAOImpl extends HibernateDaoSupport implements CusAreaDAO {
	
	/**
     * ��������õĹ��� <br>
     * @return List ���ع����б�
     */
	public List getCusArea() {
		return super.getHibernateTemplate().find("from CusArea where areIsenabled=1");
	}
	/**
	 * ������й��� <br>
	 * @return List ���ع����б�
	 */
	public List getAllCusArea() {
		return super.getHibernateTemplate().find("from CusArea");
	}
	/**
	 * ������� <br>
	 * @param cusArea ����ʵ��
	 */
	public void saveCountry(CusArea cusArea) {
		super.getHibernateTemplate().save(cusArea);
		
	}
	/**
	 * ���ݹ���id��ù���ʵ�� <br>
	 * @param id ����id
	 * @return CusArea ���ع���ʵ��
	 */
	public CusArea showCountry(long id){
		return (CusArea)super.getHibernateTemplate().get(CusArea.class, id);
	}
}