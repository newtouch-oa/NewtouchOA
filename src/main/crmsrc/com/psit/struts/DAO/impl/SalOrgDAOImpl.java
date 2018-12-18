package com.psit.struts.DAO.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalOrgDAO;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrg;
/**
 * ��˾�������ݿ�����ʵ���� <br>
 */
public class SalOrgDAOImpl extends HibernateDaoSupport implements SalOrgDAO {

	private static final Log log = LogFactory.getLog(SalOrgDAOImpl.class);
	private static final String ORGTOPCODE = "O20100000-1";//���Ŷ�����
	
	protected void initDao() {
		// do nothing
	}

	/**
	 * ��������<br>
	 * @param transientInstance ��˾���Ŷ���<br>
	 */
	public void save(SalOrg transientInstance) {
		log.debug("saving SalOrg instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * ɾ������<br>
	 * @param persistentInstance ��˾���Ŷ���<br>
	 */
	public void delete(SalOrg persistentInstance) {
		log.debug("deleting SalOrg instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/**
	 * ��ò������Լ��������¼���Ա<br>
	 * @param userNum �û�id��������
	 * @return List<SalOrg> ��˾���Ŷ����б�<br>
	 */
	public List<SalOrg>findAll(String userNum){
		Query query;
		Session session;
		session = (Session) super.getSession();
	    List<SalOrg> list=null;
	    List<SalOrg> orgList=new ArrayList<SalOrg>();
		try {
			String sql = "from SalOrg where soIsenabled='1' and soCode!='"+ORGTOPCODE+"' order by soCode ";
			query=session.createQuery(sql);
			list=query.list();
			Query que;
			 Iterator<SalOrg> iter=list.iterator();
				
			   while(iter.hasNext()){
			     SalOrg salOrg=(SalOrg)iter.next();
			     if(salOrg.getLimUsers()!=null)
			     {   
//				    	 que=session.createFilter(salOrg.getLimUsers(), "where userNum like '%"+userNum+"' order by this.userCode");
			    	 //Set set=salOrg.getLimUsers();
			    	 Set set = salOrg.getSalEmps();
			    	 Iterator it1=set.iterator();
			    	 //List<LimUser> userList = new ArrayList();
			    	 List<SalEmp> salEmpList = new ArrayList();
			    	 while(it1.hasNext()){
			    		 //LimUser user = (LimUser)it1.next();
			    		 //if(userNum!=null&&user.getUserNum().endsWith(userNum)){
			    			 //userList.add(user);
			    		 //}
			    		 SalEmp salEmp = (SalEmp) it1.next();
			    		 if(userNum != null && salEmp.getSeNo().equals(userNum)){
			    			 salEmpList.add(salEmp);
			    		 }
			    	 }
			    	 
//			    	 List<LimUser> userList=que.list();
//					     if(userList!=null&&userList.size()>0)
			    	 //if(userList!=null&&userList.size()>0)
					     //{
					    	 //salOrg.setLimUsers(new LinkedHashSet(userList));
					    	 //orgList.add(salOrg);
					     //}
			    	 if(salEmpList != null && salEmpList.size()>0){
			    		 salOrg.setSalEmps(new LinkedHashSet(salEmpList));
			    		 orgList.add(salOrg);
			    	 }
			     }
			    }
			   
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}finally{
			
		}
		return orgList;
	}
	/**
	 * ������в���<br>
	 * @return ��˾���Ŷ���
	 */
	public List<SalOrg> findAllSalOrg() {
		log.debug("finding all SalOrg instances");
		try {
			String queryString = "from SalOrg where soIsenabled='1'  order by soCode";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * ��ó��Լ��Ͷ���������������в���<br>
	 * @param soCode1 ��˾����id 
	 * @return List<SalOrg> ��˾���Ŷ����б�<br>
	 */
	public List<SalOrg> findPartSalOrg(String soCode1) {
		log.debug("finding part SalOrg instances");
		try {
			String queryString = "from SalOrg where soIsenabled='1' and soCode!= '"+ORGTOPCODE+"' and soCode!='"+soCode1+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find part failed", re);
			throw re;
		}
	}
	/**
	 * (��һ�仰�����������)<br>
	 * @param soCode ��˾����id
	 * @return List<SalOrg> ��˾���Ŷ����б�<br>
	 */
	public List<SalOrg> getLowSalOrg(String soCode) {
		Session session = (Session) super.getSession();
		String sql="from SalOrg where salOrg.soCode=:oCode";
		Query query=session.createQuery(sql).setString("oCode", soCode);
		List<SalOrg> list=query.list();
		return list;
	}
	/**
	 * �鿴��������<br>
	 * @param soCode ��˾����id
	 * @return SalOrg ��˾���Ŷ���<br>
	 */
	public SalOrg salOrgDesc(String soCode){
		return (SalOrg)super.getHibernateTemplate().get(SalOrg.class, soCode);
	}
	/**
	 * �޸Ĳ���<br>
	 * @param salOrg ��˾���Ŷ���<br>
	 */
	public void updateSalOrg(SalOrg salOrg){		
		super.getHibernateTemplate().update(salOrg);
	}
	/**
	 * �жϲ��ű���Ƿ��Ѵ���<br>
	 * @param soCode ��˾���ű��
	 * @return List<SalOrg> ��˾���Ŷ����б�<br>
	 */
	public List<SalOrg> searchBySoCode(String soCode) {
		Session session = (Session) super.getSession();
		String sql="select soOrgCode from SalOrg where soOrgCode=:oCode";
		Query query=session.createQuery(sql).setString("oCode", soCode);
		List list=(List)query.list();
		return list;
	}
	/**
	 * �жϲ������Ƿ��Ѵ���<br>
	 * @param soName ��˾��������
	 * @return List<SalOrg> ��˾���Ŷ����б�<br>
	 */
	public List<SalOrg> searchBySoName(String soName) {
		Session session = (Session) super.getSession();
		String sql="select soName from SalOrg where soName=:oName";
		Query query=session.createQuery(sql).setString("oName", soName);
		List list=(List)query.list();
		return list;
	}
	/**
	 * ������в��ų�ȥ��������<br>
	 * @return ��˾���ż�¼�б�
	 */
	public List<SalOrg> findAll() {
		log.debug("finding all SalOrg instances");
		try {
			String queryString = "from SalOrg where soIsenabled='1' and soCode!='"+ORGTOPCODE+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

}