package com.bitdecay.ludum.dare.collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.RemoveNowComponent;
import com.bitdecay.ludum.dare.gameobject.AINodeGameObject;
import com.bitdecay.ludum.dare.interfaces.*;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GameObjects implements IUpdate, IDraw, IShapeDraw {
    List<GameObject> gameObjects;

    List<GameObject> pendingAdds;
    List<GameObject> pendingRemoves;

    public GameObjects() {
        gameObjects = new ArrayList<>();
        pendingAdds = new ArrayList<>();
        pendingRemoves = new ArrayList<>();
    }

    public Iterator getIter() {
        return gameObjects.iterator();
    }

    public <T extends GameObject> List<T> getGameObjectsOfType(Class<T> clazz){
        return gameObjects.stream().filter(obj -> clazz.isAssignableFrom(obj.getClass())).map(obj -> clazz.cast(obj)).collect(Collectors.toList());
    }

    // Will be added at start of next update loop.
    public void add(GameObject obj) {
        pendingAdds.add(obj);
    }

    public void update(float delta) {
        // Add pending objects.
        doAdds();
        gameObjects.forEach(obj -> {
            obj.update(delta);

            if (obj.hasComponent(RemoveNowComponent.class) || (obj instanceof IRemoveable && ((IRemoveable) obj).shouldRemove())) {
                pendingRemoves.add(obj);
            }
        });

        doRemoves();
    }

    public void doAdds() {
        gameObjects.addAll(pendingAdds);
        pendingAdds.clear();
    }

    public void doRemoves() {
        // Remove any object that has flagged itself for removal during the loop.
        gameObjects.removeAll(pendingRemoves);
        // Make sure to let it know it was removed.
        pendingRemoves.forEach(obj -> ((IRemoveable) obj).remove());
        pendingRemoves.clear();
    }


    public List<GameObject> findWithComponents(Class<? extends IComponent>... components){
        return gameObjects.stream().filter(obj -> {
            for (int i = 0; i < components.length; i++)
                if (!obj.hasComponent(components[i])) return false;
            return true;
        }).collect(Collectors.toList());
    }

    public void preDraw(AnimagicSpriteBatch batch) {
        gameObjects.forEach(obj -> obj.preDraw(batch));
    }

    public void draw(SpriteBatch batch) {
        gameObjects.forEach(obj -> obj.draw(batch));
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        gameObjects.forEach(obj -> obj.draw(shapeRenderer));
    }

    public void clear() {
        gameObjects.clear();
        pendingRemoves.clear();
        pendingAdds.clear();
    }

    public List<AINodeGameObject> getAINodes() {
        List<AINodeGameObject> nodes = new ArrayList<>();
        gameObjects.forEach(obj -> {
            if (obj instanceof AINodeGameObject) nodes.add((AINodeGameObject) obj);
        });
        return nodes;
    }
}
