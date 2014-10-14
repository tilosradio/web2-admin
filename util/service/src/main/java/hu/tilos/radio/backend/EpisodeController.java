package hu.tilos.radio.backend;

import hu.radio.tilos.model.*;
import hu.radio.tilos.model.type.TagType;
import hu.tilos.radio.backend.converters.TagUtil;
import hu.tilos.radio.backend.data.BookmarkData;
import hu.tilos.radio.backend.data.CreateResponse;
import hu.tilos.radio.backend.data.UpdateResponse;
import hu.tilos.radio.backend.data.types.EpisodeData;
import hu.tilos.radio.backend.episode.EpisodeUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.*;

@Path("/api/v1/episode")
public class EpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EpisodeController.class);

    @Inject
    ModelMapper modelMapper;

    @Inject
    private EntityManager entityManager;

    @Inject
    EpisodeUtil episodeUtil;

    @Inject
    UserInfo info;

    @Inject
    TagUtil tagUtil;

    @GET
    @Path("/{id}")
    @Security(role = Role.GUEST)
    @Produces("application/json")
    public EpisodeData get(@PathParam("id") int i) {
        EpisodeData r = modelMapper.map(entityManager.find(Episode.class, i), EpisodeData.class);
        return r;
    }

    @GET
    @Path("/{show}/{year}/{month}/{day}")
    @Security(role = Role.GUEST)
    @Produces("application/json")
    public EpisodeData getByDate(@PathParam("show") String showAlias, @PathParam("year") int year, @PathParam("month") int month, @PathParam("day") int day) {
        Show show = (Show) entityManager.createQuery("SELECT s FROM Show s WHERE s.alias = :alias").setParameter("alias", showAlias).getSingleResult();
        List<EpisodeData> episodeData = episodeUtil.getEpisodeData(show.getId(), new Date(year - 1900, month - 1, day), new Date(year - 1900, month - 1, day, 23, 59, 59));
        if (episodeData.size() == 0) {
            //todo, error handling
            throw new IllegalArgumentException("Can't find the appropriate episode");
        } else {
            return episodeData.get(0);
        }
    }


    @Produces("application/json")
    @Security(role = Role.AUTHOR)
    @POST
    @Transactional
    public CreateResponse create(EpisodeData data) {

        Episode entity = modelMapper.map(data, Episode.class);

        if (entity.getText() != null) {
            entity.getText().setAlias("");
            entity.getText().setFormat("default");
            entity.getText().setType("episode");
            entityManager.persist(entity.getText());
            entityManager.flush();
        }
        updateTags(entity);
        entityManager.persist(entity);
        entityManager.flush();
        return new CreateResponse(entity.getId());

    }

    @Produces("application/json")
    @Security(role = Role.AUTHOR)
    @Transactional
    @PUT
    @Path("/{id}")
    public UpdateResponse update(@PathParam("id") int id, EpisodeData inputData) {

        Episode entity = entityManager.find(Episode.class, id);

        modelMapper.map(inputData, entity);

        if (entity.getText() != null) {
            entity.getText().setAlias("");
            entity.getText().setFormat("default");
            entity.getText().setType("episode");
            entityManager.persist(entity.getText());
            entityManager.flush();
        }
        updateTags(entity);
        entityManager.persist(entity);
        entityManager.flush();

        return new UpdateResponse(entity.getId());
    }

    private void updateTags(Episode entity) {
        if (entity.getText() != null && entity.getText().getContent() != null) {
            try {
                Set<String> tags = tagUtil.getTags(entity.getText().getContent());
                List<Tag> existingTags = new ArrayList(entity.getText().getTags());
                Set<String> existingTagNames = new HashSet<>();
                for (Tag tag : existingTags) {
                    existingTagNames.add(tag.getName());
                }

                //check new tags
                for (String tag : tags) {
                    if (!existingTagNames.contains(tag)) {
                        Tag tagEntity = null;
                        try {
                            tagEntity = entityManager.createNamedQuery("tag.byName", Tag.class).setParameter("name", tag).getSingleResult();
                        } catch (NoResultException ex) {
                            tagEntity = new Tag();
                            tagEntity.setName(tag);
                            tagEntity.setType(TagType.GENERIC);
                            entityManager.persist(tagEntity);
                            entityManager.flush();
                        }
                        //maintain both side
                        tagEntity.getTaggedTexts().add(entity.getText());
                        entity.getText().getTags().add(tagEntity);
                    }
                }


                //check removed tags
                for (Tag oldTag : existingTags) {
                    if (!tags.contains(oldTag.getName())) {
                        //registered but the new text doesn't contain it
                        oldTag.getTaggedTexts().remove(entity.getText());
                        entity.getText().getTags().remove(oldTag);
                    }
                }

            } catch (Exception ex) {
                LOG.error("Can't create tag for entity " + entity, ex);
            }
        }
    }


    public EntityManager getEntityManager() {
        return entityManager;
    }

}
