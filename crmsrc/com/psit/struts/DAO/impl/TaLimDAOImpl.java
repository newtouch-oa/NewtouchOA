package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.TaLimDAO;
import com.psit.struts.entity.TaLim;
/**
 * ����ִ����DAOʵ���� <br>
 */
public class TaLimDAOImpl extends HibernateDaoSupport implements TaLimDAO{
	private static final Log log = LogFactory.getLog(TaLimDAOImpl.class);
	// property constants
	public static final String TA_LIM_ID = "taLimId";
	public static final String TA_ISDEL = "taIsdel";
	public static final String TA_ISFIN = "taIsfin";
	public static final String TA_TITLE = "salTask.stTitle";
	public static final String TA_STATE = "salTask.stStu";
	public static final String TA_EMP_NO = "salEmp.seNo";
	public static final String TA_SALTASK_ISDEL ="salTask.stIsdel";
	public static final String TA_SALTASK_TYPE_NAME ="salTask.salTaskType.typName";
	public static final String TA_SALTASK_TYPE = "salTask.salTaskType";
	public static final String TA_STNAME = "salTask.stName";
	public static final String TA_STRELDATE = "salTask.stRelDate";
	public static final String TA_START_DATE = "salTask.stStartDate";
	public static final String TA_END_DATE  ="salTask.stFinDate";
	/**
	 * ���湤������ִ���� <br>
	 * @param transientInstance ��������ִ����ʵ��
	 */
	public void save(TaLim transientInstance) {
		log.debug("saving TaLim instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * ɾ��ִ���� <br>
	 * @param persistentInstance ����ִ����ʵ��
	 */
	public void delete(TaLim persistentInstance) {
		log.debug("deleting TaLim instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * ���ݹ���ִ����ID��ѯʵ�� <br>
	 * @param id ����ִ����id
	 * @return TaLim ���ع���ִ����ʵ��
	 */
	public TaLim findById(java.lang.Long id) {
		log.debug("getting TaLim instance with id: " + id);
		try {
			TaLim instance = (TaLim) getHibernateTemplate().get(
					"com.psit.struts.entity.TaLim", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * ���ݹ���Idɾ��ִ���� <br>
	 * @param stId ��������id
	 */
	public void delByTaskId(Long stId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		log.debug("finding all TaLim instances");
		try {
			String queryString = "delete TaLim  where taIsfin='1' and salTask.stId="+stId;
			 int i=session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * ĳ�˵Ĺ������Ų�ѯ�б�������,״̬��������ڣ� <br>
	 * @param args 0:��������
	 * 			   1:����״̬
	 *    		   2:����ִ���б�ʶ
	 *    		   3:���ڱ�ʶ
	 *    		   4:Ա��Id
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @return List ���ع��������б�
	 */
	public List myTaskSearch(String [] args, int currentPage, int pageSize, String orderItem, String isDe) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		query = getMyTaskSql(session,args,orderItem,isDe,false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = (List) query.list();
		return list;
	}
	public int myTaskCount(String [] args) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		query = getMyTaskSql(session,args,null,null,true);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	private Query getMyTaskSql(Session session, String [] args, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "taLim.";
		String sql = "from TaLim  taLim ";
		if(isCount){
			sql ="select count(*) "+sql;
		}else{
			sql = "select taLim "+sql;
		}
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+TA_TITLE+" like :title ");
			}
			if(args[1]!=null && !args[1].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append(tab+TA_STATE +" like :state ");
			}
			if(args[2]!=null && args[2].equals("tip")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append("("+tab+TA_ISDEL+"='0' or "+tab+TA_ISDEL+"='2') "); 
			}
			if(args[3]!=null && !args[3].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
	    		appendSql.append(tab+TA_ISDEL +"= '"+args[3]+"' ");
			}
			if(args[4]!=null && !args[4].equals("")){
	    		appendSql.append(appendSql.length()==0?"where ":"and ");
    			appendSql.append(tab+TA_EMP_NO +"= '"+args[4]+"' and "+tab+TA_SALTASK_ISDEL+"= '1' and "+tab+TA_ISFIN+"= '1' ");
			}
		}
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items =  {"stStu","typName","stTitle","startTime","endTime",null,"stName","stRelDate"};
				String [] cols = {TA_STATE,TA_SALTASK_TYPE_NAME,TA_TITLE,TA_START_DATE,TA_END_DATE,null,TA_STNAME,TA_STRELDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						switch(i){
							case 1:
								sql +="left join "+tab+TA_SALTASK_TYPE+" ";
								break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}else{
				appendSql.append("order by "+tab+TA_LIM_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				query.setString("title", "%"+args[0]+"%");
			}
			if(args[1]!=null && !args[1].equals("")){
				query.setString("state", "%"+args[1]+"%");
			}
		}
		return query;
	}
	/**
	 * ���ݹ�������Id��ѯִ���� <br>
	 * @param stId ����id
	 * @return List ���ع���ִ�����б�
	 */
	public List findByTaskId(Long stId){
		log.debug("finding all TaLim instances");
		try {
			String queryString = "from TaLim where  taIsfin='1' and salTask.stId="+stId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	//���ݹ���Id��ѯִ����
	public List findAllTaskMan(Long stId){
		log.debug("finding all TaLim instances");
		try {
			String queryString = "from TaLim where salTask.stId="+stId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ���¹���ִ���� <br>
	 * @param instance ����ִ����ʵ��
	 */
	public void updateTaskMan(TaLim instance) {
		log.debug("attaching dirty TaLim instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	/**
	 * �������ʱ�޲�ѯ�յ��Ĺ��� <br>
	 * @param userId Ա��Id
	 * @param startDate �������(��ʼ)
	 * @param endDate �������(����)
	 * @param stu ״̬
	 * @return List ���ع���ִ�����б�
	 */
	public List getMyTaskByDate(String userId,Date startDate,Date endDate,String stu){
		Session session = (Session) super.getSession();
		String hql = "from TaLim where salEmp.seNo="+userId+" and taIsfin='0' and salTask.stIsdel='1'";
		if(startDate!=null&&endDate!=null){
			hql += " and salTask.stFinDate between "+startDate+" and "+endDate; 
		}
		else if(endDate==null){
			hql += " and salTask.stFinDate > "+startDate; 
		}
		else{
			hql += " and salTask.stFinDate < "+endDate; 
		}
		if(stu!=null&&!stu.equals("")){
			hql += " and taIsdel='"+stu+"'";
		}
		hql+=" order by salTask.stFinDate";
		Query query=session.createQuery(hql);	
		List list=(List)query.list();
		return list;
	}
	/**
	 * �õ�δ�趨���ʱ�޵��յ��Ĺ��� <br>
	 * @param userCode �˺�
	 * @param stu ����״̬
	 * @return List ���ع����б�
	 */
	public List getMyTaskOfNoDate(String userCode,String stu){
		Session session = (Session) super.getSession();
		String hql;
		if(stu!=null&&!stu.equals("")){
			hql ="from TaLim where salTask.stFinDate=null and salTask.stIsdel='0' and taIsfin='1' and taIsdel='"+stu+"' order by taLimId desc ";
		}
		else{
			hql ="from TaLim where salTask.stFinDate=null and salTask.stIsdel='0' and taIsfin='1' order by taLimId desc ";
		}
		Query query=session.createQuery(hql);	
		List list=(List)query.list();
		return list;
	}
	/**
	 * �õ���ִ�е��յ��Ĺ��� <br>
	 * @param userId Ա��Id
	 * @return List<TaLim> ���ع���ִ�����б�
	 */
	public List<TaLim> getTodoMyTask(String userId){
		Session session = (Session) super.getSession();
		String hql = "from TaLim where salEmp.seNo='"+userId+"' and salTask.stStu='0' and salTask.stIsdel='1' and taIsfin='1' order by salTask.stFinDate";
		Query query=session.createQuery(hql);	
		List<TaLim> list=(List)query.list();
		return list;
	}
	/**
	 * ���ݹ���Id��ѯ����ɾ��ִ���� <br>
	 * @param stId ����Id
	 */
	public void updateByTaskId(Long stId){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		log.debug("finding all TaLim instances");
		try {
			String queryString = "update TaLim set taIsfin='0' where  salTask.stId="+stId;
			session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}