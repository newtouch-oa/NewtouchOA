package com.psit.struts.DAO.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalPaidPlanDAO;
import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.SalPaidPlan;
import com.psit.struts.util.format.GetDate;
/**
 * �ؿ�ƻ����ݿ����ʵ���� <br>
 */
public class SalPaidPlanDAOImpl extends HibernateDaoSupport implements SalPaidPlanDAO {
	private static final Log log = LogFactory.getLog(SalPaidPlanDAOImpl.class);
	//property constants
	public static final String SPD_ID = "spdId";
	public static final String SPD_ISDEL = "spdIsdel";
//	public static final String SPD_CON = "spdContent";
	public static final String SPD_ISP = "spdIsp";
	public static final String SPD_MON = "spdPayMon";
	public static final String SPD_PRM_DATE = "spdPrmDate";
//	public static final String SPD_CUS = "cusCorCus";
//	public static final String SPD_CUS_ID = "cusCorCus.corCode";
	public static final String SPD_CUS_NAME = "cusCorCus.corName";
	public static final String SPD_ORD = "salOrdCon";
	public static final String SPD_ORD_ID = "salOrdCon.sodCode";
	public static final String SPD_ORD_TIL = "salOrdCon.sodTil";
	public static final String SPD_ORD_ISDEL = "salOrdCon.sodIsfail";
	public static final String SPD_ORD_ISAPP = "salOrdCon.sodAppIsok";
//	public static final String SPD_USER = "spdResp";
	public static final String SPD_USER_NAME = "spdResp.seName";
	public static final String SPD_USER_ID = "spdResp.seNo";
	
	protected void initDao() {
		// do nothing
	}
	/**
	 * ����(��ɾ��״̬)<br>
	 * @param id �ؿ�ƻ�id
	 * @return SalPaidPlan �ؿ�ƻ�����<br>
	 */
	public SalPaidPlan findById(Long id) {
		String sql = "from SalPaidPlan where "+SPD_ISDEL+"='0' and "+SPD_ID+"=" + id;
		List list = super.getHibernateTemplate().find(sql);
		if (list.size() > 0) {
			return (SalPaidPlan) list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 *  ���»ؿ�ƻ�<br>
	 * @param paid �ؿ�ƻ�����
	 */
	public void updatePaid(SalPaidPlan paid){
		this.getSession().merge(paid);
	}
	
	/**
	 * ɾ���ؿ�ƻ����������վ��<br>
	 * @param paidId �ؿ�ƻ�id
	 */
	public void invalid(Long paidId){
		Session session=super.getSession();
		String sql = "update SalPaidPlan set "+SPD_ISDEL+" = '1' where "+SPD_ID+" = " + paidId + " ";
		session.createQuery(sql).executeUpdate();
	}
	
	/**
	 * ��ȫɾ���ؿ�ƻ�<br>
	 * @param persistentInstance �ؿ�ƻ�����<br>
	 */
	public void delete(SalPaidPlan persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}
	
	/**
	 * ���²�ѯ�ƻ��ܶ�<br>
	 * @param userId �û�id
	 * @param year �ؿ�ƻ����꣩
	 * @param month �ؿ�ƻ����£�
	 * @return String �ؿ���
	 */
	public String getPlanMonSum(String userId, String year, String month) {
		String monSum;
		Session session = super.getSession();
		 
		String hql = "select sum("+SPD_MON+") from SalPaidPlan where "+SPD_USER_ID+"='"
				+ userId
				+ "' and year("+SPD_PRM_DATE+") = '"
				+ year
				+ "' and month("+SPD_PRM_DATE+") ='" + month + "' and "+SPD_ISDEL+"='0'"
				+ " and "+SPD_ORD_ISDEL+" = '0' and "+SPD_ORD_ISAPP+"='1'";
		Query query = session.createQuery(hql);
		List list = query.list();
		if(list.get(0)!=null){
			monSum=list.get(0).toString();
		}
		else{
			monSum="0";
		}
		return monSum;
	}
	/**
	 * ���²�ѯ�¼��ƻ��ܶ�<br>
	 * @param userId �û�id
	 * @param year �ؿ�ƻ����꣩
	 * @param month �ؿ�ƻ����£�
	 * @return String �ؿ���<br>
	 */
	public String getPlanSumWithUNum(String userId, String year, String month) {
		String monSum;
		Session session = super.getSession();
		String hql = "select sum("+SPD_MON+") from SalPaidPlan where "+SPD_USER_ID+" = "
				+ userId
				+ " and year("+SPD_PRM_DATE+") = '"
				+ year
				+ "' and month("+SPD_PRM_DATE+") ='" + month + "' and "+SPD_ISDEL+"='0'"
				+ " and "+SPD_ORD_ISDEL+" = '0' and "+SPD_ORD_ISAPP+"='1'";
		Query query = session.createQuery(hql);
		List list = query.list();
		if(list.get(0)!=null){
			monSum=list.get(0).toString();
		}
		else{
			monSum="0";
		}
		return monSum;
	}
	
	/**
	 * ��ʱ���ѯ�Լ����¼���δ�ؿ�ƻ��б�(����̨)<br>
	 * @param userId �û���ID
	 * @param startDate �ؿ�ƻ�ʱ�俪ʼ
	 * @param endDate �ؿ�ƻ�ʱ�����
	 * @return List<SalPaidPlan> �ؿ�ƻ��б�<br>
	 */
	public List<SalPaidPlan> getPlanByTime(String userId,Date startDate, Date endDate){
		Session session = super.getSession();
		Query query = session.createQuery("from SalPaidPlan where "+SPD_USER_ID+" =:userId "
				+"and "+SPD_PRM_DATE+" between :startDate and :endDate "
				+"and "+SPD_ISP+"='0' and "+SPD_ISDEL+"='0'"
				+ " and "+SPD_ORD_ISDEL+" = '0' and "+SPD_ORD_ISAPP+"='1' order by "+SPD_PRM_DATE)
				.setString("userId", userId).setDate("startDate", startDate).setDate("endDate", endDate);
		List<SalPaidPlan> list = query.list();
		return list;
	}
	/**
	 * ��ѯ���ڼƻ�(����̨)<br>
	 * @param userId �û�id
	 * @param endDate �ؿ�ƻ�ʱ�����
	 * @return List<SalPaidPlan> �ؿ�ƻ��б�<br>
	 */
	public List<SalPaidPlan> getPastPlan(String userId,Date endDate){
		Session session = super.getSession();
		Query query = session
				.createQuery(
						"from SalPaidPlan where "+SPD_USER_ID+" = :userId "
						+"and "+SPD_PRM_DATE+" < :endDate and "+SPD_ISP+"='0' and "+SPD_ISDEL+"='0'"
						+ " and "+SPD_ORD_ISDEL+" = '0' and "+SPD_ORD_ISAPP+"='1' order by "+SPD_PRM_DATE)
						.setString("userId",userId).setDate("endDate", endDate);
		List<SalPaidPlan> list = query.list();
		return list;
	}
	
	/**
	 * ����ؿ�ƻ�<br>
	 * @param transientInstance �ؿ�ƻ�����<br>
	 */
	public void save(SalPaidPlan transientInstance) {
		getHibernateTemplate().save(transientInstance);
	}
	
	/**
	 * ��ѯ��ɾ���Ļؿ�ƻ�<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ��ʾ��¼��
	 * @return List �ؿ�ƻ��б�<br>
	 */
	public List findDelPaidPlan(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from SalPaidPlan where "+SPD_ISDEL+"='1' order by "+SPD_ID+" desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

	/**
	 * ��ô�ɾ�������лؿ�ƻ�����<br>
	 * @return �ؿ�ƻ���¼��<br>
	 */
	public int findDelPaidPlanCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from SalPaidPlan where "+SPD_ISDEL+"='1'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}

	/**
	 * ����id��ûؿ�ƻ�������վ���ã�<br>
	 * @param id �ؿ�ƻ�id
	 * @return SalPaidPlan �ؿ�ƻ�����<br>	 
	 * */
	public SalPaidPlan getById(Long id) {
		return (SalPaidPlan) super.getHibernateTemplate().get(SalPaidPlan.class, id);
	}
	
	/**
	 * �ؿ�ƻ��б� <br>
	 * create_date: Jan 31, 2011,11:53:30 AM <br>
	 * @param args 	[0]�Ƿ�ɾ��(1��ɾ��,0δɾ��); [1]��ѯ��Χ(0�ҵ�,1ȫ��)/����ID; [2]�û�ID;  [3]��ע����Ч��;  [4][5]�ؿ����ڷ�Χ;  
	 * 				[6]�б�ɸѡ����;
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return List<SalPaidPlan> �ؿ�ƻ��б�
	 */
	public List<SalPaidPlan> listPaidPlans(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getPlanSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<SalPaidPlan> list = query.list();
		return list;
	}
	public int listPaidPlansCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getPlanSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getPlanSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		String curDate = GetDate.parseStrDate(GetDate.getCurDate());
		String weekDate = GetDate.parseStrDate(GetDate.getDate(curDate, 7));
		String halfMonDate = GetDate.parseStrDate(GetDate.getDate(curDate, 15));
		String monthDate = GetDate.parseStrDate(GetDate.getDate(curDate, 30));
		StringBuffer appendSql = new StringBuffer();
		String tab = "plan.";//�����
		String sql = "from SalPaidPlan plan ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select plan " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+SPD_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args.length>2){
				if(args[1]!=null && !args[1].equals("")){
			    	if(args[1].equals("0")){
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+SPD_USER_ID +" = '"+args[2]+"' ");
			    	}
				}
				/*if(args[3]!=null && !args[3].equals("")){
		    		appendSql.append("and ");
		    		appendSql.append(tab+SPD_CON +" like :spdCon ");
				}*/
			    if(args[4]!=null){
			    	if (!args[4].equals("") && args[5].equals("")) {
			    		appendSql.append("and ");
			    		appendSql.append(tab+SPD_PRM_DATE+" >= '"+ args[4] + "' ");
					} else if (!args[4].equals("") && !args[5].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPD_PRM_DATE + " between '"
								+ args[4] + "' and '"
								+ args[5]
								+ "' ");
					} else if (args[4].equals("") && !args[5].equals("")) {
						appendSql.append("and ");
						appendSql.append(tab + SPD_PRM_DATE + " <= '"
								+ args[5]
								+ "' ");
					}
			    }
			    if(args[6]!=null && !args[6].equals("")){
			    	String[] filter = {"7","15","30","expired","all"};
			    	for (int i = 0; i < filter.length; i++) {
						if (args[6].equals(filter[i])) {
							switch(i){
					    	case 0:
					    		appendSql.append("and ");
					    		appendSql.append(tab + SPD_PRM_DATE + " between '" + curDate + "'"
						    			+ " and '" + weekDate + "' ");
					    		break;
					    	case 1:
					    		appendSql.append("and ");
					    		appendSql.append(tab + SPD_PRM_DATE + " between '" + curDate + "'"
						    			+ " and '" + halfMonDate + "' ");
					    		break;
					    	case 2:
					    		appendSql.append("and ");
					    		appendSql.append(tab + SPD_PRM_DATE + " between '" + curDate + "'"
						    			+ " and '" + monthDate + "' ");
					    		break;
					    	case 3:
					    		appendSql.append("and ");
						    	appendSql.append(tab + SPD_PRM_DATE + "<'" + curDate + "' ");
					    		break;
					    	}
							appendSql.append("and " + tab + SPD_ISP + "='0' ");
							break;
						}
			    	}
			    }
			}
		    else{
		    	if(args[1]!=null && !args[1].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SPD_ORD_ID +" = "+args[1]+" ");
				}
		    }
		}
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "spdCus","spdOrd", "spdMon", "spdDate", "spdUser", "spdIsP" };
				String[] cols = { SPD_CUS_NAME, SPD_ORD_TIL, SPD_MON, SPD_PRM_DATE, SPD_USER_NAME, SPD_ISP };
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch (i) {
						case 1:
							sql += "left join " + tab + SPD_ORD + " ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//Ĭ������
				appendSql.append("order by "+tab+SPD_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		/*if(args!=null){
			if(args[3]!=null && !args[3].equals("")){
				query.setString("spdCon", "%"+args[3]+"%");
			}
		}*/
		return query;
	}
}