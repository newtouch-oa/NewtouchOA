package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.LimUserDAO;
import com.psit.struts.entity.LimUser;

/**
 * �˺�DAOʵ���� <br>
 */

public class LimUserDAOImpl extends HibernateDaoSupport implements LimUserDAO{
	private static final Log log = LogFactory.getLog(LimUserDAOImpl.class);
	// property constants
	public static final String USER_CODE = "userCode";
	public static final String USER_NUM = "userNum";
	public static final String USER_LOGIN_NAME = "userLoginName";
	public static final String USER_PWD = "userPwd";
	public static final String USER_LEV = "userLev";
	public static final String USER_SE_NAME = "userSeName";
	public static final String USER_DESC = "userDesc";
	public static final String USER_ISENABLED = "userIsenabled";
	public static final String USER_ISLOGIN = "userIsLogin";
	public static final String USER_ORG_ID = "salOrg.soCode";
	public static final String USER_EMP_ID = "salEmp.seNo";
	public static final String USER_ROL_ID = "limRole.rolId";
	public static final String USER_ROL_LEV = "limRole.rolLev";
	public static final String USER_USER_CODE = "limUser.userCode";

	protected void initDao() {
		// do nothing
	}
	/**
	 * �����˺� <br>
	 * @param transientInstance �˺�ʵ��
	 */
	public void save(LimUser transientInstance) {
		log.debug("saving LimUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * �����˺� <br>
	 * @param limUser �˺�ʵ��
	 */
	public void updateUser(LimUser limUser){
		super.getHibernateTemplate().update(limUser);
	}
	/**
	 * �����˺�ID��ѯ�˺�ʵ�� <br>
	 * @param id �˺�
	 * @return LimUser �˺�ʵ��
	 */
	public LimUser findById(java.lang.String id) {
		log.debug("getting LimUser instance with id: " + id);
		try {
			LimUser instance = (LimUser) getHibernateTemplate().get(
					"com.psit.struts.entity.LimUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	/**
	 * �����û���Ų�ѯ�û�ʵ��(��״̬) <br>
	 * @param userCode �˺�
	 * @return LimUser �����û�ʵ�� 
	 */
	public LimUser getUser(String userCode){
		Session session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED
						+ "='1' or " + USER_ISENABLED + "='2') and " + USER_CODE
						+ "=:userCode";
		Query query = session.createQuery(sql)
				.setString("userCode", userCode);	
		return (LimUser)query.uniqueResult();
	}
	/**
	 * ��½��֤ <br>
	 * @param loginName ��¼��
	 * @return List �˺��б�
	 */
	public LimUser getUserByName(String loginName) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where " + USER_LOGIN_NAME
				+ "=:loginName and (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='3')";
		Query query = session.createQuery(sql)
				.setString("loginName", loginName);	
		return (LimUser)query.uniqueResult();
	}
	/**
	 * �õ����������õ��˺�
	 * @return List �����������˺��б�
	 */
	public List<LimUser> listValidUser() {
		log.debug("finding all LimUser instances");
		try {
			String queryString = "from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2')";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * �����ű�Ų�ѯ�˺� <br>
	 * @param soCode ���ű��
	 * @return List �����˺��б�
	 */
	public List<LimUser> listByOrgId(String soCode){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_ORG_ID+"='"+soCode+"'";
		Query query=session.createQuery(sql);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * ��Ա��Id��ѯ�˺� <br>
	 * @param seNo Ա��Id
	 * @return List �����˺��б� 
	 */
	public List<LimUser> listByEmpId(String seNo){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_EMP_ID+"="+seNo;
		Query query=session.createQuery(sql);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * �˺�������ѯ <br>
	 * @return int �����˺�����
	 */
	public int getCountAllUse(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where "+USER_ISENABLED+"!='3' or "+USER_ISENABLED+" is null";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * ��ѯδʹ���˺����� <br>
	 * @return int �����˺����� 
	 */
	public int getCountNotUse(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where "+USER_ISENABLED+" is null";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * �������˺�������ѯ <br>
	 * @return int ����ʹ���˺�����
	 */
	public int getCountUser(){
		Session session = (Session) super.getSession();
		String sql ="select count(*) from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2')";
		Query query = session.createQuery(sql);
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * ��ѯ���������õ��ϼ��˺�(״̬Ϊ0��2) <br>
	 * @param userLev ְ��
	 * @return List<LimUser> �����ϼ��˺��б�
	 */
	public List<LimUser> getUpRole(int userLev) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where ("+USER_ISENABLED+"='1' or "+USER_ISENABLED+"='2') and "+USER_ROL_LEV+" < "
				+ userLev + " order by "+USER_ROL_LEV;
		Query query = session.createQuery(sql);
		List<LimUser> list = query.list();
		return list;
	}
	
	/**
	 * �����˺��Ƿ����¼��˺�<br>
	 * @param userCode �˺�
	 * @return List �����˺��б�
	 */
	public List<LimUser> checkDownCode(String userCode){
		Session session = (Session) super.getSession();
		String sql ="from LimUser where "+USER_USER_CODE+" = :userCode";
		Query query=session.createQuery(sql).setString("userCode", userCode);
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * ���ְ���Ƿ�ʹ�� <br>
	 * @param rolId ְ��id
	 * @return List ����ְ���б�
	 */
	public List<LimUser> checkRole(String rolId) {
		Session session = (Session) super.getSession();
		String sql1 = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_ROL_ID + "= " + rolId;
		Query query = session.createQuery(sql1);
		List<LimUser> list = query.list();
		return list;
	}
	/**
	 * ��ѯ����ְ�����˺���Ϣ <br>
	 * @param userCode �˺�
	 * @return List �����˺��б�
	 */
	public List<LimUser> findByBossLev(String userCode){
		Session session = (Session) super.getSession();
		String sql =" from LimUser where "+USER_ROL_LEV+"=1 and "+USER_CODE+" =:userCode";
		Query query=session.createQuery(sql).setString("userCode", userCode);	
		List<LimUser> list=query.list();
		return list;
	}
	/**
	 * ��������¼��˺� <br>
	 * @param userNum �û���
	 * @param userCode �˺�
	 * @return List �����û��б� 
	 */
	public List<LimUser> getAllLowerUser(String userNum, String userCode) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM
				+ " like :userNum and " + USER_CODE + "!=:userCode";
		query = session.createQuery(sql)
				.setString("userNum", "%" + userNum)
				.setString("userCode", userCode);
		List<LimUser> list = query.list();
		return list;
	}
	 /**
	  * �жϵ�¼���Ƿ��ظ� <br>
	  * @param loginName ��¼��
	  * @return List �����˺��б�
	  */
	public List getLoginName(String loginName) {
		Session session = (Session) super.getSession();
		String sql ="select userCode from LimUser where "+USER_LOGIN_NAME+" = :loginName";
		Query query=session.createQuery(sql).setString("loginName",loginName);
		List list=query.list();
		return list;
	}
	/**
	 * �ָ��û��ĵ�½״̬�����IP <br>
	 */
	public void recInit() {
		Session session=(Session) super.getSession();
		String sql="update LimUser set "+USER_ISLOGIN+"='0'";
		session.createQuery(sql).executeUpdate();
	}
	/**
	 * �����û����ѯ�˺� <br>
	 * @param userNum �û���
	 * @return List �����˺��б�
	 */
	public List<LimUser> findByUserNum(String userNum) {
		Session session = (Session) super.getSession();
		String sql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM
				+ " like :userNum order by " + USER_ROL_LEV + " asc";
		Query query = session.createQuery(sql)
			.setString("userNum","%" + userNum);
		List<LimUser> list = query.list();
		return list;
	}
	/**
	 * ��ѯ�����˺� <br>
	 * @return String �����˺�
	 */
	public String getMaxCode(){
		Session session = (Session) super.getSession();
	    String sql1 ="select max("+USER_CODE+")from LimUser";
	    Query query=session.createQuery(sql1);
		List list=query.list();
		String code="";
		if(list!=null){
			code=list.get(0).toString();
		}
		return code;
	}
	/**
	 * ��ȡδ���õ�userCode��Сֵ <br>
	 * @return String �����˺�
	 */
	public String getMinCode(){
		Session session = (Session) super.getSession();
	    Query query=session.createQuery("select min("+USER_CODE+") from LimUser where " + USER_ISENABLED + " is null ");
		List list=query.list();
		String code="";
		if(list.get(0)!=null){
			code=list.get(0).toString();
		}
		return code;
	}
	
	/**
	 * ��ѯ������������˺ż����¼��˺ŵ��˺��б� <br>
	 * @param userNum ���ų����˺��û���
	 * @return List �˺��б�
	 */
	public List<LimUser> getUserWithOut(String userNum){
		Session session = (Session) super.getSession();
		String hql = "from LimUser where (" + USER_ISENABLED + "='1' or "
				+ USER_ISENABLED + "='2') and " + USER_NUM + " not like :userNum ";
		Query query = session.createQuery(hql).setString("userNum", "%"+userNum);
		List<LimUser> list = query.list();
		return list;
	}
}