package com.v1.api.home;

import com.v1.api.dto.home.EChartDTO;
import com.v1.api.dto.home.TotalCountDTO;

public interface HomeRpcService {
    TotalCountDTO getTotalCount();

    EChartDTO getEChartData();

    void resetPassword(Long userId, String userType, String newPassword);
}
