package com.training.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.training.bean.Product;
import com.training.exception.ArtistIdException;
import com.training.exception.OrgIdException;
import com.training.exception.ProductException;
import com.training.exception.ReleaseDateException;
import com.training.exception.UpcException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {

	private long count=0;
	@Autowired
	private JmsTemplate jmsTemplate;

	private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);
	public static String TimeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
	//public static String newTimeStamp = verifyInput(TimeStamp);
	

	private static final java.text.SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("yyyyMMdd");

	public static String verifyInput(String input) {
		if (input != null) {
			try {
				java.util.Date ret = sdf.parse(input.trim());
				// System.out.println(input.trim());
				if (sdf.format(ret).equals(input.trim())) {
					return input;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void isEmpty(Product product)
	{
		final String upc=product.getUpc();
		final String artistId=product.getArtistId();
		final String orgId=product.getOrgId();
		final String releaseDate=product.getReleaseDate();

		/* LinkedHashMap<String,String> hm = new LinkedHashMap<>(); */

		try{
			if(upc.isEmpty());
				/*if(artistId.isEmpty())
					hm.put("Exception 2:","artistId is empty");
				else if(orgId.isEmpty())
					hm.put("Exception 3:","orgId is empty");
				else if(releaseDate.isEmpty()) {
					hm.put("newReleaseDate",TimeStamp);
					// hm.put("Exception 4:", "releaseDate is empty");
				}
				throw new UpcException("Upc not found");*/
			}catch(UpcException e) {
				System.out.println("Upc not found");
			}
		try{
			if(artistId.isEmpty());
				/*if(artistId.isEmpty())
					hm.put("Exception 2:","artistId is empty");
				else if(orgId.isEmpty())
					hm.put("Exception 3:","orgId is empty");
				else if(releaseDate.isEmpty()) {
					hm.put("newReleaseDate",TimeStamp);
					// hm.put("Exception 4:", "releaseDate is empty");
				}
				throw new UpcException("Upc not found");*/
			}catch(ArtistIdException e) {
				System.out.println("ArtistId not found");
			}
		try{
			if(orgId.isEmpty());
				/*if(artistId.isEmpty())
					hm.put("Exception 2:","artistId is empty");
				else if(orgId.isEmpty())
					hm.put("Exception 3:","orgId is empty");
				else if(releaseDate.isEmpty()) {
					hm.put("newReleaseDate",TimeStamp);
					// hm.put("Exception 4:", "releaseDate is empty");
				}
				throw new UpcException("Upc not found");*/
			}catch(OrgIdException e) {
				System.out.println("OrgId not found");
			}
		try{
			verifyInput(releaseDate);
				/*if(artistId.isEmpty())
					hm.put("Exception 2:","artistId is empty");
				else if(orgId.isEmpty())
					hm.put("Exception 3:","orgId is empty");
				else if(releaseDate.isEmpty()) {
					hm.put("newReleaseDate",TimeStamp);
					// hm.put("Exception 4:", "releaseDate is empty");
				}
				throw new UpcException("Upc not found");*/
			}catch(ReleaseDateException e) {
				System.out.println("ReleaseDate is not valid");
			}
		
		
			
			/*
			 * //hm.put(throw new UpcException("Upc not found")); if(artistId.isEmpty())
			 * hm.put("Exception 2:","artistId is empty"); else if(orgId.isEmpty())
			 * hm.put("Exception 3:","orgId is empty"); else if(releaseDate.isEmpty()) {
			 * hm.put("newReleaseDate",TimeStamp); // hm.put("Exception 4:",
			 * "releaseDate is empty"); } //return hm;
			 * 
			 * 
			 * if(artistId.isEmpty()) { hm.put("Exception 1:","artistId is empty");
			 * if(orgId.isEmpty()) hm.put("Exception 2:","orgId is empty"); else
			 * if(releaseDate.isEmpty()) { hm.put("newReleaseDate",TimeStamp); //
			 * hm.put("Exception 3:", "releaseDate is empty"); } return hm; } else
			 * if(orgId.isEmpty()) {
			 * 
			 * hm.put("Exception 1:", "orgId is empty"); if(releaseDate.isEmpty()) {
			 * hm.put("newReleaseDate",TimeStamp); // hm.put("Exception 2:",
			 * "releaseDate is empty"); } return hm; } else if(releaseDate.isEmpty()) {
			 * hm.put("newReleaseDate",TimeStamp); // hm.put("Exception 1:",
			 * "releaseDate is empty"); return hm; }else return hm;
			 */
	}

	@Override
	public Product process(final Product product) throws Exception {
		log.info("Started processing the csv file..");
		//final long uniqueNo=product.getUniqueNo();
		final String upc=product.getUpc();
		final String productDesc=product.getProductDesc();
		final String artistId=product.getArtistId();
		final String orgId=product.getOrgId();
		final String configId=product.getConfigId();
		 String releaseDate=product.getReleaseDate();

		if(upc!="" && artistId!="" && orgId!="")
		{
			if(releaseDate.isEmpty()) {
				releaseDate=TimeStamp;
			}
			// String newReleaseDate = verifyInput(releaseDate);
			final Product transformedProduct = new Product(upc,productDesc,artistId,orgId,configId,releaseDate);
			//modify date or validate date

			log.info("Converting (" + product + ") into (" + transformedProduct + ")");
			++count;
			//System.out.println(ProductItemProcessor.isEmpty(product));
			jmsTemplate.convertAndSend("productQueue",transformedProduct);
			return transformedProduct;
		}
		else
		{
			ProductItemProcessor.isEmpty(product);
			++count;
			System.out.println(" failed id :"+count);
			jmsTemplate.convertAndSend("failedQueue",new Product(upc,productDesc,artistId,orgId,configId,releaseDate));
		}
		return null;

	}
}