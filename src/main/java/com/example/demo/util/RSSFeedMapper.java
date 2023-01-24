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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.util.TextUtil.getStopWords;
import static com.example.demo.util.TextUtil.split;

@Slf4j
@Component
public class RSSFeedMapper {
    public HashMap<Header, List<Item>> mapAndGetItems(List<Object> objects) {
        log.info("mapping the fetch rss feeds to POJOs");
        HashMap<Header, List<Item>> hashMap = new HashMap<>();
        for (Object object : objects) {
            Map<String, Object> channel = this.getChannel(object);
            if (Objects.isNull(channel)) continue;

            Header header = this.mapHeader(channel);
            List<Item> items = this.mapItems(channel, header);

            hashMap.put(header, items);
        }
        return hashMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getChannel(Object object) {
        try {
            Map<String, Object> hashMap = (Map<String, Object>) object;
            if (hashMap == null || hashMap.isEmpty()) {
                log.error("Data type miss match. Failed to map Object to Map. Object: {}", object);
                return null;
            }
            return (Map<String, Object>) hashMap.get("channel");
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
                .map(this::mapItem)
                .map(item -> item.setHeader(header))
                .toList();
    }

    private Item mapItem(Map<String, Object> channel) {
        Item item = new Item();
        item.setTitle(stringMapper(channel, "title"));
        item.setLink(stringMapper(channel, "link"));
        HashSet<String> words = split(item.getTitle())
                .map(String::toLowerCase)
                .filter(word -> !getStopWords().contains(word))
                .collect(Collectors.toCollection(HashSet::new));
        item.setSeparatedWords(words);
        return item;
    }

    private Header mapHeader(Map<String, Object> channel) {
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
