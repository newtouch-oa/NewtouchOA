package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.psit.struts.DAO.RUserRigDAO;
import com.psit.struts.entity.LimRight;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RUserRig;
/**
 * �û�Ȩ��DAOʵ���� <br>
 */
public class RUserRigDAOImpl extends HibernateDaoSupport implements RUserRigDAO {
	/**
	 * �����˺Ż�ȡ���˺ŵ�����Ȩ�� <br>
	 * @param userCode �˺�
	 * @return List ����Ȩ���б�
	 */
	public List getRUserRig(String userCode) {
		return super.getHibernateTemplate().find("from RUserRig where limUser.userCode='"+userCode+"'");
	}
	/**
	 * Ϊĳ�˺����Ȩ�� <br>
	 * @param userLims Ȩ������
	 * @param userCode �˺�
	 * @param rurType ��������
	 */
	public void addRUserRig(String[] userLims,String userCode,String rurType) {
		   Session session;
		   session = (Session) super.getSession();
		   if(userLims!=null)
		    {  
				 for(int i=0;i<userLims.length;i++)
				   {
					   RUserRig ruserRig=new RUserRig();
					   ruserRig.setLimRight(new LimRight(userLims[i]));
					   ruserRig.setLimUser(new LimUser(userCode));
					   ruserRig.setRurType(rurType);
					   session.save(ruserRig);
					   if(userLims.length>0&&userLims.length<20)
					   {
						   session.flush();
						   session.clear();
					   }
					   else if(i%20==0)
					   {
						   session.flush();
						   session.clear();
					   }
				   }
		    }
	}
	/**
	 * ɾ���ض��˺ŵ�Ȩ�� <br>
	 * create_date: Aug 11, 2010,11:56:58 AM <br>
	 * @param userCode �˺�
	 * @param funType ��������
	 */
	public void delRUserRig(String userCode,String funType) {
		Session session=(Session) super.getSession();
		String sql="delete from RUserRig where  rurType=:funType and limUser.userCode='"+userCode+"'";
		Query query=session.createQuery(sql).setString("funType", funType);
		query.executeUpdate();
	}
	/**
	 * �����˺Ż�ȡ���˺���ĳ�����������µ�����Ȩ�� <br>
	 * @param userCode �˺�
	 * @param rurType ��������
	 * @return List ����Ȩ���б�
	 */
	public List getRUserRig(String userCode, String rurType) {
		Session session=(Session) super.getSession();
		String sql="from RUserRig where limUser.userCode='"+userCode+"' and rurType=:rType";
		Query query=session.createQuery(sql).setString("rType", rurType);
		return query.list();
	}
}