package com.sqrt4.cubja.model.fst;

import com.sqrt4.cubja.model.GameCubeImage;

/**
 * Created by James on 29/07/2014.
 */
public abstract class FSNode implements Comparable<FSNode> {
    public GameCubeImage container;
    public DirNode parent;
    public String name;

    public FSNode(GameCubeImage container, String name, DirNode parent) {
        this.container = container;
        this.name = name;
        this.parent = parent;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if(parent != null) {
            sb.append(parent.getFullName());
            if(sb.length() != 0)
              sb.append("/");
            sb.append(name);
        }
        return sb.toString();
    }
}