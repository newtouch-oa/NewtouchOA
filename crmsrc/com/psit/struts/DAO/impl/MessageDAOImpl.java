package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.MessageDAO;
import com.psit.struts.entity.Message;
/**
 * ��Ϣ����DAOʵ���� <br>
 */
public class MessageDAOImpl extends HibernateDaoSupport implements MessageDAO{
	public static final String  LIM_RML_ID = "rmlId";
	public static final String 	LIM_RML_DATE = "rmlDate";
	public static final String 	LIM_RML_ISDEL = "rmlIsdel";
	public static final String  LIM_RML_STATE = "rmlState";
	public static final String 	LIM_EMP_NO = "salEmp.seNo";
	public static final String  LIM_MSG_TITLE = "message.meTitle";
	public static final String  LIM_MSG_INSUSER = "message.meInsUser";
	
	public static final String  MSG_ID = "meCode";
	public static final String  MSG_TITLE = "meTitle";
	public static final String  MSG_MEDATE = "meDate";
	public static final String  MSG_ISSEND = "meIssend";
	public static final String  MSG_ISDEL = "meIsdel";
	public static final String  MSG_EMP_NO = "salEmp.seNo";
	
	/**
	 * ������Ϣ���� <br>
	 * @param message ��Ϣʵ��
	 */
	public void save(Message message) {
		super.getHibernateTemplate().save(message);
	}
	/**
	 * ����ض��˺����ύ�ı���<br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @return List ���ر����б� 
	 */
	public List getHaveSenMess(int pageCurrentNo, int pageSize, Long empId, String orderItem, String isDe) {
		 Session session = super.getSession();
		String sql = getHaveSenMessSql(empId, orderItem, isDe, false);
		Query query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int getHaveSenMessCount(Long empId) {
		Session session = super.getSession();
		String sql = getHaveSenMessSql(empId,null,null,true);
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getHaveSenMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from Message ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where " + tab + MSG_EMP_NO + "=" + empId + " and "
				+ tab + MSG_ISDEL + " = '1' and " + tab + MSG_ISSEND
				+ " = '1' ");
		if (!isCount) {
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"title",null,"meDate"};
				String [] cols = {MSG_TITLE,null,MSG_MEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+MSG_ID+" desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	/**
	 * �����Ϣ���� <br>
	 * @param meCode ��Ϣid
	 * @return Message ������Ϣʵ�� 
	 */
	public Message showMessInfo(Long meCode) {
		return (Message) super.getHibernateTemplate().get(Message.class, meCode);
	}
	/**
	 * �������������Ϣ <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @return List ������Ϣ�б� 
	 */
	public List getAllMess(int pageCurrentNo, int pageSize,Long empId,String orderItem, String isDe) {
		Query query;
		Session session;
		session=super.getSession();
		String sql=getAllMessSql(empId,orderItem,isDe,false);
		query=session.createQuery(sql);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * �������������Ϣ���� <br>
	 * @param empId �˺�
	 * @return int ������Ϣ���� 
	 */
	public int getAllMessCount(Long empId) {
		Query query;
		Session session;
		session=super.getSession();
		String sql = getAllMessSql(empId,null,null,true);
		query=session.createQuery(sql);
		int count=Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	private String getAllMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from RMessLim ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+LIM_EMP_NO+"="+empId+" and "+tab+LIM_RML_ISDEL+"= '1' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"state","title","insUser","meDate"};
				String [] cols = {LIM_RML_STATE,LIM_MSG_TITLE,LIM_MSG_INSUSER,LIM_RML_DATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+LIM_RML_ID+" desc ");
			}
		}
		sql += appendSql.toString();
		return sql;
	}
	/**
	 * ������Ϣ <br>
	 * @param message ��Ϣʵ��
	 */
	public void update(Message message) {
		super.getHibernateTemplate().update(message);
	}
	/**
	 * ����Ѷ���δ����Ϣ�б� <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @param isRead �Ƿ��Ѷ�
	 * @return List ������Ϣ�б� 
	 */
	public List getIsReadMess(int pageCurrentNo, int pageSize, Long empId,String isRead)
	{
		Session session=super.getSession();
		String sql="select rmessLim from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRead order by rmlDate desc";
		Query query=session.createQuery(sql).setParameter("empId", empId).setParameter("isRead", isRead);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * ����Ѷ���δ����Ϣ������ <br>
	 * @param empId Ա��id
	 * @param isRead �Ƿ��Ѷ�
	 * @return int ������Ϣ�б����� 
	 */
	public int getIsReadMessCount(Long empId,String isRead) {
		Session session=super.getSession();
		Query query=session.createQuery("select count(*) from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRead")
		.setParameter("empId", empId).setParameter("isRead", isRead);
		int count =Integer.parseInt(query.uniqueResult().toString());
		return count;
	}

	
	/**
	 * 
	 * ����ض��˺Ŵ�������Ϣ <br>
	 * create_date: Aug 6, 2010,12:05:49 PM <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @return List ������Ϣ�б� 
	 */
	public List getReadySenMess(int pageCurrentNo, int pageSize, Long empId, String orderItem, String isDe) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenMessSql(empId,orderItem,isDe,false);
		query = session.createQuery(sql);
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List)query.list();
		return list;
	}
	/**
	 * ����ض��˺Ŵ�������Ϣ������ <br>
	 * @param empId Ա��id
	 * @return int ������Ϣ���� 
	 */
	public int getReadySenMessCount(Long empId) {
		Query query;
		Session session;
		session = super.getSession();
		String sql = getReadySenMessSql(empId,null,null,true);
		query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private String getReadySenMessSql(Long empId, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from Message ";
		String tab = "";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append("where "+tab+MSG_EMP_NO+"="+empId+" and "+tab+MSG_ISDEL+" = '1' and "+tab+MSG_ISSEND+" = '0' ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"meTitle","meDate"};
				String [] cols = {MSG_TITLE,MSG_MEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab +MSG_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	}
	/**
	 * ����ѻػ�δ����Ϣ�б� <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param empId Ա��id
	 * @param isRep �Ƿ�ظ�
	 * @return List ������Ϣ�б� 
	 */
	public List getIsReplyMes(int pageCurrentNo, int pageSize, Long empId, String isRep){
		Session session=super.getSession();
		String sql="select rmessLim from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRep order by rmlDate desc";
		Query query=session.createQuery(sql).setParameter("empId", empId).setParameter("isRep", isRep);
		query.setFirstResult((pageCurrentNo-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * ����ѻػ�δ����Ϣ���� <br>
	 * @param empId Ա��id
	 * @param isRep �Ƿ�ظ�
	 * @return int ������Ϣ�б����� 
	 */
	public int getIsReplyCount(Long empId,String isRep){
		Session session=super.getSession();
		Query query=session.createQuery("select count(*) from RMessLim as rmessLim where salEmp.seNo=:empId and rmlIsdel='1' and rmlState=:isRep")
		.setParameter("empId", empId).setParameter("isRep", isRep);
		int count =Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	/**
	 * ɾ����Ϣ <br>
	 * @param message ��Ϣʵ��
	 */
	public void delMess(Message message) {
		super.getHibernateTemplate().delete(message);
  	}
	/**
	 * �õ����õ���Ϣ <br>
	 * @param empId Ա��id
	 * @return List ������Ϣ�б� 
	 */
	public List getNoUseMess(Long empId){
		Session session=super.getSession();
		String sql="select mes from Message as mes where salEmp.seNo=:empId and meIsdel='2'";
		Query query=session.createQuery(sql).setParameter("empId", empId);
		List list=query.list();
		return list;
	}
}