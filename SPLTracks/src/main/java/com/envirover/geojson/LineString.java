/*
This file is part of SPLTracks application.

Copyright (C) 2017 Envirover

SPLGroundControl is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SPLStrean is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SPLTracks.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.envirover.geojson;

import java.util.ArrayList;
import java.util.Collection;

public class LineString extends Geometry {

    private final Collection<Collection<Double>> coordinates;

    public LineString() {
        this.type = "LineString";
        this.coordinates = new ArrayList<Collection<Double>>();
    }

    public LineString(Collection<Collection<Double>> coordinates) {
        this.type = "LineString";
        this.coordinates = coordinates;
    }

    public Collection<Collection<Double>> getCoordinates() {
        return coordinates;
    }

}
