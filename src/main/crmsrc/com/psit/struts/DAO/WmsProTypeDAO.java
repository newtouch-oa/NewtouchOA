package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.WmsProType;
/**
 * 
 * ��Ʒ���DAO <br>
 *
 * create_date: Aug 19, 2010,11:36:32 AM<br>
 * @author zjr
 */
public interface WmsProTypeDAO {
	/**
	 * 
	 * ��ѯ������ʹ�õ���Ʒ��� <br>
	 * create_date: Aug 11, 2010,2:22:52 PM <br>
	 * @return List ����б�
	 */
	public List findAllWptType();
	/**
	 * 
	 * ��ѯ������ʹ�õ���Ʒ��� <br>
	 * create_date: Aug 11, 2010,2:22:52 PM <br>
	 * @return List ����б�
	 */
	public List<WmsProType> findAll();
	/**
	 * 
	 * �����Ʒ��� <br>
	 * create_date: Aug 12, 2010,9:40:34 AM <br>
	 * @param wmsProType ��Ʒ���ʵ��
	 */
	public void save(WmsProType wmsProType);
	/**
	 * 
	 * ������Ʒ���Id��ѯ���ʵ�� <br>
	 * create_date: Aug 12, 2010,11:27:56 AM <br>
	 * @param id ��Ʒ���Id
	 * @return WmsProType ������Ʒ���ʵ��
	 */
	public WmsProType findById(java.lang.Long id);
	/**
	 * 
	 * ������Ʒ��� <br>
	 * create_date: Aug 12, 2010,11:29:59 AM <br>
	 * @param instance ��Ʒ���ʵ��
	 */
	public void updateProType(WmsProType instance);
	/**
	 * 
	 * ɾ����Ʒ��� <br>
	 * create_date: Aug 12, 2010,11:30:18 AM <br>
	 * @param instance ��Ʒ���ʵ��
	 */
	public void delete(WmsProType instance);
	/**
	 * 
	 * ��ѯ��Ʒ�����¼���� <br>
	 * create_date: Aug 12, 2010,11:30:37 AM <br>
	 * @param wptId ��Ʒ���id
	 * @return List ������Ʒ����б�
	 */
	public List findByUpId(Long wptId);
	
	/**
	 * ���ݿͻ�Id��ѯ��Ʒ���<br>
	 * @param cusId �ͻ�Id
	 * @return List<WmsProType> ��Ʒ���<br>
	 */
	public List<WmsProType> findByCusId(String cusId);
}