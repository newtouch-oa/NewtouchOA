package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalOppDAO;
import com.psit.struts.entity.SalOpp;
/**
 * ���ۻ������ݿ�����ʵ���� <br>
 */
public class SalOppDAOImpl extends HibernateDaoSupport implements SalOppDAO{
	private static final Log log = LogFactory.getLog(SalOppDAOImpl.class);
	//property constants
	public static final String OPP_ID = "oppId";
	public static final String OPP_ISDEL = "oppIsDel";
	public static final String OPP_TIL = "oppTitle";
	public static final String OPP_LEV = "oppLev";
	public static final String OPP_STATE = "oppState";
	public static final String OPP_FDATE = "oppFindDate";
	public static final String OPP_EXE_DATE = "oppExeDate";
	public static final String OPP_POS = "oppPossible";
	public static final String OPP_STAGE = "oppStage";
	public static final String OPP_STAGE_ID = "oppStage.typId";
	public static final String OPP_STAGE_NAME = "oppStage.typName";
	public static final String OPP_USER_CODE = "limUser.userCode";
	public static final String OPP_USER_NUM = "limUser.userNum";
	public static final String OPP_CUS_ID = "cusCorCus.corCode";
	public static final String OPP_CUS_NAME = "cusCorCus.corName";
	public static final String OPP_SE_NO = "salEmp1.seNo";
	public static final String OPP_EMP_NAME = "salEmp1.seName";
	
	/**
	 * ���ۻ����б� <br>
	 * @param args 	[0]�Ƿ�ɾ��(1δɾ��,0��ɾ��);  [1]��Χ(0�Լ�,1ȫ��);  [2]�û���(userNum);  
	 * 				[3]��������;  [4]�ͻ�ID;  [5]�����ȶ�;  [6]����״̬;  [7]������;  
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalOpp> �����б�
	 */
	public List<SalOpp> listSalOpps(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getOppSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalOpp> list = query.list();
		return list;
	}
	public int listSalOppsCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getOppSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getOppSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "opp.";//�����
		String sql = "from SalOpp opp ";
  		if(isCount){
  			sql = "select count(opp) " + sql;
  		}
  		else{
  			sql = "select opp " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+OPP_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args.length>2){
				if(args[1]!=null && !args[1].equals("")){
			    	if(args[1].equals("0")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+OPP_SE_NO +" = '"+args[2]+"' ");
			    	}
				}
			    if(args[3]!=null && !args[3].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+OPP_TIL+" like :oppTil ");
			    }
			    if(args[4]!=null && !args[4].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+OPP_CUS_NAME+" like :corName ");
			    }
			    if(args[5]!=null && !args[5].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ OPP_LEV+" = '"+args[5]+"' ");
			    }
			    if(args[6]!=null && !args[6].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ OPP_STATE+" = '"+args[6]+"' ");
			    }
			    if(args[7]!=null && !args[7].equals("")){
			    	appendSql.append(appendSql.length()==0?"where ":"and ");
			    	appendSql.append(tab+ OPP_EMP_NAME+" like :uName ");
			    }
			}
			else{
				if(args[1]!=null && !args[1].equals("")){
					appendSql.append(appendSql.length()==0?"where ":"and ");
					appendSql.append(tab+OPP_CUS_ID+" = "+args[1]+" ");
				}
			}

		}
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = {"oppState", "oppTil", "oppLev", "oppCus","oppExeDate",
				"oppDate", "oppStage", "oppPos",null,"seName" };
				String[] cols = { OPP_STATE, OPP_TIL, OPP_LEV, OPP_CUS_NAME,OPP_EXE_DATE,
						OPP_FDATE, OPP_STAGE_NAME, OPP_POS,null,OPP_EMP_NAME};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch(i){
							case 6:
								sql += "left join " + tab + OPP_STAGE +" "; 
								break;
						}
						orderItem = cols[i];
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//Ĭ������
				appendSql.append("order by "+tab+OPP_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args.length>2){
				if(args[3]!=null && !args[3].equals("")){
					query.setString("oppTil", "%"+args[3]+"%");
				}
				if(args[4]!=null && !args[4].equals("")){
					query.setString("corName", "%"+args[4]+"%");
				}
				if(args[7]!=null && !args[7].equals("")){
					query.setString("uName", "%"+args[7]+"%");
				}
			}
		}
		return query;
	}
	/**
	 * ���������ۻ���<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @param startDate ���ۻ������ѿ�ʼʱ��
	 * @param endDate ���ۻ������ѽ���ʱ��
	 * @param range ��ʶ��ѯ��Χ0��ʾ��ѯ�ҵ����ۻ���1��ʾ��ѯ�ҵ��������ۻ���2��ʾ��ѯȫ�����ۻ���
	 * @param userCode �˺�
	 * @param userNum �û�id��������
	 * @return List ���ۻ����¼�б�<br>
	 */
	public List getOppByExeDate(int pageCurrentNo, int pageSize, Date startDate,
			Date endDate,String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  salOpp from SalOpp as salOpp where " +
			"salEmp1.seNo ='"+seNo+"'"
			+ " and oppIsDel='1' and oppExeDate between :startDate and :endDate order by oppExeDate asc";
	    }
	    /*else if(range!=null&&range.equals("1")){
	    	sql = "select  salOpp from SalOpp as salOpp where limUser.userCode!='" +
			userCode+"' and limUser.userNum like '%"+userNum+"'"
			+ " and oppIsDel='1' and oppExeDate between :startDate and :endDate order by oppExeDate asc";
	    }*/
	    else{
	    	sql = "select  salOpp from SalOpp as salOpp where " 
			+ "oppIsDel='1' and oppExeDate between :startDate and :endDate order by oppExeDate asc";
	    }
		query = session.createQuery(sql).setDate("startDate", startDate)
				.setDate("endDate", endDate);
		//this.query.setFirstResult((pageCurrentNo - 1) * pageSize);
		//this.query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	public int getOppByExeDateCount(Date startDate, Date endDate,String range,String seNo) {
		Query query;
		Session session;
		session = super.getSession();
		String sql="";
		if(range!=null&&range.equals("0")){
			sql = "select  count(*) from SalOpp as salOpp where " +
			"salEmp1.seNo ='"+seNo+"'"
			+ " and oppIsDel='1' and oppExeDate between :startDate and :endDate";
	    }
	    else{
	    	sql = "select  count(*) from SalOpp as salOpp where " 
			+ "oppIsDel='1' and oppExeDate between :startDate and :endDate";
	    }
		query = session.createQuery(sql)
				.setDate("startDate", startDate).setDate("endDate", endDate);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * ���ĳ�ͻ��µ��������ۻ���<br>
	 * @param cusCode �ͻ�id
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List ���ۻ����¼�б�<br>
	 */
	public List getCusOpp(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		Session session=super.getSession();
		String sql = getCusOppSql(cusCode,orderItem,isDe,false);
		Query query=session.createQuery(sql);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	public int getCusOppCount(Long cusCode) {
		Session session = super.getSession();
		String sql = getCusOppSql(cusCode, null, null, true);
		Query query = session.createQuery(sql);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private String getCusOppSql(Long cusId, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "so.";
		String sql = "from SalOpp so ";
		if (isCount) {
			sql = "select count(*) " + sql;
		} else {
			sql = "select so " + sql;
		}
		appendSql.append(" where " + tab + "oppIsDel = '1' and " + tab
				+ OPP_CUS_ID + " = " + cusId + " ");
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "oppState", "oppTitle", "oppLev", "oppCus",
						"oppFindDate", "oppStage", "oppPossible", null,
						"seName" };
				String[] cols = { OPP_STATE, OPP_TIL, OPP_LEV, OPP_CUS_NAME,
						OPP_FDATE, OPP_STAGE_NAME, OPP_POS, null, OPP_EMP_NAME };
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch (i) {
						case 5:
							sql += "left join " + tab + OPP_STAGE + " ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by " + tab + orderItem + " ");
				if (isDe!=null && isDe.equals("1")) {
					appendSql.append("desc ");
				}
			} else {
				appendSql.append("order by " + tab + OPP_ID + " desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	/**
	 * ���ĳ�ͻ��µ��������ۻ���(�޷�ҳ)<br>
	 * @param cusCode �ͻ�id
	 * @return List ���ۻ����¼�б�<br>
	 */
	public List getOppByCusCode(Long cusCode) {
		Session session = super.getSession();
		String sql="select salOpp from SalOpp as salOpp where oppIsDel='1' and cusCorCus.corCode=:cusCode order by oppInsDate desc";
		Query query=session.createQuery(sql).setParameter("cusCode", cusCode);	
		List list=query.list();
		return list;
	}
	/**
	 * ��ô�ɾ�����������ۻ���<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List ���ۻ����¼�б�<br>
	 */
	public List findDelOpp(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from SalOpp where  oppIsDel='0' order by oppInsDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int findDelOppCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from SalOpp where  oppIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * �������ۻ�����Ϣ<br>
	 * @param salOpp ���ۻ������<br>
	 */
	public void save(SalOpp salOpp) {
		super.getHibernateTemplate().save(salOpp);
	}
	/**
	 * ����Id������ۻ���<br>
	 * @param id ���ۻ���id
	 * @return SalOpp ���ۻ������<br>
	 */
	public SalOpp getSalOpp(Long id) {
		return (SalOpp) super.getHibernateTemplate().get(SalOpp.class, id);
	}
	/**
	 * ����Id������ۻ���(��ɾ��״̬)<br>
	 * @param id ���ۻ���id
	 * @return SalOpp ���ۻ������<br>
	 */
	public SalOpp showSalOpp(Long id) {
		String sql="from SalOpp where oppId="+id+" and oppIsDel='1'";
		List list=super.getHibernateTemplate().find(sql);
		if (list.size() > 0) {
			return (SalOpp) list.get(0);
		}
		else
			return null;
	}
	/**
	 * �������ۻ�����Ϣ<br>
	 * @param salOpp ���ۻ������<br>
	 */
	public void update(SalOpp salOpp) {
		super.getHibernateTemplate().update(salOpp);
		
	}
	/**
	 * ɾ�����ۻ���<br>
	 * @param salOpp ���ۻ������<br>
	 */
	public void delOpp(SalOpp salOpp) {
		super.getHibernateTemplate().delete(salOpp);
	}
}