package dev.stormwatch.cs4300.entity;

import dev.stormwatch.cs4300.component.Component;
import dev.stormwatch.cs4300.component.ComponentSignature;

import java.util.Optional;

public class Entity {

    private final int id;
    private final EntityTag tag;
    private int componentSignature;
    private final Component[] components = new Component[Component.MAX_COMPONENTS];
    private boolean alive = true;

    public Entity(int id, EntityTag tag) {
        this.id = id;
        this.tag = tag;
    }

    public void addComponent(Component component) {
        this.componentSignature |= component.getSignature().ordinal();
        this.components[component.getSignature().ordinal()] = component;
    }

    public boolean hasComponent(ComponentSignature signature) {
        return (1 << signature.ordinal() & this.componentSignature) == 1 << signature.ordinal();
    }

    public Optional<Component> getComponent(ComponentSignature signature) {
        // TODO: consider just throwing an error if entity has no component of type
        // TODO: generics for better return type?
        Component component = this.components[signature.ordinal()];
        if (component == null) return Optional.empty();
        return Optional.of(component);
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void remove() {
        this.alive = false;
    }

}
