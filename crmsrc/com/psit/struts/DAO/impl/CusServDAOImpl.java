package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusServDAO;
import com.psit.struts.entity.CusServ;
/**
 * �ͻ�����DAOʵ���� <br>
 */
public class CusServDAOImpl extends HibernateDaoSupport implements CusServDAO{
	public static final String SERV_ID = "serCode";
	public static final String SERV_ISDEL = "serIsDel";
	public static final String SERV_TITLE = "serTitle";
	public static final String SERV_STATE = "serState";
	public static final String SERV_METHOD = "serMethod";
	public static final String SERV_EXEDATE = "serExeDate";
	public static final String SERV_CUS = "cusCorCus";
	public static final String SERV_CUS_ID = "cusCorCus.corCode";
	public static final String SERV_CUS_NAME = "cusCorCus.corName";
	public static final String SERV_EMP_NAME = "salEmp.seName";
	public static final String SERV_EMP ="salEmp";
	public static final String SERV_USER_NUM = "limUser.userNum";
	public static final String SERV_CONTENT = "serContent";
	
	/**
	 * ����ͻ�������Ϣ <br>
	 * @param cusServ �ͻ�����ʵ��
	 */
	public void save(CusServ cusServ) {
		super.getHibernateTemplate().save(cusServ);
	}
	/**
	 * ���ݿͻ�����ID��ȡ�ͻ����� <br>
	 * @param serCode �ͻ�����id
	 * @return CusServ  ���ؿͻ�����ʵ�� 
	 */
	public CusServ showCusServ(Long serCode) {
		return (CusServ) super.getHibernateTemplate().get(CusServ.class, serCode);
	}
	/**
	 * ���¿ͻ�������Ϣ <br>
	 * @param cusServ �ͻ�����ʵ��
	 */
	public void update(CusServ cusServ) {
		super.getHibernateTemplate().update(cusServ);
	}
	 /**
	  * ɾ���ͻ����� <br>
	  * @param cusServ �ͻ�����ʵ��
	  */
	public void delServ(CusServ cusServ) {
		super.getHibernateTemplate().delete(cusServ);
	}
	 /**
	  * ��ô�ɾ�������пͻ����� <br>
	  * @param pageCurrentNo ��ǰҳ��
	  * @param pageSize ÿҳ����
	  * @return List ���ؿͻ������б� 
	  */
	public List findDelServ(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from CusServ where  serIsDel='0' order by serInsDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	 /**
	  * ��ô�ɾ�������пͻ��������� <br>
	  * @return int ���ؿͻ������б�����
	  */
	public int findDelServCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from CusServ where  serIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	
	/**
	 * ���ĳ�ͻ��µ����пͻ�����<br>
	 * @param cusCode �ͻ�Id
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage  ��ǰҳ��
	 * @param pageSize ÿҳ����
	 * @return list ���ؿͻ������б�
	 */
	public List getCusServ(Long cusCode,String orderItem, String isDe, int currentPage,int pageSize){
		Session session=super.getSession();
		String sql1= getCusServSql(cusCode,orderItem,isDe,false);
		Query query=session.createQuery(sql1);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	}
	/**
	 * ���ĳ�ͻ��µ����пͻ���������
	 */
	public int getCusServCount(Long cusCode){
		Session session=super.getSession();
		 String sql1=getCusServSql(cusCode,null,null,true);
		 Query query=session.createQuery(sql1);
		 int count=Integer.parseInt(query.uniqueResult().toString());
		 return count;
	}
	
	private String getCusServSql(Long cusCode, String orderItem, String isDe, boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String tab = "";
		String sql = "from CusServ ";
		if(isCount){
			sql = "select count(*) "+sql;
		}
		appendSql.append(" where "+tab+SERV_ISDEL+" = '1' and "+tab+SERV_CUS_ID+" = "+cusCode+" ");
		if(!isCount){
			if(orderItem!=null && !orderItem.equals("")){
				String [] items = {"serState","serTitle","cName","serMethod","serContent","seName","exeDate"};
				String [] cols = {SERV_STATE,SERV_TITLE,SERV_CUS_NAME,SERV_METHOD,SERV_CONTENT,SERV_EMP_NAME,SERV_EXEDATE};
				for(int i=0;i<items.length;i++){
					if(orderItem.equals(items[i])){
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append(" order by "+tab+orderItem+" ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else{
				appendSql.append(" order by "+tab+SERV_ID+" desc ");
			}
		}
		sql +=appendSql.toString();
		return sql;
	}
	 /**
	  * �ͷ��б�<br>
	  * @param args
	  *			    args[0]: �Ƿ�ɾ��
	  * 			args[1]: 0:������ 2���Ѵ���
	  * 			args[2]: ����
	  * 			args[3]: �ͷ�Id
	  * 			args[4]: ״̬
	  * 			args[5]: �ػ���ʽ
	  * 			args[6]: ��ʼִ������
	  * 			args[7]: ��ִֹ������
	 * @param orderItem �����ֶ�
	 * @param isDe �Ƿ�����
	 * @param currentPage ��ǰҳ
	 * @param pageSize ÿҳ����
	  * @return List<CusServ> 
	  */
	 public List<CusServ> getServ(String[]args, String orderItem,String isDe, int currentPage, int pageSize){
		 Session session = (Session)super.getSession();
		 Query query = getServSql(session, args, orderItem, isDe,false);
		 query.setFirstResult((currentPage - 1) * pageSize);
		 query.setMaxResults(pageSize);
		 List<CusServ> list = query.list();
		 return list;
	 }
	 public int getServCount(String [] args){
		 Session session = (Session) super.getSession();
		 Query query = getServSql(session,args,null,null,true);
		 int count = Integer.valueOf(query.uniqueResult().toString());
		 return count;
	 }
	 private Query getServSql(Session session, String [] args, String orderItem, String isDe
			 ,boolean isCount){
		 StringBuffer appendSql = new StringBuffer();
		 String tab = "cs.";//�����
		 String sql = "from CusServ cs ";
		 if(isCount){
			 sql ="select count(*) " + sql;
		 }
		 else {
			 sql ="select cs " + sql;
		 }
		 if(args!=null){
			 if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+SERV_ISDEL+" = '"+args[0]+"' ");
			 }
			 if(args.length>2){
				 if(args[1]!=null && !args[1].equals("")){
				    	if(args[1].equals("0")){
				    		appendSql.append(appendSql.length()==0?"where ":"and ");
				    		appendSql.append(tab+SERV_STATE +" = '������' ");
				    	}
				    	else {
				    		appendSql.append(appendSql.length()==0?"where ":"and ");
				    		appendSql.append(tab+SERV_STATE +" = '�Ѵ���' ");
				    	}
				 }
				 if(args[2]!=null && !args[2].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_EMP_NAME +" like :seName ");
				 }
				 if(args[3]!=null && !args[3].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_CUS_NAME +" like :corName "); 
				 }
				 if(args[4]!=null && !args[4].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_STATE +" = '"+args[4]+"' ");	 
				 }
				 if(args[5]!=null && !args[5].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_METHOD +" = '"+args[5]+"' "); 
				 }
			    if(args[6]!=null){
			    	if (!args[6].equals("") && args[7].equals("")) {
			    		appendSql.append(appendSql.length()==0?"where ":"and ");
			    		appendSql.append(tab+SERV_EXEDATE+" >= '"+ args[6] + "' ");
					} else if (!args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + SERV_EXEDATE + " between '"
								+ args[6] + "' and '"+args[7]+ "' ");
					} else if (args[6].equals("") && !args[7].equals("")) {
						appendSql.append(appendSql.length()==0?"where ":"and ");
						appendSql.append(tab + SERV_EXEDATE + " <= '"+ args[7]+ "' ");
					}
			    }
			    if(args[8]!=null && !args[8].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_TITLE +" like :serTitle "); 
			    }
			 }
			 else{
				 if(args[1]!=null && !args[1].equals("")){
		    		appendSql.append(appendSql.length()==0?"where ":"and ");
		    		appendSql.append(tab+SERV_CUS_ID+" = "+args[1]+" "); 
				 }
			 }
		 }
		 if(!isCount){
			if (orderItem != null && !orderItem.equals("")) {
				if(args.length>2){
					String[] items = { "title","customer","mothed","content","seName","exeDate"};
					String[] cols = { SERV_TITLE, SERV_CUS_NAME, SERV_METHOD, SERV_CONTENT ,SERV_EMP_NAME, SERV_EXEDATE};
					for (int i = 0; i < items.length; i++) {
						if (orderItem.equals(items[i])) {
							switch(i){
							case 4:
								sql+=" left join "+tab+SERV_EMP +" ";
								break;
							}
							orderItem = cols[i];
							break;
						}
					}
				}
				else{
					String[] items1 = { "state","title","customer","mothed","content","seName","exeDate"};
					String[] cols1 = { SERV_STATE,SERV_TITLE, SERV_CUS_NAME, SERV_METHOD, SERV_CONTENT ,SERV_EMP_NAME, SERV_EXEDATE};
					for (int i = 0; i < items1.length; i++) {
						if (orderItem.equals(items1[i])) {
							switch(i){
							case 5:
								sql+=" left join "+tab+SERV_EMP +" ";
								break;
							}
							orderItem = cols1[i];
							break;
						}
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//Ĭ������
				appendSql.append("order by "+tab+SERV_ID+" desc ");
			}
		 }
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args.length>2){
				if(args[2]!=null && !args[2].equals("")){
					query.setString("seName", "%"+args[2]+"%");
				}
				if(args[3]!=null && !args[3].equals("")){
					query.setString("corName", "%"+args[3]+"%");
				}
				if(args[8]!=null && !args[8].equals("")){
					query.setString("serTitle", "%"+args[8]+"%");
				}
			}
		}
		return query;
	 }
}