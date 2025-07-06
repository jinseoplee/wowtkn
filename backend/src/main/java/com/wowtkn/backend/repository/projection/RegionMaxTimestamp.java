package com.wowtkn.backend.repository.projection;

import com.wowtkn.backend.common.Region;

public interface RegionMaxTimestamp {

    Region getRegion();

    Long getMaxTimestamp();
}
