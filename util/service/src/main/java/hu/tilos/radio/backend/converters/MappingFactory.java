package hu.tilos.radio.backend.converters;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

public class MappingFactory {

    public static DozerBeanMapper createDozer(EntityManager em) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        Map<String, CustomConverter> customConvertersWithId = new HashMap<>();
        customConvertersWithId.put(ChildEntityFieldConverter.ID, new ChildEntityFieldConverter(em));
        mapper.setCustomConvertersWithId(customConvertersWithId);
        return mapper;
    }

    public static DozerBeanMapper createDozer(EntityManager em, BeanMappingBuilder builder) {
        DozerBeanMapper mapper = createDozer(em);
        mapper.addMapping(builder);
        return mapper;
    }
}
