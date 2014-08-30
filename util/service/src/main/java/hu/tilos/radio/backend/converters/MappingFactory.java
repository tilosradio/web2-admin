package hu.tilos.radio.backend.converters;

import hu.radio.tilos.model.Author;
import hu.radio.tilos.model.Mix;
import hu.tilos.radio.backend.data.types.AuthorSimple;
import hu.tilos.radio.backend.data.types.MixData;
import hu.tilos.radio.backend.data.types.MixSimple;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.jooq.DSLContext;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create mappers.
 */
@Named
public class MappingFactory {

    private String uploadUrl = "http://tilos.hu/upload/";

    public static DozerBeanMapper createDozer(EntityManager em) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        Map<String, CustomConverter> customConvertersWithId = new HashMap<>();
        customConvertersWithId.put(ChildEntityFieldConverter.ID, new ChildEntityFieldConverter(em));
        mapper.setCustomConvertersWithId(customConvertersWithId);
        return mapper;
    }

    public static DozerBeanMapper createDozer(DSLContext jooq) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        Map<String, CustomConverter> customConvertersWithId = new HashMap<>();
        //customConvertersWithId.put(ChildEntityFieldConverter.ID, new ChildEntityFieldConverter(em));
        mapper.setCustomConvertersWithId(customConvertersWithId);
        return mapper;
    }

    public static DozerBeanMapper createDozer(EntityManager em, BeanMappingBuilder builder) {
        DozerBeanMapper mapper = createDozer(em);
        mapper.addMapping(builder);
        return mapper;
    }

    public static DozerBeanMapper createDozer(DSLContext jooq, BeanMappingBuilder builder) {
        DozerBeanMapper mapper = createDozer(jooq);
        mapper.addMapping(builder);
        return mapper;
    }

    @Produces
    @Default
    public ModelMapper createModelMapper() {
        final Converter<String, String> uploadUrlConverter = new PrefixingConverter(uploadUrl);
        final Converter<String, String> sounds = new PrefixingConverter("http://archive.tilos.hu/sounds/", "http");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Author, AuthorSimple>() {
            @Override
            protected void configure() {
                using(uploadUrlConverter).map().setAvatar(source.getAvatar());
            }
        });
        modelMapper.addMappings(new PropertyMap<Mix, MixSimple>() {
            @Override
            protected void configure() {
                using(sounds).map().setLink(source.getFile());
                using(new AbstractConverter<String, Boolean>() {
                    @Override
                    protected Boolean convert(String source) {
                        if (source == null) {
                            return false;
                        }
                        return source.length() > 10;
                    }
                }).map(source.getContent()).setWithContent(false);
            }
        });
        modelMapper.addMappings(new PropertyMap<Mix, MixData>() {
            @Override
            protected void configure() {
                using(sounds).map().setLink(source.getFile());
            }
        });
        modelMapper.addMappings(new PropertyMap<MixData, Mix>() {
            @Override
            protected void configure() {
                using(new AbstractConverter<String, Date>() {

                    @Override
                    protected Date convert(String source) {
                        if (source == null) {
                            return null;
                        }
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd").parse(source);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).map(source.getDate()).setDate(null);
                skip().setShow(null);
            }
        });
        return modelMapper;

    }
}
