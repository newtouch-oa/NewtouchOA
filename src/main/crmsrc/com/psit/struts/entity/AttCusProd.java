package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 来往记录附件 <br>
 *
 * create_date: Aug 27, 2010,3:13:41 PM<br>
 * @author zjr
 */
public class AttCusProd extends Attachment implements java.io.Serializable {

    // Fields    
     private CusProd cusProd;

    // Constructors

    public CusProd getCusProd() {
		return cusProd;
	}

	public void setCusProd(CusProd cusProd) {
		this.cusProd = cusProd;
	}

	/** default constructor */
    public AttCusProd() {
    }
    
    /** full constructor */
    public AttCusProd(CusProd cusProd) {
        this.cusProd = cusProd;
    }

    // Property accessors



}