package de.hm.ccwi.api.benchmark.util;

import java.util.Comparator;

import de.hm.ccwi.api.benchmark.api.response.ResponseEntry;

/**
 * @author Marcel
 * @project extractTwitterData
 * @email mk@mkarrasch.de
 * @createdOn 27.11.2016
 * @package de.mk.extractTwitterData.Util
 */

public class SortResponseEntity implements Comparator<ResponseEntry> {

    /**
     * Sort all Entities alphabetical (A->Z) by comparing to entities
     *
     * @param e1 First Entity
     * @param e2 Second Entity
     * @return Return result of sorting
     */
    @Override
    public int compare (ResponseEntry e1, ResponseEntry e2) {
        return e1.getEntry().compareTo(e2.getEntry());
    }


}
