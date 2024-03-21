package server.service;

import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import jakarta.transaction.Transactional;
import org.springframework.core.metrics.StartupStep;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.database.UserRepository;
import server.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TagService {
    private TagRepository tagRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private Function<server.model.Tag, Tag> mapper = tag -> new commons.dto.Tag(
            tag.getId(),
            tag.getName(),
            tag.getColor(),
            tag.getEvent().getId());
    private Function<Tag, server.model.Tag> mapperInv = tag -> new server.model.Tag(
            tag.getId(),
            tag.getName(),
            tag.getColor(),
            eventRepository.getById(tag.getId()));

    private server.model.Tag mapperInv(Tag tag) {
        return new server.model.Tag(
                tag.getId(),
                tag.getName(),
                tag.getColor(),
                eventRepository.getById(tag.getId()));
    }

    protected TagService(TagRepository tagRepository,
                             EventRepository eventRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Tag> getTags() {
        return tagRepository.findAll().stream().map(mapper).toList();
    }

    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).map(mapper).orElse(null);
    }

    @Transactional
    public Tag createTag(Integer eventId, Tag tag) {
        server.model.Event event = eventRepository.getById(eventId);
        server.model.Tag tagEntity = new server.model.Tag(
                null,
                tag.getName(),
                tag.getColor(),
                event);
        List<server.model.Tag> listTagsPrev = event.getTags();
        listTagsPrev.add(tagEntity);
        event.setTags(listTagsPrev);

        eventRepository.save(event);
        server.model.Tag createdEntity = tagRepository.save(tagEntity);
        return new Tag(createdEntity.getId(),
                tag.getName(),
                tag.getColor(),
                tag.getEventId());
    }

    public Tag updateTag(Integer eventId, Tag tag) {
        server.model.Event event = eventRepository.getById(eventId);
        server.model.Tag existingTag = tagRepository.findByTagId(tag.getId());
        if (existingTag.getEvent().getId() != eventId)
            throw new IllegalArgumentException("Tag doesn't belong with provided event");
        if (existingTag != null) {
            existingTag.setName(tag.getName());
            existingTag.setColor(tag.getColor());
            server.model.Tag savedTag = tagRepository.save(existingTag);
            return mapper.apply(savedTag);
        }
        return null;
    }

    public List<Tag> getTag(Integer eventId) {
        return tagRepository.findAllByEventId(eventId).stream().map(mapper).toList();
    }

    public Tag deleteTag(Integer eventId, Integer tagId) {
        Optional<server.model.Tag> tag = tagRepository.findById(tagId);
        return tag.map(value -> {
            if (eventId != value.getEvent().getId()) {
                throw new IllegalArgumentException("Expense doesn't belong to given event");
            }
            tagRepository.deleteById(tagId);
            return mapper.apply(value);
        }).orElseThrow(() -> new IllegalArgumentException("Expense to be deleted not found"));
    }

    private List<server.model.Tag> getTags(List<commons.dto.Tag> tags) {
        return tags.stream().map(mapperInv).toList();
    }

    private User getUserById(Integer it) {
        User user = userRepository.getById(it);
        if (user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

    private List<Integer> getUserIds(List<User> users) {
        return users.stream().map(it -> it.getId()).toList();
    }

    public List<Tag> getTags(Integer eventId) {
        return tagRepository.findAllByEventId(eventId).stream().map(mapper).toList();
    }

    private List<User> getUsers(List<Integer> userIds) {
        return userIds.stream().map(it -> getUserById(it)).toList();
    }


    private server.model.Event getModelEvent(Event event) {
        server.model.Event newEvent = new server.model.Event();
        newEvent.setId(event.getId());
        newEvent.setTitle(event.getTitle());
        newEvent.setUsers(getUsers(event.getUserIds()));
        return newEvent;
    }

}
