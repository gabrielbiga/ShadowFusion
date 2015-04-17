/*
 * ========= (C) Copyright 2014 Liquare, L.L.C. All rights reserved. ===========
 * 
 * The copyright to the contents herein is the property of Liquare, L.L.C.
 * The contents may be used and/or copied only with the written permission of
 * Liquare, L.L.C., or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the contents have been supplied.
 * 
 * $Header: $
 * $NoKeywords: $
 * =============================================================================
 */

package ui;

import java.util.ArrayList;

/**
 * Relational ID->Java Object dynamic cache
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class ObjectCache {
    private class Node {
        int treeIdx;
        Object obj;
    }
    
    private ArrayList<Node> cache = new ArrayList<>();
    
    public void clear() {
        cache.clear();
    }
    
    public void addNode(int treeIdx, Object obj) {
        Node newNode = new Node();
        newNode.treeIdx = treeIdx;
        newNode.obj     = obj;
        
        cache.add(newNode);
    }
    
    public Object getNodeObj(int treeIdx) {
        int i;
        
        for(i = 0; i < cache.size(); i++) {
            Node nodeAt = cache.get(i);
            
            if (nodeAt.treeIdx == treeIdx) {
                return nodeAt.obj;
            }
        }
        
        return null;
    }
    
    public void removeNode(int treeIdx) {
        for(int i = 0; i < cache.size(); i++) {
            Node nodeAt = cache.get(i);
            
            if (nodeAt.treeIdx == treeIdx) {
                cache.remove(i);
            }
        }
    }
    
    public int getCount() {
        return cache.size();
    }
}
