package com.training.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@ToString
//@Entity
@Table(name="Product")
public class Product implements Serializable {
	//@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private long uniqueNo;
	private String upc;
	private String productDesc;
	private String artistId;
	private String orgId;
	private String configId;
	private String releaseDate;
	
	
	public Product(String upc, String productDesc, String artistId, String orgId, String configId, String releaseDate) {
		super();
		this.upc = upc;
		this.productDesc = productDesc;
		this.artistId = artistId;
		this.orgId = orgId;
		this.configId = configId;
		this.releaseDate = releaseDate;
	}


	
	
	
	
}

  