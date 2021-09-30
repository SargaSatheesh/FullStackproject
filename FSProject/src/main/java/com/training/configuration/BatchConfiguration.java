package com.training.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.training.batch.JobCompletionNotificationListener;
import com.training.batch.ProductItemProcessor;
import com.training.bean.Product;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private Logger log=LoggerFactory.getLogger(BatchConfiguration.class);
  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  public FlatFileItemReader<Product> reader() {
	  log.info("Reading the csv file..");
    return new FlatFileItemReaderBuilder<Product>()
      .name("personItemReader")
      .resource(new ClassPathResource("extrenalpro.csv"))
      .delimited()
      .names(new String[]{"upc","productDesc","artistId","orgId","configId","releaseDate"})
      .fieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
        setTargetType(Product.class);
      }})
      .build();
  }

  @Bean
  public ProductItemProcessor processor() {
    return new ProductItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Product> writer(DataSource dataSource) {
	  log.info("writing data into db..");
    return new JdbcBatchItemWriterBuilder<Product>()
      .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
      .sql("INSERT INTO Product (upc,product_desc,artist_id,org_id,config_id,release_date) VALUES (:upc, :productDesc, :artistId, :orgId, :configId, :releaseDate)")
      .dataSource(dataSource)
      .build();
  }

  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
	  log.info("here");
    return jobBuilderFactory.get("importUserJob")
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .flow(step1)
      .end()
      .build();
  }

  @Bean
  public Step step1(JdbcBatchItemWriter<Product> writer) {
	  
	  log.info("Step Builder");
    return stepBuilderFactory.get("step1")
      .<Product, Product> chunk(5)
      .reader(reader())
      .processor(processor())
      .writer(writer)
      .build();
  }
}