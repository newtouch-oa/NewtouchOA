package com.psit.struts.DAO.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.AttachmentDAO;
import com.psit.struts.entity.Attachment;

/**
 * ����DAOʵ���� <br>
 */

public class AttachmentDAOImpl extends HibernateDaoSupport implements AttachmentDAO {
	private static final Log log = LogFactory.getLog(AttachmentDAOImpl.class);
	// property constants
	public static final String ATT_NAME = "attName";
	public static final String ATT_SIZE = "attSize";
	public static final String ATT_PATH = "attPath";
	public static final String ATT_IS_JUNK = "attIsJunk";
	/**
	 * ���渽��  <br>
	 * @param transientInstance ����ʵ��
	 */
	public void save(Attachment transientInstance) {
		log.debug("saving Attachment instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * ɾ������ <br>
	 * @param persistentInstance ����ʵ��
	 */
	public void delete(Attachment persistentInstance) {
		log.debug("deleting Attachment instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	/**
	 * ����id��ѯ���� <br>
	 * @param id ����id
	 * @return Attachment ���ظ���ʵ��
	 */
	public Attachment findById(java.lang.Long id) {
		log.debug("getting Attachment instance with id: " + id);
		try {
			Attachment instance = (Attachment) getHibernateTemplate().get(
					"com.psit.struts.entity.Attachment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}