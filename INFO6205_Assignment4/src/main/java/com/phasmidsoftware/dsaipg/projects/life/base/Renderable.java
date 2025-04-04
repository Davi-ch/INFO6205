/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.life.base;

/**
 * Interface Renderable.
 */
public interface Renderable {
    /**
     * Method to create a String representation of an object such that said
     * string will provide an informative and nicely formatted visualization of the object.
     *
     * @return a String which, typically, includes newlines≥
     */
    String render();
}