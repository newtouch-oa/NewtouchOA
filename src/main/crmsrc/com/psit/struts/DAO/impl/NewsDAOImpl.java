package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.NewsDAO;
import com.psit.struts.entity.News;
/**
 * ���Ź���DAOʵ���� <br>
 */
public class NewsDAOImpl extends HibernateDaoSupport implements NewsDAO{
	public static final String TOTOP = "newIstop";
	public static final String NEWTYPE = "newType";
	public static final String NEWTITLE = "newTitle";
	public static final String NEW_INS_USER = "newInsUser";
//	public static final String SONAME = "news1.salEmp.salOrg.soName";
	public static final String NEWDATE = "newDate";
	/**
	 * �������Ź��� <br>
	 * @param news ���Ź���ʵ��
	 */
	public void saveNews(News news) {
		super.getHibernateTemplate().save(news);
	}
	/**
	 * �������������Ź��棨�޷�ҳ�� <br>
	 * @param empId Ա��id
	 * @param type ����
	 * @return List �������Ź����б� 
	 */
	public List getAllNews(Long empId,String type) {
		Session session=super.getSession();
		String sql = "select news1 from News as news1 where newType in (:types) and newCode in(select news.newCode from RNewLim where salEmp.seNo="
				+ empId + ") order by newIstop,newDate desc";
		Query query = session.createQuery(sql).setString("types", type);
		query.setFirstResult(0);
		query.setMaxResults(15);
		List list=query.list();
		return list;
	}
	
	/**
	 * ��������������Ź��棨�з�ҳ�� <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @return List �������Ź����б� 
	 */
	public List getAllNews(int pageCurrentNo, int pageSize,String orderItem,String isDe,Long empId) {
		Session session = (Session) super.getSession();
		String sql = getHql(empId, orderItem, isDe, false);
		Query query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int getAllNewsCount(Long empId) {
		Session session = (Session) super.getSession();
		String sql = getHql(empId, null, null, true);
		Query query = session.createQuery(sql);
        int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	public String getHql (Long empId,String orderItem,String isDe,boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from News ";
  		if(isCount){
  			sql = "select count(*) " + sql+" ";
  		}
		if(empId != null  && !empId.equals("")){
		   sql +=  "where newCode in(select news.newCode from RNewLim where salEmp.seNo="+empId+") ";
		
		}
		if(!isCount){
			appendSql.append("order by " + TOTOP+" , ");
			if(orderItem != null && !orderItem.equals("")){
				String[] items = {"newType","newTitle","newInsUser","newDate"};
				String cols[] = {NEWTYPE,NEWTITLE,NEW_INS_USER,NEWDATE};
				for(int i = 0; i < items.length; i++){
					if(orderItem.equals(items[i])){
						  orderItem = cols[i];
						}
				}
				appendSql.append(orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else {//Ĭ������
				appendSql.append("newCode desc ");
			}
		}
		return sql+appendSql.toString();
	}
	/**
	 * �������ű�Ż�����Ź��� <br>
	 * @param newCode ���ű��
	 * @return News ��������ʵ�� 
	 */
	public News showNewsInfo(Long newCode) {
		return (News) super.getHibernateTemplate().get(News.class, newCode);
	}
	/**
	 * ��������ѷ������Ź��� <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @return List �������Ź����б� 
	 */
	public List getHaveSenNews(String orderItem, String isDe,int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = "from News ";
		StringBuffer appendSql = new StringBuffer("");
		appendSql.append("order by "+TOTOP+" , ");
		if(orderItem != null && !orderItem.equals("")){
			String[] items = {"newType","newTitle","newInsUser","newDate"};
			String cols[] = {NEWTYPE,NEWTITLE,NEW_INS_USER,NEWDATE};
			for(int i = 0; i < items.length; i++){
				if(orderItem.equals(items[i])){
					  orderItem = cols[i];
					}
			}
			appendSql.append(orderItem + " ");
			if(isDe!=null && isDe.equals("1")){
				appendSql.append("desc ");
			}
		}
		else {//Ĭ������
			appendSql.append("newCode desc ");
		}
		sql = sql + appendSql.toString();
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	/**
	 * ����ض��˺��ѷ������Ź�������� <br>
	 * @param empId �˺�
	 * @return int �������Ź����б����� 
	 */
	public int getHaveSenNewsCount() {
		Query query;
		Session session;
		session = super.getSession();
		query = session.createQuery("select count(*) from News ");
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * �������Ź�����Ϣ <br>
	 * @param news ���Ź���ʵ��
	 */
	public void update(News news) {
		super.getHibernateTemplate().update(news);
	}
	/**
	 * ɾ�����Ź��� <br>
	 * @param news ���Ź���ʵ��
	 */
	public void delNews(News news) {
		super.getHibernateTemplate().delete(news);
	}
	
	/**
	 * ��������Id�������н����� <br>
	 * @param newId ����Id
	 * @return List ���ؽ������б�<br>
	 */
	public List findAllMsgMan(Long newId){
		try {
			String queryString = "from RNewLim where news.newCode="+newId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * ������ŵĽ����� <br>
	 * @param newId void ����Id<br>
	 */
	public void delRnewLim(Long newId){
		Session session = (Session) super.getSession();
		String sql = "delete r_new_lim where rnl_new_code = "+newId;
		session.createSQLQuery(sql).executeUpdate();
	}
}