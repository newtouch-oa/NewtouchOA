package com.psit.struts.DAO.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsWarInDAO;
import com.psit.struts.entity.WmsStro;
import com.psit.struts.entity.WmsWarIn;
import com.psit.struts.util.OperateDate;
/**
 * 
 * ��ⵥDAOʵ���� <br>
 *
 * create_date: Aug 19, 2010,2:15:09 PM<br>
 * @author zjr
 */
public class WmsWarInDAOImpl extends HibernateDaoSupport implements WmsWarInDAO{
	private static final Log log = LogFactory.getLog(WmsWarInDAOImpl.class);
	java.util.Date starTime=new Date();  
	java.util.Date  endTime=OperateDate.getDate(starTime, 1);
	//��������
	java.sql.Date starTime1=new java.sql.Date(starTime.getTime());
	java.sql.Date endTime1=new java.sql.Date(endTime.getTime());
	
//	protected void initDao() {
//		// do nothing
//	}
	/**
	 * 
	 * ������ⵥ <br>
	 * create_date: Aug 11, 2010,2:33:07 PM <br>
	 * @param transientInstance ��ⵥʵ��
	 */
	public void save(WmsWarIn transientInstance) {
		log.debug("saving WmsWarIn instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ɾ��δ������ⵥ <br>
	 * create_date: Aug 11, 2010,2:59:37 PM <br>
	 * @param persistentInstance ��ⵥʵ��
	 */
	public void delete(WmsWarIn persistentInstance) {
		log.debug("deleting WmsWarIn instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

//	public WmsWarIn findById(java.lang.String id) {
//		log.debug("getting WmsWarIn instance with id: " + id);
//		try {
//			WmsWarIn instance = (WmsWarIn) getHibernateTemplate().get(
//					"com.psit.struts.DAO.impl.WmsWarIn", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//		}
//	}

//	public List findByExample(WmsWarIn instance) {
//		log.debug("finding WmsWarIn instance by example");
//		try {
//			List results = getHibernateTemplate().findByExample(instance);
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
//		} catch (RuntimeException re) {
//			log.error("find by example failed", re);
//			throw re;
//		}
//	}
//
//	public List findByProperty(String propertyName, Object value) {
//		log.debug("finding WmsWarIn instance with property: " + propertyName
//				+ ", value: " + value);
//		try {
//			String queryString = "from WmsWarIn as model where model."
//					+ propertyName + "= ?";
//			return getHibernateTemplate().find(queryString, value);
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}
	/**
	 * 
	 * ��ѯ������ⵥ <br>
	 * create_date: Aug 11, 2010,3:39:43 PM <br>
	 * @return List ������ⵥ�б�
	 */
	public List findAll() {
		log.debug("finding all WmsWarIn instances");
		try {
			String queryString = "from WmsWarIn";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

//	public WmsWarIn merge(WmsWarIn detachedInstance) {
//		log.debug("merging WmsWarIn instance");
//		try {
//			WmsWarIn result = (WmsWarIn) getHibernateTemplate().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(WmsWarIn instance) {
//		log.debug("attaching dirty WmsWarIn instance");
//		try {
//			getHibernateTemplate().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(WmsWarIn instance) {
//		log.debug("attaching clean WmsWarIn instance");
//		try {
//			getHibernateTemplate().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public static WmsWarInDAOImpl getFromApplicationContext(ApplicationContext ctx) {
//		return (WmsWarInDAOImpl) ctx.getBean("WmsWarInDAO");
//	}
	/**
	 * 
	 * ��ⵥ��ѯ�б�����(����ⵥ����,�ֿ���,���״̬,���״̬,��������) <br>
	 * create_date: Aug 11, 2010,2:34:09 PM <br>
	 * @param wwiTitle �������
	 * @param wmsCode �ֿ�����
	 * @param wwiAppIsok ���״̬
	 * @param wwiState ���״̬
	 * @param startTime ��������(��ʼ)
	 * @param endTime ��������(����)
	 * @return int ������ⵥ�б�����
	 */
	public int getWwiCount(String wwiTitle,String wmsCode,String wwiAppIsok,
			String wwiState,String startTime,String endTime){
		Query query;
		Session session;
		session = (Session) super.getSession();
	    String sql="";
		 //1.ֱ������ 2.���ֿ����ء�3.��״̬����ˣ����״̬������
		     if(wmsCode!=null&&!wmsCode.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
	    		 else
	    			 sql=" and wmsStro.wmsCode='"+wmsCode+"'";
	    	 }
	    	 if(wwiTitle!=null&&!wwiTitle.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and (wwiTitle like '%"+wwiTitle+"%' or wwiCode like '%"+wwiTitle+"%')";
	    		 else
	    			 sql=" and (wwiTitle like '%"+wwiTitle+"%' or wwiCode like '%"+wwiTitle+"%')";
	    	 }
	    	 if(wwiAppIsok!=null&&!wwiAppIsok.equals("")){
	    		 if(sql.length()>0)
	    			 if(wwiAppIsok.equals("2"))
	    				 sql+=" and (wwiAppIsok!='1' or wwiAppIsok is null) and wwiState='0'";
	    			 else
	    				 sql+=" and wwiAppIsok='1' and wwiState='0'";
	    		 else
	    			 if(wwiAppIsok.equals("2"))
	    				 sql=" and (wwiAppIsok!='1' or wwiAppIsok is null) and wwiState='0'";
	    			 else
	    				 sql=" and wwiAppIsok='1' and wwiState='0'"; 
	    	 }
	    	 if(wwiState!=null&&!wwiState.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wwiState ='"+wwiState+"'";
	    		 else
	    			 sql=" and wwiState ='"+wwiState+"'";
	    	 }
	    	 if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
		    		if(sql.length()>0)
		    			sql +=" and wwiInpTime <= '"+endTime+"'";
		    		else
		    			sql =" and wwiInpTime <= '"+endTime+"'";
				}
				else if(startTime!=null&&!startTime.equals("")){
					if(sql.length()>0)
		    			sql +=" and wwiInpTime between '"+startTime+"' and '"+endTime+"'";
		    		else
		    			sql =" and wwiInpTime between '"+startTime+"' and '"+endTime+"'";
				}
		    String queryString="select count(*) from WmsWarIn where wwiIsdel='1'"+sql;
			query = session.createQuery(queryString);
			int count = (Integer.parseInt(query.uniqueResult().toString()));
			return count;
	}
	/**
	 * 
	 * ��ⵥ��ѯ�б�(����ⵥ����,�ֿ���,���״̬,���״̬,��������) <br>
	 * create_date: Aug 11, 2010,2:39:30 PM <br>
	 * @param wwiTitle �������
	 * @param wmsCode �ֿ�����
	 * @param wwiAppIsok ���״̬
	 * @param wwiState ���״̬
	 * @param startTime ��������(��ʼ)
	 * @param endTime ��������(����)
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ������ⵥ�б�
	 */
	public List WwiSearch(String wwiTitle,String wmsCode,String wwiAppIsok,String wwiState
			,String startTime,String endTime,int currentPage,int pageSize){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="";
	     //1.ֱ������ 2.���ֿ����ء�3.��״̬����ˣ����״̬������
		     if(wmsCode!=null&&!wmsCode.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wmsStro.wmsCode='"+wmsCode+"'";
	    		 else
	    			 sql=" and wmsStro.wmsCode='"+wmsCode+"'";
	    	 }
	    	 if(wwiTitle!=null&&!wwiTitle.equals("")){
		    		 if(sql.length()>0)
		    			 sql+=" and (wwiTitle like '%"+wwiTitle+"%' or wwiCode like '%"+wwiTitle+"%')";
		    		 else
		    			 sql=" and (wwiTitle like '%"+wwiTitle+"%' or wwiCode like '%"+wwiTitle+"%')";
	    	 }
    	 	if(wwiAppIsok!=null&&!wwiAppIsok.equals("")){
	    		 if(sql.length()>0)
	    			 if(wwiAppIsok.equals("2"))
	    				 sql+=" and (wwiAppIsok!='1' or wwiAppIsok is null) and wwiState='0'";
	    			 else
	    				 sql+=" and wwiAppIsok='1' and wwiState='0'";
	    		 else
	    			 if(wwiAppIsok.equals("2"))
	    				 sql=" and (wwiAppIsok!='1' or wwiAppIsok is null) and wwiState='0'";
	    			 else
	    				 sql=" and wwiAppIsok='1' and wwiState='0'"; 
    	 	}
	    	 if(wwiState!=null&&!wwiState.equals("")){
	    		 if(sql.length()>0)
	    			 sql+=" and wwiState ='"+wwiState+"'";
	    		 else
	    			 sql=" and wwiState ='"+wwiState+"'";
	    	 }
	    	 if((startTime==null||startTime.equals(""))&&(endTime!=null&&!endTime.equals(""))){
		    		if(sql.length()>0)
		    			sql +=" and wwiInpTime <= '"+endTime+"'";
		    		else
		    			sql =" and wwiInpTime <= '"+endTime+"'";
				}
				else if(startTime!=null&&!startTime.equals("")){
					if(sql.length()>0)
		    			sql +=" and wwiInpTime between '"+startTime+"' and '"+endTime+"'";
		    		else
		    			sql =" and wwiInpTime between '"+startTime+"' and '"+endTime+"'";
			}
		    String queryString="from WmsWarIn where wwiIsdel='1'"+sql+"order by wwiId desc";
			query=session.createQuery(queryString);
			query.setFirstResult((currentPage-1)*pageSize);
			query.setMaxResults(pageSize);
			List list=(List)query.list();
			return list;
	}
	/**
	 * 
	 * ������ⵥId��ѯʵ�� <br>
	 * create_date: Aug 11, 2010,2:40:43 PM <br>
	 * @param wwiId ��ⵥId
	 * @return WmsWarIn ������ⵥʵ��
	 */
	public WmsWarIn findWwi(Long wwiId) {
		return (WmsWarIn)super.getHibernateTemplate().get(WmsWarIn.class,wwiId);
	}
	/**
	 * 
	 * ��ɾ��״̬�����ⵥʵ�� <br>
	 * create_date: Aug 11, 2010,3:34:45 PM <br>
	 * @param wwiId ��ⵥid
	 * @return WmsWarIn ������ⵥʵ��
	 */
	public WmsWarIn findWwi2(Long wwiId) {
		log.debug("finding all WmsWarIn instances");
		try {
			String queryString = "from WmsWarIn where wwiIsdel='1' and wwiId="+wwiId;
			List list=getHibernateTemplate().find(queryString);
			if(list.size()>0)
				return (WmsWarIn) list.get(0);
			else
				return null;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ������ⵥ�Ų�ѯ <br>
	 * create_date: Aug 11, 2010,3:39:43 PM <br>
	 * @param wwiCode ��ⵥ��
	 * @return List ������ⵥ�б�
	 */
	public List findAll(String wwiCode) {
		log.debug("finding all WmsWarIn instances");
		try {
			String queryString = "from WmsWarIn where wwiCode='"+wwiCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * ������ⵥ <br>
	 * create_date: Aug 11, 2010,2:48:41 PM <br>
	 * @param wmsWarIn ��ⵥʵ��
	 */
	public void updateWwi(WmsWarIn wmsWarIn) {
		super.getHibernateTemplate().update(wmsWarIn);
	}
	/**
	 * 
	 * ��ѯָ���ֿ����ⵥ <br>
	 * create_date: Aug 11, 2010,3:20:52 PM <br>
	 * @param wmsCode �ֿ���
	 * @return List ������ⵥ�б�
	 */
	public List WwiSearch(String wmsCode){
		Query query;
		Session session;
		session = (Session) super.getSession();
	    String sql ="select wwi from WmsWarIn as wwi where wwi.wwiIsdel='1' and wwi.wmsStro.wmsCode='"+wmsCode+"' order by wwi.wwiId desc";
	    query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ��ѯ����Ҫ��˵���ⵥ���� <br>
	 * create_date: Aug 11, 2010,3:27:19 PM <br>
	 * @param wmsCode �ֿ���
	 * @param isok ���״̬
	 * @return int ������ⵥ����
	 */
	public int findApp(String wmsCode,String isok) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			if(isok.equals("2"))
				sql=" and wwiState='0'  and (wwiAppIsok!='1' or wwiAppIsok is null) and wmsStro.wmsCode='"+wmsCode+"'";
			else
				sql=" and wwiState='0'  and wwiAppIsok ='1' and wmsStro.wmsCode='"+wmsCode+"'";	
		}else{
			if(isok.equals("2"))
				sql=" and (wwiAppIsok!='1' or wwiAppIsok is null)";
			else
				sql=" and wwiAppIsok='1'";
		}
		String queryString ="select count(*) from WmsWarIn where wwiIsdel='1' and wwiState='0'"+sql;
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * ��ѯ����Ҫ������ⵥ <br>
	 * create_date: Aug 11, 2010,3:28:36 PM <br>
	 * @param wmsCode �ֿ���
	 * @return int ������ⵥ����
	 */
	public int findInWms(String wmsCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String queryString ="";
		if(wmsCode!=null&&!wmsCode.equals("")){
			queryString="select count(*) from WmsWarIn where wwiIsdel='1' and wwiAppIsok='1' and wmsStro.wmsCode='"
					+wmsCode+"' and (wwiInpTime between'"+starTime1+"' and '"+endTime1+"' or wwiAltTime between'"+starTime1+"' and '"+endTime1+"')";
		}else{
			queryString="select count(*) from WmsWarIn where wwiIsdel='1' and wwiAppIsok='1' and (wwiInpTime between'"
					+starTime1+"' and '"+endTime1+"' or wwiAltTime between'"+starTime1+"' and '"+endTime1+"')";
		}
		query = session.createQuery(queryString);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 
	 * ��ô�ɾ����������ⵥ <br>
	 * create_date: Aug 11, 2010,3:31:27 PM <br>
	 * @param pageCurrentNo ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return List ������ⵥ�б�
	 */
	public List findDelWarIn(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsWarIn where  wwiIsdel='0' order by wwiId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * ��ô�ɾ����������ⵥ���� <br>
	 * create_date: Aug 11, 2010,3:34:10 PM <br>
	 * @return int ������ⵥ�б�����
	 */
	public int findDelWarInCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsWarIn where  wwiIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
}