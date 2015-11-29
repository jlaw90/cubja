package com.sqrt4.cubja.model.fst;

import com.sqrt4.cubja.model.GameCubeImage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James on 29/07/2014.
 */
public class DirNode extends FSNode {
    public int endId;
    public final List<FSNode> children = new ArrayList<FSNode>();

    public DirNode(GameCubeImage container, String name, DirNode parent, int endEntry) {
        super(container, name, parent);
        this.endId = endEntry;
    }

    @Override
    public int compareTo(FSNode o) {
        if(!(o instanceof DirNode))
            return -1;
        return name.compareTo(((DirNode) o).name);
    }
}