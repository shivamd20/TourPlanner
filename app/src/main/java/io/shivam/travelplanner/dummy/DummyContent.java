package io.shivam.travelplanner.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.shivam.travelplanner.R;
import io.shivam.travelplanner.skeletons.Route;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Stack<String>> ITEMS = new ArrayList<Stack<String>>();
    public static final Map<Integer,String> NODE_MAP=new HashMap<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Stack<String>> ITEM_MAP = new HashMap<String, Stack<String>>();

    private static final int COUNT = 2;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Stack<String> item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.get(0)+"", item);
    }

    private static Stack<String> createDummyItem(int position) {

       Stack<String> nodes=new Stack<>();

        Route route =new Route();

        route.to=position;
        route.from=456;

        nodes.push("raipur");
        nodes.push("bilaspur");
        nodes.push("durg");
        nodes.push("cjandigadh");
        nodes.push("rajnandgaon");

        return nodes;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */

}
