package com.count.time.configuration.batch;

import com.count.time.model.batch.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;
import java.net.MalformedURLException;

@Configuration
@Slf4j
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Value("${file.input}")
    private String fileInput;

    @Value("file:result-output.xml")
    private Resource outputXml;

    @Bean
    @SuppressWarnings("unchecked")
    public FlatFileItemReader<Coffee> reader() {
        return new FlatFileItemReaderBuilder<Coffee>().name("coffeeItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names(new String[] { "brand", "origin", "characteristics" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(Coffee.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Coffee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Coffee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1, Step xmlStep) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .next(xmlStep)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Coffee> writer) {
        return stepBuilderFactory.get("step1")
                .<Coffee, Coffee> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Coffee> forXmlReader()
            throws UnexpectedInputException, ParseException {
        FlatFileItemReader<Coffee> reader = new FlatFileItemReader<Coffee>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] tokens = { "brand", "origin", "characteristics" };
        tokenizer.setNames(tokens);
        reader.setResource(new ClassPathResource(fileInput));
        DefaultLineMapper<Coffee> lineMapper =
                new DefaultLineMapper<Coffee>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemWriter<Coffee> forXmlWriter(Marshaller marshaller)
            throws MalformedURLException {
        StaxEventItemWriter<Coffee> itemWriter =
                new StaxEventItemWriter<Coffee>();
        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("coffeeRecord");
        itemWriter.setResource(outputXml);
        return itemWriter;
    }

    @Bean
    public Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[] { Coffee.class });
        return marshaller;
    }

    @Bean
    @Qualifier(value = "xmlStep")
    protected Step xmlStep(@Qualifier("forXmlReader") ItemReader<Coffee> reader,
                           ItemProcessor<Coffee, Coffee> processor,
                           @Qualifier("forXmlWriter") ItemWriter<Coffee> writer) {
        return stepBuilderFactory.get("xmlStep").<Coffee, Coffee> chunk(10)
                .reader(reader).processor(processor).writer(writer).build();
    }

//    @Bean(name = "toXmlBatchJob")
//    public Job job(@Qualifier("xmlStep") Step xmlStep) {
//        return jobBuilderFactory.get("toXmlBatchJob").start(xmlStep).build();
//    }

    @Bean
    public CoffeeItemProcessor processor() {
        return new CoffeeItemProcessor();
    }
}