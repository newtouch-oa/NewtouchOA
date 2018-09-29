package com.psit.struts.DAO.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.QuoteDAO;
import com.psit.struts.entity.Quote;

/**
 * ���۱�����ݿ����ʵ���� <br>
 */
public class QuoteDAOImpl extends HibernateDaoSupport implements QuoteDAO{

	/**
	 * ���汨����Ϣ<br>
	 * @param quote ���ۼ�¼����
	 */
	public void saveQuote(Quote quote) {
		super.getHibernateTemplate().save(quote);
		
	}

	/**
	 * ����Id��ñ���<br>
	 * @param id ���ۼ�¼id
	 * @return Quote ���ر���ʵ��
	 */
	public Quote showQuote(Long id) {
		return (Quote) super.getHibernateTemplate().get(Quote.class, id);
	}

	/**
	 * ���±�����Ϣ<br>
	 * @param quote ���ۼ�¼����
	 */
	public void updateQuo(Quote quote) {
		super.getHibernateTemplate().update(quote);
	}

	/**
	 * ɾ�����ᱨ��<br>
	 * @param quote ���ۼ�¼����
	 */
	public void delQuote(Quote quote) {
		super.getHibernateTemplate().delete(quote);
	}

	/**
	 * ��ô�ɾ�������б��ۼ�¼<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ��¼��
	 * @return List ���ر��ۼ�¼�б�
	 */
	public List findDelQuote(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		
		session = super.getSession();
		String hql ="from Quote where  quoIsDel='0' order by quoInsDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

	/**
	 * ��ô�ɾ�������б��ۼ�¼����<br>
	 * create_date: Aug 9, 2010,10:38:14 AM<br>
	 * @return ���ر��ۼ�¼����
	 */
	public int findDelQuoteCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from Quote where  quoIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		
		 
		return count;
	}
}