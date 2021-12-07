package net.modificationstation.stationapi.impl.oredict;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class FilterableOreDictMap extends TreeMap<String, List<OreDictEntryObject>> {

    public List<OreDictEntryObject> filterMap(String filterText) {
        List<OreDictEntryObject> newList = new ArrayList<>();
        if(filterText.length() > 0) {
            char nextLetter = (char) (filterText.charAt(filterText.length() -1) + 1);
            String end = filterText.substring(0, filterText.length()-1) + nextLetter;
            SortedMap<String, List<OreDictEntryObject>> map = this.subMap(filterText, end);
            for (String key : map.keySet()) {
                if (filterText.length() == key.length() || (key.length() > filterText.length() && Character.isUpperCase(key.charAt(filterText.length()+1)))) {
                    newList.addAll(map.get(key));
                }
            }
        }
        else {
            this.values().forEach(newList::addAll);
        }
        return newList;
    }
}
