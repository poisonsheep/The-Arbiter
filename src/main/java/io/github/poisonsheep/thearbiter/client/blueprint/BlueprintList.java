package io.github.poisonsheep.thearbiter.client.blueprint;

import java.util.ArrayList;
import java.util.List;

public class BlueprintList {
    public static final BlueprintList INSTANCE = new BlueprintList();
    private BlueprintList() {}
    public List<String> blueprints = new ArrayList<>();
}
