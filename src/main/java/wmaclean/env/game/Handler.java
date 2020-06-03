package wmaclean.env.game;

import wmaclean.env.Environment;
import wmaclean.env.game.objs.Enemy;
import wmaclean.env.game.objs.GameObject;
import wmaclean.env.game.objs.Player;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Handler {

    LinkedList<GameObject> objects = new LinkedList<GameObject>();

    private Environment environment;

    public Handler(Environment environment) {
        this.environment = environment;
    }

    public void tick(){
        for (GameObject tempObject : objects) {
            tempObject.tick();
        }

        checkPlayerCollision();
        checkPlayerAlive();
    }

    public void checkPlayerAlive() {
        if(this.getPlayer().getHealth() <= 0){
            this.environment.setGamestate(Gamestate.OVER);
        }
    }

    public void checkPlayerCollision() {
        for (GameObject tempObject : objects) {
            if(tempObject instanceof Enemy){
                if (tempObject.getX() < this.getPlayer().getX() + this.getPlayer().getWidth()
                        && tempObject.getX() + tempObject.getWidth() > this.getPlayer().getX()
                        && tempObject.getY() < this.getPlayer().getY() + this.getPlayer().getHeight()
                        && tempObject.getY() + tempObject.getHeight() > this.getPlayer().getY()){
                    this.getPlayer().handleCollision((Enemy)tempObject);
                }
            }
        }
    }

    public void render(Graphics g){
        for (GameObject tempObject : objects) {
            tempObject.render(g);
        }
    }

    public void addObject(GameObject object){
        this.objects.add(object);
    }

    public void removeObject(GameObject object){
        this.objects.remove(object);
    }

    public Player getPlayer(){
        for (GameObject tempObject : objects) {
            if (tempObject instanceof Player) {
                return (Player) tempObject;
            }
        }

        return null;
    }

    public void clear() {
        this.objects.clear();
    }

    public List<GameObject> getObjects() {
        return this.objects;
    }
}
