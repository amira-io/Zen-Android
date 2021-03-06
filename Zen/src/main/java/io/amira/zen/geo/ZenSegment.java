/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.geo;

import com.google.android.gms.maps.model.LatLng;

public class ZenSegment {
    /** Points in this segment. **/
    private LatLng start;
    /** Turn instruction to reach next segment. **/
    private String instruction;
    /** Length of segment. **/
    private int length;
    /** Distance covered. **/
    private double distance;

    /**
     * Create an empty segment.
     */

    public ZenSegment() {
    }


    /**
     * Set the turn instruction.
     * @param turn Turn instruction string.
     */

    public void setInstruction(final String turn) {
        this.instruction = turn;
    }

    /**
     * Get the turn instruction to reach next segment.
     * @return a String of the turn instruction.
     */

    public String getInstruction() {
        return instruction;
    }

    /**
     * Add a point to this segment.
     * @param point GeoPoint to add.
     */

    public void setPoint(final LatLng point) {
        start = point;
    }

    /** Get the starting point of this
     * segment.
     * @return a GeoPoint
     */

    public LatLng startPoint() {
        return start;
    }

    /** Creates a segment which is a copy of this one.
     * @return a Segment that is a copy of this one.
     */

    public ZenSegment copy() {
        final ZenSegment copy = new ZenSegment();
        copy.start = start;
        copy.instruction = instruction;
        copy.length = length;
        copy.distance = distance;
        return copy;
    }

    /**
     * @param length the length to set
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

}
