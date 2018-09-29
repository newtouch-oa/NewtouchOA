package com.psit.struts.DAO.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalInvoiceDAO;
import com.psit.struts.entity.ProdShipment;
import com.psit.struts.entity.SalInvoice;
import com.psit.struts.entity.SalPaidPast;


/**
 * Ʊ�ݼ�¼���ݿ����ʵ����<br>
 */
public class SalInvoiceDAOImpl extends HibernateDaoSupport implements SalInvoiceDAO {
	//property constants
	static final String SIN_ID = "sinId";
	static final String SIN_CUS_ID = "cusCorCus.corCode";
	static final String SIN_ISRECIEVE = "sinIsrecieve";
	static final String SIN_CUS_NAME = "cusCorCus.corName";
	static final String SIN_ORD_TIL = "salOrdCon.sodTil";
	static final String SIN_MON = "sinMon";
	static final String SIN_TYPE_NAME = "typeList.typName";
	static final String SIN_DATE = "sinDate";
	static final String SIN_SE_NAME = "salEmp.seName";
	static final String SIN_CUS = "cusCorCus";
	static final String SIN_ORD = "salOrdCon";
	static final String SIN_TYPE = "typeList";
	static final String SIN_SE = "salEmp";
	
	
	protected void initDao() {
		// do nothing
	}
	

	/**
	 * ���¿�Ʊ��� <br>
	 * @param salInvoice  ��Ʊ��¼
	 * @param session  
	 */
	private void updateTicketNum(SalInvoice salInvoice,double oldSin,Session session,int type){
		if(salInvoice!=null){
			double sinMon = (salInvoice.getSinMon()!=null)?salInvoice.getSinMon():0;
			switch(type){
			case 0:break;//�½�
			case 1:
				if(oldSin>0){
					sinMon = sinMon - oldSin; 
				}
				break;//����
			case 2:
				sinMon = -sinMon;
				break;//ɾ��
			}
			if(salInvoice.getCusCorCus()!=null){
				String hql = "update CusCorCus set "+CusCorCusDAOImpl.COR_TICKET_NUM+" = "+CusCorCusDAOImpl.COR_TICKET_NUM+"+(" + sinMon + ") where "+CusCorCusDAOImpl.CUS_ID+" = " + salInvoice.getCusCorCus().getCorCode();
				session.createQuery(hql).executeUpdate();
			}
		}
	}
	
	/**
	 * ����Ʊ�ݼ�¼<br>
	 * @param paid Ʊ�ݼ�¼����<br>
	 */
	public void save(SalInvoice invoice) {
		Session session=super.getSession();
		updateTicketNum(invoice,0.0,session,0);
		session.save(invoice);
	}
	
	/**
	 * ����Ʊ�ݼ�¼<br>
	 * @param paid Ʊ�ݼ�¼����
	 */
	public void update(SalInvoice invoice,Double oldSin){
		Session session=super.getSession();
		updateTicketNum(invoice,oldSin ,session,1);
		session.merge(invoice);
	}
	
	/**
	 * ����ɾ��<br>
	 * @param paid Ʊ�ݼ�¼����<br>
	 */
	public void delete(SalInvoice invoice) {
		Session session=super.getSession();
		updateTicketNum(invoice,0.0,session,2);
		getHibernateTemplate().delete(invoice);
	}
	
	/**
	 * ����id���Ʊ�ݼ�¼����<br>
	 * @param id Ʊ�ݼ�¼����id
	 * @return SalInvoice Ʊ�ݼ�¼����<br>
	 */
	public SalInvoice findById(Long id) {
		String sql="from SalInvoice where sinId="+id;
		List list=super.getHibernateTemplate().find(sql);
		if(list.size()>0)
			return (SalInvoice) list.get(0);
		else
			return null;
	}
	
	/**
	 * ����id���Ʊ�ݼ�¼���󣨻���վ���ã�<br>
	 * @param id Ʊ�ݼ�¼����id
	 * @return SalInvoice Ʊ�ݼ�¼������<br>
	 */
	public SalInvoice getById(Long id)
	{
		return (SalInvoice) super.getHibernateTemplate().get(SalInvoice.class, id);
	}
	
	/**
	 * Ʊ�ݼ�¼�б� <br>
	 * @param args 	[0]
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalPaidPast> Ʊ�ݼ�¼�б�
	 */
	public List<SalInvoice> listSalInvoices(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getPastSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalInvoice> list = query.list();
		return list;
	}
	public int listSalInvoicesCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getPastSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getPastSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "si.";//�����
		String sql = "from SalInvoice si ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select si " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
				appendSql.append(appendSql.length()==0?"where ":"and ");
				appendSql.append(tab+SIN_CUS_ID+"="+args[0]+" ");
			}
		}
		if (!isCount) {
			String[] sortSqls = getSortSql(sql,tab,orderItem,isDe);
			sql+=sortSqls[0];
			appendSql.append(sortSqls[1]);
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		return query;
	}
	
	/**
	 * ����������� <br>
	 * @param sql ԭ������sql
	 * @param tab ����
	 * @param orderItem �����ֶ�
	 * @param isDe	�Ƿ�����
	 * @return String[] �������
	 */
	private String[] getSortSql(String sql,String tab,String orderItem,String isDe){
		String joinSql = "";
		String sortSql = "";
		if (orderItem != null && !orderItem.equals("")) {
			String[] items = { "sinIsrecieve","sinCus","sinOrd","sinMon","typName","sinDate","seName" };
			String[] cols = {  SIN_ISRECIEVE,SIN_CUS_NAME, SIN_ORD_TIL, SIN_MON, SIN_TYPE_NAME, SIN_DATE,SIN_SE_NAME };
			for (int i = 0; i < items.length; i++) {
				if (orderItem.equals(items[i])) {
					switch (i) {
					case 1:
						joinSql += "left join " + tab + SIN_CUS + " ";
						break;
					case 2:
						joinSql += "left join " + tab + SIN_ORD + " ";
						break;
					case 4:
						joinSql += "left join " + tab + SIN_TYPE + " ";
						break;
					case 6:
						joinSql += "left join " + tab + SIN_SE + " ";
						break;
					}
					orderItem = cols[i];
					break;
				}
			}
			sortSql = "order by " +tab+orderItem + " ";
			if(isDe!=null && isDe.equals("1")){
				sortSql+="desc ";
			}
		} else {//Ĭ������
			sortSql = "order by "+tab+SIN_ID+" desc ";
		}
		return new String[]{joinSql,sortSql};
	}
	

	
}