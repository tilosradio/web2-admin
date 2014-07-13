package hu.tilos.radio.backend.converters;

import hu.radio.tilos.model.Mix;
import hu.tilos.radio.backend.SearchControllerTest;
import hu.tilos.radio.backend.data.EntitySelector;
import hu.tilos.radio.backend.data.MixRequest;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.DozerBuilder;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOption;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class ChildEntityFieldConverterTest {

    private static EntityManagerFactory factory;

    @BeforeClass
    public static void setUp() throws Exception {
        factory = Persistence.createEntityManagerFactory("tilos-test");

        JdbcDatabaseTester tester = new JdbcDatabaseTester("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/tilos_test", "root", "");
        tester.setDataSet(new FlatXmlDataSet(SearchControllerTest.class.getResourceAsStream("baseData.xml")));
        tester.onSetup();
    }

    @Test
    public void dozerTest() {
        //given
        BeanMappingBuilder updateBuilder = new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(MixRequest.class, Mix.class).fields("show", "show", new FieldsMappingOption() {
                    @Override
                    public void apply(DozerBuilder.FieldMappingBuilder fieldMappingBuilder) {
                        fieldMappingBuilder.customConverterId("childEntity");
                    }
                }).exclude("id");


            }
        };
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(updateBuilder);

        Map<String, CustomConverter> customConvertersWithId = new HashMap<>();
        customConvertersWithId.put("childEntity", new ChildEntityFieldConverter(factory.createEntityManager()));
        mapper.setCustomConvertersWithId(customConvertersWithId);


        MixRequest r = new MixRequest();
        r.setShow(new EntitySelector(2));

        EntityManager em = factory.createEntityManager();
        Mix mix = em.find(Mix.class, 1);

        //when
        mapper.map(r, mix);

        //then
        mix = em.find(Mix.class, 1);
        Assert.assertEquals(mix.getShow().getId(), 2);
        System.out.println(mix.getShow().getId());
        em.close();
    }

}