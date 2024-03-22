package server.api;

import commons.dto.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import commons.dto.Expense;
import server.service.TagService;

import java.util.List;


@Controller
@RequestMapping("/rest/events/{event_id}/tags")
public class TagController {
    private final TagService tagService;

    TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseBody
    public Tag createTag(@PathVariable(name = "event_id") Integer eventId, @RequestBody Tag tag) {
        return tagService.createTag(eventId, tag);
    }

    @PutMapping("/{tag_id}")
    @ResponseBody
    public Tag updateTag(@PathVariable(name = "event_id") Integer eventId,
                         @PathVariable(name = "tag_id") Integer tagId,
                         @RequestBody Tag tag) throws BadRequestException {
        if (tag.getId() != tagId) throw new BadRequestException();
        return tagService.updateTag(eventId, tag);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Tag getTag(@PathVariable Integer id) {
        return tagService.getTagById(id);
    }

    @GetMapping
    @ResponseBody
    public List<Tag> getTags(@PathVariable(name = "event_id") Integer eventId) {
        return tagService.getTag(eventId);
    }

    @DeleteMapping("/{tag_id}")
    @ResponseBody
    public Tag deleteTag(@PathVariable(name = "event_id") Integer eventId,
                         @PathVariable(name = "tag_id") Integer tagId) {
        return tagService.deleteTag(eventId,tagId);
    }

}

