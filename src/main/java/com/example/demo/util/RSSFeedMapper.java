package com.example.demo.util;

import com.example.demo.domain.Header;
import com.example.demo.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.util.FeedMapperHelper.getStopWords;

@Slf4j
@Component
public class RSSFeedMapper {
    @SuppressWarnings("unchecked")
    public List<Item> mapItems(Object object) {
        try {
            Map<String, Object> hashMap = (Map<String, Object>) object;
            if (hashMap == null || hashMap.isEmpty()) {
                log.error("Data type miss match. Failed to map Object to Map. Object: {}", object);
                return null;
            }
            Map<String, Object> channel = (Map<String, Object>) hashMap.get("channel");
            return this.mapItems(channel, this.getHeader(channel));
        } catch (Exception e) {
            log.error("Exception: {} occurred during parsing the object.", e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Item> mapItems(Map<String, Object> channel, Header header) {
        List<HashMap<String, Object>> objectItems = (List<HashMap<String, Object>>) channel.get("item");
        if (objectItems == null) {
            log.error("Attribute type miss match. Failed to find item from the Channel. Header= {}\nChannel= {}",
                    header.toString(), channel);
            return null;
        }
        return objectItems.stream()
                .map(this::getItem)
                .map(item -> item.setHeader(header))
                .collect(Collectors.toList());
    }

    private Item getItem(Map<String, Object> channel) {
        Item item = new Item();
        item.setTitle(stringMapper(channel, "title"));
        item.setLink(stringMapper(channel, "link"));
        item.setDescription(stringMapper(channel, "description"));

        HashSet<String> words = Stream.of(item.getTitle().split(" "))
                .filter(word -> !getStopWords().contains(word))
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));

        item.setDistinctWords(words);

        return item;
    }

    private Header getHeader(Map<String, Object> channel) {
        Header header = new Header();
        header.setTitle(stringMapper(channel, "title"));
        header.setLink(stringMapper(channel, "link"));
        header.setLanguage(stringMapper(channel, "language"));
        header.setDescription(stringMapper(channel, "description"));
        return header;
    }

    @SuppressWarnings("unchecked")
    private String stringMapper(Map<String, Object> map, String key) {
        if (map.get(key) == null) {
            return "NULL-VALUE";
        }
        if (String.class.isAssignableFrom(map.get(key).getClass())) {
            return (String) map.get(key);
        }
        if (Collection.class.isAssignableFrom(map.get(key).getClass())) {
            Collection<Object> collection = (Collection<Object>) map.get(key);
            for (Object next : collection) {
                if (next != null && String.class.isAssignableFrom(next.getClass())) {
                    return (String) next;
                }
            }
        }
        return "TYPE-MISS-MATCH";
    }
}
